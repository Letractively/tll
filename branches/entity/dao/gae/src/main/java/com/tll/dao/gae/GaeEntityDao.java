/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.dao.gae;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.apache.commons.lang.math.NumberRange;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.JdoCallback;
import org.springframework.orm.jdo.JdoObjectRetrievalFailureException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.google.inject.Inject;
import com.tll.criteria.Criteria;
import com.tll.criteria.Criterion;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.ICriterion;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.ISelectNamedQueryDef;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityExistsException;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.NonUniqueResultException;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.model.NameKey;
import com.tll.model.bk.IBusinessKey;
import com.tll.util.DateRange;

/**
 * GaeEntityDao
 * @author jpk
 */
public class GaeEntityDao extends JdoDaoSupport implements IEntityDao {

	private static final String Q_AND = " && ";

	private static final String Q_OR = " || ";

	/**
	 * QueryExec - Encapsulates the needed assets for executing a JDO
	 * {@link Query}.
	 * @author jpk
	 */
	static final class QueryExec {

		final Query q;
		final HashMap<String, Object> params;

		/**
		 * Constructor
		 * @param q
		 * @param params
		 */
		public QueryExec(Query q, HashMap<String, Object> params) {
			super();
			this.q = q;
			this.params = params;
		}
	} // QueryExec

	/**
	 * Transforms an arbitrary collection to a list of wrapping
	 * {@link SearchResult} instances.
	 * @param clc The collection
	 * @return Newly created list {@link SearchResult} instances where each is a
	 *         wrapper around the corres. element in the given collection.
	 */
	private static List<SearchResult> toSearchResultList(Collection<?> clc) {
		if(clc == null) return null;
		final ArrayList<SearchResult> rlist = new ArrayList<SearchResult>(clc.size());
		for(final Object o : clc) {
			rlist.add(new SearchResult(o));
		}
		return rlist;
	}

