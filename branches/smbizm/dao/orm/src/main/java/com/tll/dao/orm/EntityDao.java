package com.tll.dao.orm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.Comparator;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.IComparatorTranslator;
import com.tll.criteria.ICriteria;
import com.tll.criteria.ICriterion;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumn;
import com.tll.dao.SortDir;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.CollectionUtil;

/**
 * EntityDao - Hibernate dao implementation.
 * @author jpk
 */
public class EntityDao extends HibernateJpaSupport implements IEntityDao {

	/**
	 * Used for transforming entity results into a native friendly handle.
	 */
	private static final ResultTransformer ENTITY_RESULT_TRANSFORMER = new EntitySearchResultTransformer();

	/**
	 * String representing the beginning of an order by clause in an SQL string.
	 * Used internally to manually modify sql query strings in order to support
	 * dynamic sorting.
	 */
	private static final String ORDER_BY_TOKEN = "order by ";

	/**
	 * @param <T>
	 * @param maybeProxy
	 * @param baseClass
	 * @return the deproxied instance
	 * @throws ClassCastException
	 */
	private static <T> T deproxy(Object maybeProxy, Class<T> baseClass) throws ClassCastException {
		if(maybeProxy instanceof HibernateProxy) {
			return baseClass.cast(((HibernateProxy) maybeProxy).getHibernateLazyInitializer().getImplementation());
		}
		return baseClass.cast(maybeProxy);
	}

	/**
	 * Applies sorting objects to the hibernate criteria object. This method takes
	 * a <code>DetachedCriteria</code> implementation simply because there is no
	 * common interface for <code>DetachedCriteria</code> and
	 * <code>Criteria</code>.
	 * @param dc the hibernate criteria object
	 * @param sorting sorting object
	 * @throws InvalidCriteriaException
	 */
	private static void applySorting(DetachedCriteria dc, Sorting sorting) throws InvalidCriteriaException {
		if(sorting != null) {
			final SortColumn[] columns = sorting.getColumns();
			for(final SortColumn element : columns) {
				// final SortDir dir = element.getDirection() == null ? SortDir.ASC :
				// element.getDirection();
				final String column = element.getPropertyName();
				final Boolean ignoreCase = element.getIgnoreCase();
				applyAliasIfNecessary(dc, column);
				final Order order = (element.getDirection() == SortDir.ASC) ? Order.asc(column) : Order.desc(column);
				if(Boolean.TRUE.equals(ignoreCase)) {
					order.ignoreCase();
				}
				dc.addOrder(order);
			}
		}
	}

	/**
	 * Applies aliases if necessary.
	 * @param dc
	 * @param propPath
	 * @throws InvalidCriteriaException
	 */
	private static void applyAliasIfNecessary(DetachedCriteria dc, String propPath) throws InvalidCriteriaException {
		// if this is a foreign key property, final join to entity table is not
		// necessary
		if(propPath.endsWith("." + IEntity.PK_FIELDNAME)) {
			propPath = propPath.substring(0, propPath.length() - 3);
		}
		// add alias if criterion refers to a nested property
		final int index = propPath.indexOf('.');
		if(index > 0) {
			final String suffix = propPath.substring(index + 1);

			// NOTE: currently don't know how to handle nested aliases
			if(suffix.indexOf('.') >= 0) {
				throw new InvalidCriteriaException("Unable to handle 3+ deep criterion property paths");
			}
			final String alias = propPath.substring(0, index);
			dc.createAlias(alias, alias);
		}
	}

	/**
	 * The db dialect handler.
	 * <p>
	 * <strong>NOTE: </strong>To retain thread safety, do <em>not</em> publish
	 * this member.
	 */
	private final IDbDialectHandler dbDialectHandler;

