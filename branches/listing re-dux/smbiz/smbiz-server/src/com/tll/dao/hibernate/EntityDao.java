package com.tll.dao.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
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
import org.hibernate.ejb.QueryImpl;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.transform.ResultTransformer;
import org.springframework.util.StringUtils;

import com.google.inject.Provider;
import com.tll.criteria.Comparator;
import com.tll.criteria.CriteriaType;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.IComparatorTranslator;
import com.tll.criteria.ICriteria;
import com.tll.criteria.ICriterion;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.criteria.SelectNamedQuery;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.IEntityDao;
import com.tll.listhandler.IPageResult;
import com.tll.listhandler.SearchResult;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.SortDir;
import com.tll.listhandler.Sorting;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.CollectionUtil;

/**
 * Base dao class for hibernate dao impls.
 * @author jpk
 */
public abstract class EntityDao<E extends IEntity> extends HibernateJpaSupport implements IEntityDao<E> {

	/**
	 * Used for transforming entity results into a native friendly handle.
	 */
	protected static final ResultTransformer ENTITY_RESULT_TRANSFORMER = new EntitySearchResultTransformer();

	/**
	 * String representing the beginning of an order by clause in an SQL string.
	 * Used internally to manually modify sql query strings in order to support
	 * dynamic sorting.
	 */
	private final String ORDER_BY_TOKEN = "order by ";

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
	 * @param comparatorTranslator
	 */
	// @Inject
	public EntityDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr);
		this.dbDialectHandler = dbDialectHandler;
		this.comparatorTranslator = comparatorTranslator;
	}

	public abstract Class<E> getEntityClass();

	/**
	 * @param <T>
	 * @param maybeProxy
	 * @param baseClass
	 * @return
	 * @throws ClassCastException
	 */
	private static <T> T deproxy(Object maybeProxy, Class<T> baseClass) throws ClassCastException {
		if(maybeProxy instanceof HibernateProxy) {
			return baseClass.cast(((HibernateProxy) maybeProxy).getHibernateLazyInitializer().getImplementation());
		}
		return baseClass.cast(maybeProxy);
	}

	/**
	 * Ensures the given entity class is the same is this dao's entity class or an
	 * extended class from it.
	 * @param entityClass
	 * @throws IllegalArgumentException
	 */
	protected final void ensureTypeCompatible(Class<? extends IEntity> entityClass) throws IllegalArgumentException {
		if(!getEntityClass().isAssignableFrom(entityClass)) {
			throw new IllegalArgumentException("Incompatible type: " + entityClass.toString());
		}
	}

	public final E load(PrimaryKey key) {
		ensureTypeCompatible(key.getType());
		final E e = getEntityManager().getReference(getEntityClass(), key.getId());
		return deproxy(e, getEntityClass());
	}

	@SuppressWarnings("unchecked")
	public final E load(BusinessKey key) {
		ensureTypeCompatible(key.getType());
		try {
			return findEntity(new com.tll.criteria.Criteria<E>((Class<? extends E>) key.getType()));
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from business key: " + e.getMessage(), e);
		}
	}

	public final E load(NameKey nameKey) {
		ensureTypeCompatible(nameKey.getType());
		try {
			final com.tll.criteria.Criteria<E> nc = new com.tll.criteria.Criteria<E>(getEntityClass());
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			return findEntity(nc);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<E> loadAll() {
		final DetachedCriteria dc = DetachedCriteria.forClass(getEntityClass());
		final Session session = (Session) getEntityManager().getDelegate();
		final Criteria c = dc.getExecutableCriteria(session);
		return c.list();
	}

	public E persist(E entity) {
		return persistInternal(entity, true);
	}

	protected E persistInternal(E entity, boolean flush) {
		E merged;
		try {
			final EntityManager em = getEntityManager();
			// TODO figure out how to get away with NOT merging!
			merged = em.merge(entity);
			em.persist(merged);
			em.flush();
		}
		catch(final RuntimeException re) {
			throw dbDialectHandler.translate(re);
		}
		return merged;
	}

	public Collection<E> persistAll(Collection<E> entities) {
		if(entities == null) return null;
		final Collection<E> merged = new HashSet<E>(entities.size());
		for(final E e : entities) {
			merged.add(persistInternal(e, false));
		}
		getEntityManager().flush();
		return merged;
	}

	public void purge(E entity) {
		try {
			if(entity.isNew()) {
				getEntityManager().remove(entity);
			}
			else {
				getEntityManager().remove(getEntityManager().merge(entity));
			}
		}
		catch(final RuntimeException re) {
			throw dbDialectHandler.translate(re);
		}
	}

	public void purgeAll(Collection<E> entities) {
		if(CollectionUtil.isEmpty(entities)) {
			return;
		}
		for(final E e : entities) {
			purge(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<E> findEntities(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null || criteria.getCriteriaType().isScalar()) {
			throw new InvalidCriteriaException("A criteria type must be specified and be non-scalar.");
		}
		return (List<E>) processCriteria(criteria, sorting, !criteria.getCriteriaType().isQuery(),
				CriteriaSpecification.DISTINCT_ROOT_ENTITY);
	}

	public E findEntity(ICriteria<? extends E> criteria) throws InvalidCriteriaException {
		final List<E> list = findEntities(criteria, null);
		if(list == null || list.size() != 1) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<SearchResult<E>> find(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
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

	public void clear() {
		getEntityManager().clear();
	}

	public void flush() {
		getEntityManager().flush();
	}

	/**
	 * Process criteria instances providing a distinct list of matching results
	 * whose elements are either {@link SearchResult} or {@link IEntity} instances
	 * depending on the specified {@link CriteriaType}.
	 * @param criteria Assumed non-<code>null</code>.
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @param resultTransformer The result transformer. May NOT be
	 *        <code>null</code>.
	 * @return List of distinct matching results
	 * @throws InvalidCriteriaException When the criteria type is invalid or
	 *         otherwise.
	 */
	@SuppressWarnings("unchecked")
	protected final List<?> processCriteria(ICriteria<? extends E> criteria, Sorting sorting, boolean applySorting,
			ResultTransformer resultTransformer) throws InvalidCriteriaException {
		assert criteria != null && resultTransformer != null;
		if(criteria.getCriteriaType().isQuery()) {
			// presume named query ref
			return findByNamedQuery(criteria, sorting, resultTransformer);
		}
		// translate to hbm criteria
		final Class<? extends E> entityClass =
				criteria.getEntityClass() == null ? getEntityClass() : criteria.getEntityClass();
		final DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		dc.setResultTransformer(resultTransformer);
		applyCriteria(dc, criteria, sorting, applySorting);
		return findByDetatchedCriteria(dc);
	}

	/**
	 * Executes the query as defined by the given detached criteria.
	 * @param dc The detached criteria
	 * @return List of results of unknown type.
	 */
	protected final List<?> findByDetatchedCriteria(DetachedCriteria dc) {
		final Session session = (Session) getEntityManager().getDelegate();
		final Criteria executableCriteria = dc.getExecutableCriteria(session);
		return executableCriteria.list();
	}

	/**
	 * Translates native criteria to a new Query instance.
	 * @param queryName
	 * @param queryParams
	 * @param sorting
	 * @param resultTransformer May be <code>null</code>.
	 * @param cacheable Is this query cacheable?
	 * @return New Query instance
	 * @throws InvalidCriteriaException When no query name is specified in the
	 *         given criteria.
	 */
	private final Query assembleQuery(String queryName, Collection<IQueryParam> queryParams, Sorting sorting,
			ResultTransformer resultTransformer, boolean cacheable) throws InvalidCriteriaException {
		if(queryName == null) {
			throw new InvalidCriteriaException("No query name specified.");
		}
		final EntityManager em = getEntityManager();
		Query q = em.createNamedQuery(queryName);
		org.hibernate.Query hbmQuery = jpa2hbmQuery(q);

		// apply sorting (if specified)
		if(sorting != null) {
			final StringBuffer sb = new StringBuffer(hbmQuery.getQueryString());
			int indx = sb.indexOf(ORDER_BY_TOKEN);
			if(indx >= 0) {
				indx += ORDER_BY_TOKEN.length();
				assert sb.length() - 1 > indx;
				sb.setLength(indx);
			}
			sb.append(sorting.toString());
			q = em.createQuery(sb.toString());
			hbmQuery = jpa2hbmQuery(q);
		}

		// fill the named params (if any)
		final String[] namedParams = hbmQuery.getNamedParameters();
		if(namedParams != null && namedParams.length > 0) {
			// create param map
			if(queryParams == null || queryParams.size() != namedParams.length) {
				throw new InvalidCriteriaException("Empty or invalid number of query parameters for named query: " + queryName);
			}
			for(final String np : namedParams) {
				IQueryParam queryParam = null;
				for(IQueryParam qp : queryParams) {
					if(np.equals(qp.getPropertyName())) {
						queryParam = qp;
						break;
					}
				}
				if(queryParam == null) {
					throw new InvalidCriteriaException("Named parameter: " + np + " is not specified for named query: "
							+ queryName);
				}
				hbmQuery.setParameter(np, queryParam.getValue());
			}
		}

		if(resultTransformer != null) {
			hbmQuery.setResultTransformer(resultTransformer);
		}

		hbmQuery.setCacheable(cacheable);

		return q;
	}

	/**
	 * Obtains the Hibernate query from a JPA query.
	 * @param q The jpa query instance
	 * @return The hibernate query instance.
	 */
	protected final org.hibernate.Query jpa2hbmQuery(Query q) {
		assert q instanceof QueryImpl;
		return ((QueryImpl) q).getHibernateQuery();
	}

	/**
	 * Invokes a named query.
	 * @param criteria
	 * @param resultTransformer May be <code>null</code>.
	 * @return List of either entities or scalars.
	 * @throws InvalidCriteriaException
	 */
	protected final List<?> findByNamedQuery(ICriteria<? extends E> criteria, Sorting sorting,
			ResultTransformer resultTransformer) throws InvalidCriteriaException {
		SelectNamedQuery nq = criteria.getNamedQueryDefinition();
		return assembleQuery(nq.getQueryName(), criteria.getQueryParams(), sorting, resultTransformer, true)
				.getResultList();
	}

	/**
	 * Translates criterion objects to the hibernate criterion objects. This
	 * method takes a <code>DetachedCriteria</code> implementation simply
	 * because there is no common interface for <code>DetachedCriteria</code>
	 * and <code>Criteria</code>. This method may be overridden by subclasses
	 * and thus is not called from processUniqueCriteria(ICriteria).
	 * @param dc the hibernate criteria object
	 * @param criteria Criteria object
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @throws InvalidCriteriaException
	 */
	protected void applyCriteria(DetachedCriteria dc, ICriteria<? extends E> criteria, Sorting sorting,
			boolean applySorting) throws InvalidCriteriaException {
		applyCriteriaStrict(dc, criteria, sorting, applySorting);
	}

	/**
	 * This method is called by processCriteria(ICriteria) such that the behavior
	 * may not be overridden.
	 * @param dc the hibernate criteria object
	 * @param criteria criteria object
	 * @param sorting The optional sorting directive
	 * @param applySorting Apply the sorting?
	 * @throws InvalidCriteriaException
	 * @see #applyCriteria(DetachedCriteria, ICriteria)
	 */
	private void applyCriteriaStrict(DetachedCriteria dc, ICriteria<? extends E> criteria, Sorting sorting,
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

	private void applyCriterion(DetachedCriteria dc, ICriterion ctn, ICriteria<? extends E> criteria, Sorting sorting,
			Junction junction) throws InvalidCriteriaException {
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
		if(Comparator.CONTAINS.equals(c) || Comparator.ENDS_WITH.equals(c) || Comparator.EQUALS.equals(c)
				|| Comparator.LIKE.equals(c) || Comparator.STARTS_WITH.equals(c)) {

			((SimpleExpression) expression).ignoreCase();
		}
	}

	/**
	 * Applies sorting objects to the hibernate criteria object. This method takes
	 * a <code>DetachedCriteria</code> implementation simply because there is no
	 * common interface for <code>DetachedCriteria</code> and
	 * <code>Criteria</code>.
	 * @param dc the hibernate criteria object
	 * @param sorting sorting object
	 */
	protected static void applySorting(DetachedCriteria dc, Sorting sorting) throws InvalidCriteriaException {
		if(sorting == null) {
			return;
		}
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

	/**
	 * Applies aliases if necessary.
	 * @param dc
	 * @param propPath
	 * @throws InvalidCriteriaException
	 */
	private static final void applyAliasIfNecessary(DetachedCriteria dc, String propPath) throws InvalidCriteriaException {
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

	@SuppressWarnings("unchecked")
	public List<E> findByIds(List<Integer> ids, Sorting sorting) {
		com.tll.criteria.Criteria<E> nativeCriteria = new com.tll.criteria.Criteria<E>(getEntityClass());
		nativeCriteria.getPrimaryGroup().addCriterion(IEntity.PK_FIELDNAME, ids, Comparator.IN, false);
		final DetachedCriteria dc = DetachedCriteria.forClass(nativeCriteria.getEntityClass());
		dc.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		try {
			applyCriteriaStrict(dc, nativeCriteria, sorting, true);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException(e.getMessage(), e);
		}
		return (List<E>) findByDetatchedCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getIds(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
		if(criteria.getCriteriaType().isQuery()) {
			throw new InvalidCriteriaException("Ids are not supplied for direct queries!");
		}
		final Class<? extends E> entityClass =
				criteria.getEntityClass() == null ? getEntityClass() : criteria.getEntityClass();
		final DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		dc.setProjection(Projections.property(IEntity.PK_FIELDNAME));
		applyCriteria(dc, criteria, sorting, true);
		return (List<Integer>) findByDetatchedCriteria(dc);

	}

	@SuppressWarnings("unchecked")
	public List<E> getEntitiesFromIds(Class<? extends E> entityClass, Collection<Integer> ids, Sorting sorting) {
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
			throw new PersistenceException("Invalid criteria: " + e.getMessage(), e); // shouldn't
			// happen
		}
		return (List<E>) findByDetatchedCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public IPageResult<SearchResult<E>> getPage(ICriteria<? extends E> criteria, Sorting sorting, int offset, int pageSize)
			throws InvalidCriteriaException {
		assert criteria != null && criteria.getCriteriaType() != null;
		final Class<? extends E> entityClass =
				criteria.getEntityClass() == null ? getEntityClass() : criteria.getEntityClass();
		List<SearchResult<E>> rlist = null;
		int totalCount = -1;
		switch(criteria.getCriteriaType()) {

			case ENTITY: {
				final DetachedCriteria dc = DetachedCriteria.forClass(entityClass);

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
				final SelectNamedQuery namedQueryDef = criteria.getNamedQueryDefinition();
				String queryName = namedQueryDef.getQueryName();
				String countQueryName = namedQueryDef.getCountCounterpartQueryName();
				final Query cq = assembleQuery(countQueryName, null, null, null, false);
				final Long count = (Long) cq.getSingleResult();
				assert count != null;
				totalCount = count.intValue();
				final Query q = assembleQuery(queryName, criteria.getQueryParams(), sorting, ENTITY_RESULT_TRANSFORMER, true);
				rlist = q.setFirstResult(offset).setMaxResults(pageSize).getResultList();
				break;
			}

			case SCALAR_NAMED_QUERY: {
				// get the count by convention looking for a couter-part named query w/
				// same name and additional suffix of .count
				final SelectNamedQuery namedQueryDef = criteria.getNamedQueryDefinition();
				String queryName = namedQueryDef.getQueryName();
				String countQueryName = namedQueryDef.getCountCounterpartQueryName();
				final Query cq = assembleQuery(countQueryName, criteria.getQueryParams(), null, null, false);
				final Long count = (Long) cq.getSingleResult();
				assert count != null;
				totalCount = count.intValue();

				final Query q =
						assembleQuery(queryName, criteria.getQueryParams(), sorting, new ScalarSearchResultTransformer(criteria
								.getEntityClass()), true);
				rlist = q.setFirstResult(offset).setMaxResults(pageSize).getResultList();
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
}