	/**
	 * Constructor
	 * @param pmf
	 */
	@Inject
	public GaeEntityDao(PersistenceManagerFactory pmf) {
		super();
		setPersistenceManagerFactory(pmf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E extends IEntity> E load(Class<E> entityType, Object key) {
		if(logger.isDebugEnabled()) logger.debug("Loading by PK: " + key);
		try {
			final E e = (E) getJdoTemplate().getObjectById(entityType, key);
			if(logger.isDebugEnabled()) logger.debug(e + " loaded by PK");
			return e;
		}
		catch(final JdoObjectRetrievalFailureException ex) {
			throw new EntityNotFoundException("Entity of primary key: '" + key + "' not found.", ex);
		}
	}

	@Override
	public <E extends IEntity> E load(IBusinessKey<E> key) {
		if(logger.isDebugEnabled()) logger.debug("Loading by BK: " + key);
		try {
			final E e = findEntity(new Criteria<E>(key.getType()));
			if(logger.isDebugEnabled()) logger.debug(e + " loaded by BK");
			return e;
		}
		catch(final InvalidCriteriaException e) {
			throw new ObjectRetrievalFailureException(key.getType(), key);
		}
	}

	@Override
	public <E extends IEntity> E load(NameKey<E> nameKey) {
		if(logger.isDebugEnabled()) logger.debug("Loading by NameKey: " + nameKey);
		try {
			final Criteria<E> nc = new Criteria<E>(nameKey.getType());
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			final E e = findEntity(nc);
			if(logger.isDebugEnabled()) logger.debug(e + " loaded by NameKey");
			return e;
		}
		catch(final InvalidCriteriaException e) {
			throw new ObjectRetrievalFailureException(nameKey.getType(), nameKey);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> loadAll(Class<E> entityType) {
		return (List<E>) getJdoTemplate().find(entityType);
	}

	@Override
	public <E extends IEntity> E persist(E entity) {
		return persistInternal(entity, true);
	}

	@Override
	public <E extends IEntity> Collection<E> persistAll(Collection<E> entities) {
		if(entities == null) return null;
		final Collection<E> merged = new HashSet<E>(entities.size());
		for(final E e : entities) {
			merged.add(persistInternal(e, false));
		}
		try {
			getJdoTemplate().flush();
		}
		catch(final DataIntegrityViolationException e) {
			throw new EntityExistsException("Encountered not-unique entity: " + e.getMessage(), e);
		}
		return merged;
	}

	@SuppressWarnings("unchecked")
	private <E extends IEntity> E persistInternal(E entity, boolean flush) {
		if(logger.isDebugEnabled()) logger.debug("Persisting '" + entity + "'..");
		try {
			final E saved = (E) getJdoTemplate().makePersistent(entity);
			if(flush) {
				if(logger.isDebugEnabled()) logger.debug("Flushing PersistenceManager after persist");
				getJdoTemplate().flush();
			}
			if(logger.isDebugEnabled()) logger.debug(saved + " persisted");
			return saved;
		}
		catch(final DataIntegrityViolationException e) {
			throw new EntityExistsException("Entity '" + entity.descriptor() + "' is not-unique.", e);
		}
	}

	@Override
	public <E extends IEntity> void purge(E entity) throws EntityNotFoundException, DataAccessException {
		purgeInternal(entity, true);
	}

	@Override
	public <E extends IEntity> void purge(Class<E> entityType, Object key) throws EntityNotFoundException, DataAccessException {
		final E e = load(entityType, key);
		purge(e);
	}

	@Override
	public <E extends IEntity> void purgeAll(Collection<E> entities) {
		if(entities == null) return;
		for(final E e : entities) {
			purgeInternal(e, false);
		}
		try {
			getJdoTemplate().flush();
		}
		catch(final JdoObjectRetrievalFailureException e) {
			throw new EntityNotFoundException("Entity not found during batch purge: " + e.getMessage(), e);
		}
	}

	private <E extends IEntity> void purgeInternal(final E entity, boolean flush) {
		if(logger.isDebugEnabled()) logger.debug("Purging " + entity);
		try {
			getJdoTemplate().deletePersistent(entity);
			if(flush) {
				if(logger.isDebugEnabled()) logger.debug("Flushing PersistenceManager after purge");
				getJdoTemplate().flush();
			}
		}
		catch(final JdoObjectRetrievalFailureException e) {
			throw new EntityNotFoundException(entity.descriptor() + " not found.", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<E> findEntities(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		final List<E> rval = (List<E>) executeQueryClc(criteria2Query(criteria, sorting, null));
		return rval;
	}

	@Override
	public <E extends IEntity> E findEntity(Criteria<E> criteria) throws InvalidCriteriaException,
	EntityNotFoundException, NonUniqueResultException {
		final List<E> list = findEntities(criteria, null);
		if(list == null || list.size() < 1) {
			throw new EntityNotFoundException("No matching entity found.", null);
		}
		else if(list.size() > 1) {
			throw new NonUniqueResultException("More than one matching entity found.");
		}
		assert list.size() == 1;
		return list.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<SearchResult> find(Criteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		return (List<SearchResult>) executeQueryClc(criteria2Query(criteria, sorting, null));
	}

	/**
	 * Transforms a native {@link Criteria} instance to a JDO {@link Query}
	 * instance wrapped in a {@link QueryExec} instance in order to provide query
	 * parameters and declarations.
	 * @param <E>
	 * @param criteria
	 * @param sorting
	 * @param queryNameOverride Optional query name that overrides the given
	 *        criteria's query name property
	 * @return A new {@link QueryExec} instance.
	 * @throws InvalidCriteriaException When the criteria is found to be invalid
	 */
	private <E extends IEntity> QueryExec criteria2Query(final Criteria<E> criteria, final Sorting sorting,
			String queryNameOverride) throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("A criteria type must be specified.");
		}
		if(logger.isDebugEnabled()) logger.debug("Transforming criteria: " + criteria);

		Query q = null;
		final HashMap<String, Object> params = new HashMap<String, Object>();

		if(criteria.getCriteriaType().isQuery()) {
			// named query?
			final Class<E> entityClass = criteria.getEntityClass();
			final ISelectNamedQueryDef qd = criteria.getNamedQueryDefinition();
			final String qname = queryNameOverride == null ? qd.getQueryName() : queryNameOverride;
			final List<IQueryParam> qps = criteria.getQueryParams();
			if(qps != null) {
				for(final IQueryParam qp : criteria.getQueryParams()) {
					params.put(qp.getPropertyName(), qp.getValue());
				}
			}
			q = getPersistenceManager().newNamedQuery(entityClass, qname);
		}
		else {
			// dynamic query
			q = getPersistenceManager().newQuery(criteria.getEntityClass());
			final String filter = toFilter(criteria, params);
			q.setFilter(filter);
		}

		if(sorting != null) {
			q.setOrdering(sorting.getJdoOrderingClause());
		}

		if(logger.isDebugEnabled()) logger.debug("'" + criteria + "' transformed to JDO query: '" + q + "'");
		return new QueryExec(q, params);
	}

	/**
	 * Executes a query whose results are assumed to be a collection.
	 * @param <E>
	 * @param qe a {@link QueryExec} instance
	 * @return newly created list of detached query result elements
	 */
	private <E extends IEntity> List<?> executeQueryClc(final QueryExec qe) {
		final List<?> rval = (List<?>) getJdoTemplate().executeFind(new JdoCallback() {

			@Override
			public Object doInJdo(PersistenceManager pm) throws JDOException {
				try {
					final List<?> list = (List<?>) qe.q.executeWithMap(qe.params);
					return getJdoTemplate().detachCopyAll(list);
				}
				finally {
					qe.q.closeAll();
				}
			}
		});
		return rval;
	}

	/**
	 * Executes a query whose results are assumed to be a single object.
	 * @param <E>
	 * @param qe a {@link QueryExec} instance
	 * @return the detached single query result object
	 */
	private <E extends IEntity> Object executeQuerySingle(final QueryExec qe) {
		final Object rval = getJdoTemplate().executeFind(new JdoCallback() {

			@Override
			public Object doInJdo(PersistenceManager pm) throws JDOException {
				try {
					final Object obj = qe.q.executeWithMap(qe.params);
					return getJdoTemplate().detachCopy(obj);
				}
				finally {
					qe.q.closeAll();
				}
			}
		});
		return rval;
	}

	private <E extends IEntity> String toFilter(Criteria<E> criteria, final HashMap<String, Object> params) {
		final StringBuilder sb = new StringBuilder();
		if(criteria.isSet()) {
			final CriterionGroup pg = criteria.getPrimaryGroup();
			for(final ICriterion crit : pg) {
				toFilter(sb, crit, Boolean.valueOf(pg.isConjunction()), params);
			}
		}
		if(sb.indexOf(Q_AND) == 0 || sb.indexOf(Q_OR) == 0) sb.delete(0, 4);
		return sb.toString();
	}

	/**
	 * @param <E>
	 * @param sb the string buffer which is appended with the translated criterion
	 * @param ctn the criterion
	 * @param conjunction <code>true</code> ==> (... 'AND' ...),
	 *        <code>false</code> ==> (... 'OR' ..), <code>null</code> ==> 'AND'
	 * @param params map of parameter declarations
	 */
	private <E extends IEntity> void toFilter(StringBuilder sb, ICriterion ctn, Boolean conjunction,
			final HashMap<String, Object> params) {
		if(!ctn.isSet()) return;
		if(ctn.isGroup()) {
			final CriterionGroup g = (CriterionGroup) ctn;
			sb.append(" (");
			for(final ICriterion c : g) {
				if(sb.indexOf(Q_AND) == 0 || sb.indexOf(Q_OR) == 0) sb.delete(0, 4);
				toFilter(sb, c, conjunction, params);
			}
			sb.append(')');
			return;
		}

		sb.append((conjunction == null || conjunction == Boolean.TRUE) ? Q_AND : Q_OR);

		// single (non-group) criterion
		assert ctn instanceof Criterion;
		final Criterion crtn = (Criterion) ctn;
		final String name = crtn.getField();
		final Object value = crtn.getValue();
		String param;
		switch(crtn.getComparator()) {
		case BETWEEN: {
			Object min, max;
			if(value instanceof NumberRange) {
				final NumberRange range = (NumberRange) value;
				min = range.getMinimumNumber();
				max = range.getMaximumNumber();
			}
			else if(value instanceof DateRange) {
				final DateRange range = (DateRange) value;
				min = range.getStartDate();
				max = range.getEndDate();
			}
			else {
				// presume an object array
				final Object[] oarr = (Object[]) value;
				min = oarr[0];
				max = oarr[1];
			}
			if(min != null && max != null) {
				sb.append(name);
				sb.append(" >= :");
				param = "p_" + Math.abs(min.hashCode());
				sb.append(param);
				params.put(param, min);
				sb.append(" && ");
				sb.append(name);
				sb.append(" <= :");
				param = "p_" + Math.abs(max.hashCode());
				sb.append(param);
				params.put(param, max);
			}
			break;
		}
		case CONTAINS:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(".contains(\"");
				sb.append(value);
				sb.append("\")");
			}
			break;
		case ENDS_WITH:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(".endsWith(\"");
				sb.append(value);
				sb.append("\")");
			}
			break;
		case EQUALS:
			if(value instanceof String) {
				sb.append(name);
				sb.append(" == \"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(name);
				sb.append(" == :");
				param = "p_" + Math.abs(value.hashCode());
				sb.append(param);
				params.put(param, value);
			}
			break;
		case GREATER_THAN:
			sb.append(name);
			sb.append(" > :");
			param = "p_" + Math.abs(value.hashCode());
			sb.append(param);
			params.put(param, value);
			break;
		case GREATER_THAN_EQUALS:
			sb.append(name);
			sb.append(" >= :");
			param = "p_" + Math.abs(value.hashCode());
			sb.append(param);
			params.put(param, value);
			break;
		case IN:
			if(value instanceof Collection<?>) {
				param = "p_" + Math.abs(ctn.hashCode());
				sb.append(":");
				sb.append(param);
				sb.append(".contains(");
				sb.append(name);
				sb.append(")");
				params.put(param, value);
			}
			break;
		case IS:
			// not supported
			break;
		case LESS_THAN:
			sb.append(name);
			sb.append(" < :");
			param = "p_" + Math.abs(value.hashCode());
			sb.append(param);
			params.put(param, value);
			break;
		case LESS_THAN_EQUALS:
			sb.append(name);
			sb.append(" <= :");
			param = "p_" + Math.abs(value.hashCode());
			sb.append(param);
			params.put(param, value);
			break;
		case LIKE:
			// currently not supported
			// TODO implement
			break;
		case NOT_EQUALS:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(" != \"");
				sb.append(value);
				sb.append("\"");
			}
			else {
				sb.append(name);
				sb.append(" == :");
				param = "p_" + Math.abs(value.hashCode());
				sb.append(param);
				params.put(param, value);
			}
			break;
		case STARTS_WITH:
			if(value instanceof String) {
				// NOTE: case sensitive clause
				sb.append(name);
				sb.append(".startsWith(\"");
				sb.append(value);
				sb.append("\"");
			}
			break;
		}
		if(sb.length() < 1) {
			throw new UnsupportedOperationException("Un-supported criterion state: " + crtn);
		}
		if(logger.isDebugEnabled())
			logger.debug("Criterion (" + ctn + ") translated to JDO query fragment: " + sb.toString());
	}

	@Override
	public <E extends IEntity> List<E> findByPrimaryKeys(Class<E> entityType, final Collection<?> ids, Sorting sorting) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends IEntity> List<?> getPrimaryKeys(Criteria<E> criteria, Sorting sorting) throws InvalidCriteriaException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <E extends IEntity> IPageResult<SearchResult> getPage(Criteria<E> criteria, Sorting sorting, int offset,
			int pageSize) throws InvalidCriteriaException {
		if(criteria == null || criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("Empty or invalid criteria instance.");
		}
		List<SearchResult> rlist = null;
		int totalCount = -1;
		switch(criteria.getCriteriaType()) {

		case ENTITY: {
			if(sorting == null) {
				// sorting is necessary in the case of IPage results due to
				// necessary in memory manipulation to provide a distinct list of results
				throw new InvalidCriteriaException("Paged results require a sorting directive");
			}

			// count query
			QueryExec qexec = null;
			qexec = criteria2Query(criteria, null, null);
			qexec.q.setResult("count(id)");
			final Long rowCount = (Long) qexec.q.executeWithMap(qexec.params);
			assert rowCount != null && rowCount.longValue() <= Integer.MAX_VALUE;
			totalCount = rowCount.intValue();

			// data (page) query
			qexec = criteria2Query(criteria, sorting, null);
			qexec.q.setRange(offset, offset + pageSize);
			@SuppressWarnings("unchecked")
			final Collection<Object> rclc = (Collection<Object>) qexec.q.executeWithMap(qexec.params);
			rlist = toSearchResultList(rclc);
			break;
		}

		case ENTITY_NAMED_QUERY: {
			// get the count by convention looking for a couter-part named query
			// with same name and additional suffix of .count
			final ISelectNamedQueryDef snq = criteria.getNamedQueryDefinition();
			if(!snq.isSupportsPaging()) {
				throw new InvalidCriteriaException(snq.getQueryName() + " query does not support paging.");
			}
			// count query
			QueryExec qe = criteria2Query(criteria, sorting, snq.getQueryName() + ".count");
			qe.q.setResultClass(Long.class);
			final Long count = (Long) executeQuerySingle(qe);
			assert count != null;
			totalCount = count.intValue();
			// data (page) query
			qe = criteria2Query(criteria, sorting, null);
			@SuppressWarnings("unchecked")
			final Collection<Object> rclc = (Collection<Object>) executeQueryClc(qe);
			rlist = toSearchResultList(rclc);
			break;
		}
		case SCALAR_NAMED_QUERY:
			throw new UnsupportedOperationException("Scalar named queries are not supported in JDO DAO.");
		}

		if(rlist == null) {
			throw new InvalidCriteriaException("Invalid paging criteria: " + criteria.getCriteriaType().name());
		}

		final List<SearchResult> list = rlist;
		final int count = totalCount;

		return new IPageResult<SearchResult>() {

			@Override
			public int getResultCount() {
				return count;
			}

			@Override
			public List<SearchResult> getPageList() {
				return list;
			}

		};
	}

	@Override
	public int executeQuery(String queryName, IQueryParam[] params) throws DataAccessException {
		throw new UnsupportedOperationException();
	}
}