	/**
	 * The comparator translator.
	 * <p>
	 * <strong>NOTE: </strong>To retain thread safety, do <em>not</em> publish
	 * this member.
	 */
	private final IComparatorTranslator<Criterion> comparatorTranslator;

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 */
	@Inject
	public EntityDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler) {
		super(emPrvdr);
		this.dbDialectHandler = dbDialectHandler;
		this.comparatorTranslator = new ComparatorTranslator();
	}

	public <E extends IEntity> E load(PrimaryKey<E> key) {
		final E e = getEntityManager().getReference(key.getType(), key.getId());
		return deproxy(e, key.getType());
	}

	public <E extends IEntity> E load(IBusinessKey<E> key) {
		try {
			return findEntity(new com.tll.criteria.Criteria<E>(key.getType()));
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from business key: " + e.getMessage(), e);
		}
	}

	public <N extends INamedEntity> N load(NameKey<N> nameKey) {
		try {
			final com.tll.criteria.Criteria<N> nc = new com.tll.criteria.Criteria<N>(nameKey.getType());
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			return findEntity(nc);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> loadAll(Class<E> entityType) {
		return DetachedCriteria.forClass(entityType).getExecutableCriteria(getSession(getEntityManager())).list();
	}

	public <E extends IEntity> E persist(E entity) {
		return persistInternal(entity, true, getEntityManager());
	}

	public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) {
		if(entities == null) return null;
		final EntityManager em = getEntityManager();
		final Collection<E> merged = new HashSet<E>(entities.size());
		for(final E e : entities) {
			merged.add(persistInternal(e, false, em));
		}
		em.flush();
		return merged;
	}

	private <E extends IEntity> E persistInternal(E entity, boolean flush, EntityManager em) {
		try {
			final E merged = em.merge(entity);
			em.persist(merged);
			if(flush) em.flush();
			return merged;
		}
		catch(final RuntimeException re) {
			throw dbDialectHandler.translate(re);
		}
	}

	public <E extends IEntity> void purge(E entity) {
		purgeInternal(entity, true, getEntityManager());
	}

	public <E extends IEntity> void purgeAll(Collection<E> entities) {
		if(!CollectionUtil.isEmpty(entities)) {
			final EntityManager em = getEntityManager();
			for(final E e : entities) {
				purgeInternal(e, false, em);
			}
			em.flush();
		}
	}

	private <E extends IEntity> void purgeInternal(E entity, boolean flush, EntityManager em) {
		try {
			em.remove(em.merge(entity));
			if(flush) em.flush();
		}
		catch(final RuntimeException re) {
			throw dbDialectHandler.translate(re);
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findEntities(ICriteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null || criteria.getCriteriaType().isScalar()) {
			throw new InvalidCriteriaException("A criteria type must be specified and be non-scalar.");
		}
		return (List<E>) processCriteria(criteria, sorting, !criteria.getCriteriaType().isQuery(),
				CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	}

	public <E extends IEntity> E findEntity(ICriteria<E> criteria) throws InvalidCriteriaException {
		final List<E> list = findEntities(criteria, null);
		if(list == null || list.size() < 1) {
			throw new EntityNotFoundException("No matching entity found.");
		}
		else if(list.size() > 1) {
			throw new NonUniqueResultException("More than one matching entity found.");
		}
		assert list.size() == 1;
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<SearchResult<E>> find(ICriteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("A criteria type must be specified.");
		}
		return (List<SearchResult<E>>) processCriteria(criteria, sorting, !criteria.getCriteriaType().isQuery(), criteria
				.getCriteriaType().isScalar() ? (new ScalarSearchResultTransformer(criteria.getEntityClass()))
						: ENTITY_RESULT_TRANSFORMER);
	}

	/**
	 * Process criteria instances providing a distinct list of matching results
	 * whose elements are either {@link SearchResult} or {@link IEntity} instances
	 * depending on the specified {@link CriteriaType}.
	 * @param <E>
	 * @param criteria Assumed non-<code>null</code>.
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @param resultTransformer The result transformer. May NOT be
	 *        <code>null</code>.
	 * @return List of distinct matching results
	 * @throws InvalidCriteriaException When the criteria type is invalid or
	 *         otherwise.
	 */
	private <E extends IEntity> List<?> processCriteria(ICriteria<E> criteria, Sorting sorting, boolean applySorting,
			ResultTransformer resultTransformer) throws InvalidCriteriaException {
		assert criteria != null && resultTransformer != null;
		if(criteria.getCriteriaType().isQuery()) {
			// presume named query ref
			final ISelectNamedQueryDef nq = criteria.getNamedQueryDefinition();
			if(nq == null || nq.getQueryName() == null) {
				throw new InvalidCriteriaException("No named query specified.");
			}
			return assembleQuery(getEntityManager(), nq.getQueryName(), criteria.getQueryParams(), sorting,
					resultTransformer, true).list();
		}
		// translate to hbm criteria
		final DetachedCriteria dc = DetachedCriteria.forClass(criteria.getEntityClass());
		dc.setResultTransformer(resultTransformer);
		applyCriteria(dc, criteria, sorting, applySorting);
		return findByDetatchedCriteria(dc);
	}

	/**
	 * Executes the query as defined by the given detached criteria.
	 * @param dc The detached criteria
	 * @return List of results of unknown type.
	 */
	private List<?> findByDetatchedCriteria(DetachedCriteria dc) {
		return dc.getExecutableCriteria(getSession(getEntityManager())).list();
	}

	/**
	 * Translates native criteria to a new {@link org.hibernate.Query} instance.
	 * @param em The active entity manager
	 * @param queryName
	 * @param queryParams
	 * @param sorting
	 * @param resultTransformer May be <code>null</code>.
	 * @param cacheable Is this query cacheable?
	 * @return New Query instance
	 * @throws InvalidCriteriaException When no query name is specified in the
	 *         given criteria.
	 */
	private org.hibernate.Query assembleQuery(EntityManager em, String queryName,
			Collection<IQueryParam> queryParams, Sorting sorting, ResultTransformer resultTransformer, boolean cacheable)
	throws InvalidCriteriaException {
		if(queryName == null) {
			throw new InvalidCriteriaException("No query name specified.");
		}

		final Session session = getSession(em);

		org.hibernate.Query hbmQ = session.getNamedQuery(queryName);

		// apply sorting (if specified)
		if(sorting != null) {
			final StringBuilder sb = new StringBuilder(hbmQ.getQueryString());
			int indx = sb.indexOf(ORDER_BY_TOKEN);
			if(indx >= 0) {
				indx += ORDER_BY_TOKEN.length();
				assert sb.length() - 1 > indx;
				sb.setLength(indx);
			}
			sb.append(sorting.toString());
			hbmQ = session.createQuery(sb.toString());
		}

		// fill the named params (if any)
		if(queryParams != null && queryParams.size() > 0) {
			for(final IQueryParam queryParam : queryParams) {
				hbmQ.setParameter(queryParam.getPropertyName(), queryParam.getValue());
			}
		}

		if(resultTransformer != null) {
			hbmQ.setResultTransformer(resultTransformer);
		}

		hbmQ.setCacheable(cacheable);

		return hbmQ;
	}

	/**
	 * Translates criterion objects to the hibernate criterion objects. This
	 * method takes a <code>DetachedCriteria</code> implementation simply because
	 * there is no common interface for <code>DetachedCriteria</code> and
	 * <code>Criteria</code>. This method may be overridden by subclasses and thus
	 * is not called from processUniqueCriteria(ICriteria).
	 * @param <E>
	 * @param dc the hibernate criteria object
	 * @param criteria Criteria object
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @throws InvalidCriteriaException
	 */
	private <E extends IEntity> void applyCriteria(DetachedCriteria dc, ICriteria<E> criteria, Sorting sorting,
			boolean applySorting) throws InvalidCriteriaException {
		if(criteria.isSet()) {
			final CriterionGroup pg = criteria.getPrimaryGroup();
			Junction j = null;
			if(pg.size() > 1) {
				j = pg.isConjunction() ? Restrictions.conjunction() : Restrictions.disjunction();
				dc.add(j);
			}
			for(final ICriterion crit : pg) {
				applyCriterion(dc, crit, criteria, sorting, j);
			}
		} // else all entities will be retrieved
		if(applySorting) {
			applySorting(dc, sorting);
		}
	}

	private <E extends IEntity> void applyCriterion(DetachedCriteria dc, ICriterion ctn, ICriteria<E> criteria,
			Sorting sorting, Junction junction) throws InvalidCriteriaException {
		if(!ctn.isSet()) {
			return;
		}

		if(ctn.isGroup()) {
			final CriterionGroup g = (CriterionGroup) ctn;
			final Junction j = g.isConjunction() ? Restrictions.conjunction() : Restrictions.disjunction();
			dc.add(j);

			for(final ICriterion c : g) {
				applyCriterion(dc, c, criteria, sorting, j);
			}

			return;
		}

		DetachedCriteria dc2 = dc;
		String fieldName = ctn.getField();

		// PROPERTY PATH LOGIC
		final String[] hPaths = StringUtils.delimitedListToStringArray(fieldName, ".");
		// only apply sub-criteria when the path is 2+ deep
		// (i.e. when the path portion of the field contains at least one dot)
		if(hPaths.length > 2) {
			for(int i = 0; i < hPaths.length - 1; i++) {
				// create sub-criteria to handle the association path
				dc2 = dc.createCriteria(hPaths[i]);
			}
			fieldName = hPaths[hPaths.length - 1];
		}
		else {
			// apply an alias (if not sorting against this field - else hibernate
			// duplicate alias exception occurrs)
			if(sorting == null || sorting.getPrimarySortColumn().getPropertyName().equals(fieldName)) {
				applyAliasIfNecessary(dc2, fieldName);
			}
		}

		final Comparator cmptr = ctn.getComparator();
		final Criterion expression = comparatorTranslator.translate(cmptr, fieldName, ctn.getValue());
		applyCaseSensitivity(ctn, expression);
		if(junction == null) {
			dc2.add(expression);
		}
		else {
			junction.add(expression);
			if(dc2 != dc) {
				dc2.add(junction);
			}
		}
	}

	private void applyCaseSensitivity(ICriterion ctn, Criterion expression) {
		if(ctn.isCaseSensitive()) {
			return; // this is hibernate's default
		}
		if(expression instanceof SimpleExpression == false) {
			return;
		}
		final Object v = ctn.getValue();
		if(v instanceof String == false) {
			return;
		}
		final Comparator c = ctn.getComparator();
		if(Comparator.CONTAINS == c || Comparator.ENDS_WITH == c || Comparator.EQUALS == c || Comparator.LIKE == c
				|| Comparator.STARTS_WITH == c) {

			((SimpleExpression) expression).ignoreCase();
		}
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findByIds(Class<E> entityType, List<Integer> ids, Sorting sorting) {
		final com.tll.criteria.Criteria<E> nativeCriteria = new com.tll.criteria.Criteria<E>(entityType);
		nativeCriteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, ids, Comparator.IN, false);
		final DetachedCriteria dc = DetachedCriteria.forClass(nativeCriteria.getEntityClass());
		dc.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		try {
			applyCriteria(dc, nativeCriteria, sorting, true);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException(e.getMessage(), e);
		}
		return (List<E>) findByDetatchedCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<Integer> getIds(ICriteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria.getCriteriaType().isQuery()) {
			throw new InvalidCriteriaException("Ids are not supplied for direct queries!");
		}
		final DetachedCriteria dc = DetachedCriteria.forClass(criteria.getEntityClass());
		dc.setProjection(Projections.property(IEntity.PK_FIELDNAME));
		applyCriteria(dc, criteria, sorting, true);
		return (List<Integer>) findByDetatchedCriteria(dc);

	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> getEntitiesFromIds(Class<E> entityClass, Collection<Integer> ids, Sorting sorting) {
		if(CollectionUtil.isEmpty(ids)) {
			return new ArrayList<E>();
		}
		final DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		dc.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		dc.add(Property.forName(IEntity.PK_FIELDNAME).in(ids));
		try {
			applySorting(dc, sorting);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Invalid criteria: " + e.getMessage(), e); // shouldn
			// 't
			// happen
		}
		return (List<E>) findByDetatchedCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> IPageResult<SearchResult<E>> getPage(ICriteria<E> criteria, Sorting sorting, int offset,
			int pageSize) throws InvalidCriteriaException {
		assert criteria != null && criteria.getCriteriaType() != null;
		List<SearchResult<E>> rlist = null;
		int totalCount = -1;
		final EntityManager em = getEntityManager();
		switch(criteria.getCriteriaType()) {

			case ENTITY: {
				final DetachedCriteria dc = DetachedCriteria.forClass(criteria.getEntityClass());

				if(sorting == null) {
					// sorting is necessary in the case of IPage results due to necessary
					// in memory manipulation to provide a distinct list of results
					throw new InvalidCriteriaException("Paged results require a sorting directive");
				}
				applyCriteria(dc, criteria, sorting, true);
				final Session session = (Session) getEntityManager().getDelegate();
				final Criteria hCrit = dc.getExecutableCriteria(session);
				final Integer rowCount = (Integer) hCrit.setProjection(Projections.rowCount()).uniqueResult();
				assert rowCount != null;
				totalCount = rowCount.intValue();

				// restore the criteria
				hCrit.setProjection(null);
				hCrit.setResultTransformer(ENTITY_RESULT_TRANSFORMER);

				rlist = hCrit.setFirstResult(offset).setMaxResults(pageSize).list();
				break;
			}

			case ENTITY_NAMED_QUERY: {
				// get the count by convention looking for a couter-part named query w/
				// same name and additional suffix of .count
				final ISelectNamedQueryDef snq = criteria.getNamedQueryDefinition();
				if(!snq.isSupportsPaging()) {
					throw new InvalidCriteriaException(snq.getQueryName() + " query does not support paging.");
				}
				final String queryName = snq.getQueryName();
				final String countQueryName = snq.getQueryName() + ".count";
				final org.hibernate.Query cq = assembleQuery(em, countQueryName, null, null, null, false);
				final Long count = (Long) cq.uniqueResult();
				assert count != null;
				totalCount = count.intValue();
				final org.hibernate.Query q =
					assembleQuery(em, queryName, criteria.getQueryParams(), sorting, ENTITY_RESULT_TRANSFORMER, true);
				rlist = q.setFirstResult(offset).setMaxResults(pageSize).list();
				break;
			}

			case SCALAR_NAMED_QUERY: {
				// get the count by convention looking for a couter-part named query w/
				// same name and additional suffix of .count
				final ISelectNamedQueryDef snq = criteria.getNamedQueryDefinition();
				final String queryName = snq.getQueryName();
				final String countQueryName = snq.getQueryName() + ".count";
				final org.hibernate.Query cq =
					assembleQuery(em, countQueryName, criteria.getQueryParams(), null, null, false);
				final Long count = (Long) cq.uniqueResult();
				assert count != null;
				totalCount = count.intValue();
				final ResultTransformer rt = new ScalarSearchResultTransformer(criteria.getEntityClass());
				final org.hibernate.Query q = assembleQuery(em, queryName, criteria.getQueryParams(), sorting, rt, true);
				rlist = q.setFirstResult(offset).setMaxResults(pageSize).list();
				break;
			}
		}
		if(rlist == null) {
			throw new InvalidCriteriaException("Invalid paging criteria: " + criteria.getCriteriaType().name());
		}

		final List<SearchResult<E>> list = rlist;
		final int count = totalCount;

		return new IPageResult() {

			@Override
			public int getResultCount() {
				return count;
			}

			@Override
			public List getPageList() {
				return list;
			}

		};
	}

	public int executeQuery(String queryName, IQueryParam[] params) {
		final Query q = getEntityManager().createNamedQuery(queryName);
		if(params != null) {
			for(final IQueryParam param : params) {
				q.setParameter(param.getPropertyName(), param.getValue());
			}
		}
		return q.executeUpdate();
	}

}