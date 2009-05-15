package com.tll.dao.mock;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang.math.NumberRange;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.google.inject.Inject;
import com.tll.criteria.Comparator;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.DBType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.ICriterion;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.SortColumnBeanComparator;
import com.tll.dao.Sorting;
import com.tll.model.EntityGraph;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.IScalar;
import com.tll.model.ITimeStampEntity;
import com.tll.model.Scalar;
import com.tll.model.key.BusinessKeyUtil;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.NonUniqueBusinessKeyException;
import com.tll.model.key.PrimaryKey;
import com.tll.util.CommonUtil;
import com.tll.util.DateRange;

/**
 * EntityDao - MOCK dao impl.
 * @author jpk
 */
public class EntityDao implements IEntityDao {

	// private static final Log log = LogFactory.getLog(EntityDao.class);

	private static enum CompareResult {
		GT,
		GTE,
		LT,
		LTE,
		EQUAL,
		UNCOMPARABLE;
	}

	/**
	 * Compares two objects.
	 * @param o1
	 * @param o2
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	private static CompareResult compare(final Object o1, final Object o2) {
		if(o1 instanceof Comparable) {
			try {
				final int i = ((Comparable<Object>) o1).compareTo(o2);
				if(i > 0)
					return CompareResult.GT;
				else if(i < 0) return CompareResult.LT;
				return CompareResult.EQUAL;
			}
			catch(final ClassCastException e) {
			}
		}
		return CompareResult.UNCOMPARABLE;
	}

	/**
	 * Compares two objects returning true/false based on the given expected
	 * comparison argument.
	 * @param o1
	 * @param o2
	 * @param expectedCompareResult
	 * @return true/false
	 */
	private static final boolean compare(final Object o1, final Object o2, final CompareResult expectedCompareResult) {
		return compare(o1, o2) == expectedCompareResult;
	}

	/**
	 * Compares two Objects given a native Comparator.
	 * @param criterionValue The criterion value
	 * @param checkValue The object checked against the criterion value
	 * @param cmp The Comparator May NOT be <code>null</code>
	 * @param caseSensitive Apply case sensitivity in the comparison?
	 * @return <code>true</code> if the check value satisfies the comparator
	 *         clause, <code>false</code> otherwise.
	 * @throws IllegalArgumentException When the <code>cmp</code> argument is
	 *         <code>null</code>.
	 */
	private static boolean compare(final Object criterionValue, final Object checkValue, final Comparator cmp,
			final boolean caseSensitive) throws IllegalArgumentException {
		if(cmp == null) {
			throw new IllegalArgumentException("The comparator argument may not be null");
		}
		switch(cmp) {
			case EQUALS:
				return CommonUtil.equals(criterionValue, checkValue, caseSensitive);
			case NOT_EQUALS:
				return !CommonUtil.equals(criterionValue, checkValue, caseSensitive);
			case GREATER_THAN:
				return compare(criterionValue, checkValue, CompareResult.GT);
			case GREATER_THAN_EQUALS:
				return compare(criterionValue, checkValue, CompareResult.GTE);
			case LESS_THAN:
				return compare(criterionValue, checkValue, CompareResult.LT);
			case LESS_THAN_EQUALS:
				return compare(criterionValue, checkValue, CompareResult.LTE);
			case LIKE:
				// TODO impl
				throw new UnsupportedOperationException("LIKE comparisons are not supported yet");
			case CONTAINS:
				if(criterionValue instanceof String == false || checkValue instanceof String == false) {
					return false;
				}
				return ((String) checkValue).indexOf((String) criterionValue) >= 0;
			case STARTS_WITH:
				if(criterionValue instanceof String == false || checkValue instanceof String == false) {
					return false;
				}
				return ((String) criterionValue).startsWith((String) checkValue);
			case ENDS_WITH:
				if(criterionValue instanceof String == false || checkValue instanceof String == false) {
					return false;
				}
				return ((String) criterionValue).endsWith((String) checkValue);
			case BETWEEN: {
				if(criterionValue instanceof NumberRange) {
					if(checkValue instanceof Number == false) {
						return false;
					}
					final Number number = (Number) checkValue;
					final NumberRange range = (NumberRange) criterionValue;
					return range.containsNumber(number);
				}
				else if(criterionValue instanceof DateRange) {
					if(checkValue instanceof Date == false) {
						return false;
					}
					final Date date = (Date) checkValue;
					final DateRange range = (DateRange) criterionValue;
					return range.includes(date);
				}
				return false;
			}
			case IS: {
				if(criterionValue instanceof DBType == false) {
					return false;
				}
				final DBType dbType = (DBType) criterionValue;
				if(dbType == DBType.NULL) {
					return checkValue != null;
				}
				return checkValue == null;
			}
			case IN: {
				if(criterionValue.getClass().isArray()) {
					return ObjectUtils.containsElement((Object[]) criterionValue, checkValue);
				}
				else if(criterionValue instanceof Collection) {
					return ((Collection<?>) criterionValue).contains(checkValue);
				}
				else if(criterionValue instanceof String) {
					// assume comma-delimited string
					final Object[] arr =
						ObjectUtils.toObjectArray(StringUtils.commaDelimitedListToStringArray((String) checkValue));
					return ObjectUtils.containsElement(arr, checkValue);
				}
				return false;
			}
			default:
				throw new IllegalStateException("Unhandled Comparator case: " + cmp.toString());
		}
	}

	/**
	 * The entity graph.
	 */
	private final EntityGraph entityGraph;

	/**
	 * Constructor
	 * @param entityGraph The entity provider
	 */
	@Inject
	public EntityDao(EntityGraph entityGraph) {
		super();
		if(entityGraph == null) {
			throw new IllegalArgumentException("An entity provider must be specified.");
		}
		this.entityGraph = entityGraph;
	}

	/**
	 * Used for testing purposes.
	 * @return The entity graph.
	 */
	public EntityGraph getEntityGraph() {
		return entityGraph;
	}

	@Override
	public int executeQuery(String queryName, IQueryParam[] params) {
		// TODO impl ?
		return 0;
	}

	/**
	 * Mock dao impls should override as necessary to best provide the satisfying
	 * entities.
	 * @param <E>
	 * @param criteria
	 * @return List of entities that best satisfies the query ref
	 */
	private <E extends IEntity> List<E> processQuery(final ICriteria<E> criteria) {
		// base impl: return all
		return loadAll(criteria.getEntityClass());
	}

	public <E extends IEntity> List<E> findEntities(final ICriteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("A criteria type must be specified.");
		}

		List<E> list;

		// is it a query ref?
		if(criteria.getCriteriaType().isQuery()) {
			list = processQuery(criteria);
		}
		else {
			if(!criteria.isSet()) {
				list = loadAll(criteria.getEntityClass());
			}
			else {
				list = new ArrayList<E>();
				final CriterionGroup pg = criteria.getPrimaryGroup();
				if(pg.size() > 0) {
					for(final ICriterion ctn : pg) {
						if(ctn.isGroup()) {
							throw new InvalidCriteriaException(
							"Mock dao implementations are only able to handle a single criterion entityGroup.");
						}
						if(ctn.isSet()) {
							final Collection<E> clc = entityGraph.getEntitiesByType(criteria.getEntityClass());
							if(clc != null) {
								for(final E e : clc) {
									final String pn = ctn.getPropertyName();
									Object pv = null;
									final BeanWrapper bw = new BeanWrapperImpl(e);

									try {
										pv = bw.getPropertyValue(pn);
									}
									catch(final RuntimeException re) {
										throw new InvalidCriteriaException("Invalid " + criteria.getEntityClass() + " property: " + pn);
									}
									if(compare(ctn.getValue(), pv, ctn.getComparator(), ctn.isCaseSensitive())) {
										list.add(e);
									}
								}
							}
						}
					}
				}
			}
		}
		assert list != null;
		if(sorting != null) {
			Collections.sort(list, new SortColumnBeanComparator<E>(sorting.getPrimarySortColumn()));
		}

		return list;
	}

	public <E extends IEntity> List<SearchResult<?>> find(final ICriteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		if(criteria == null) {
			throw new InvalidCriteriaException("No criteria specified.");
		}
		if(criteria.getCriteriaType() == null) {
			throw new InvalidCriteriaException("A criteria type must be specified.");
		}
		final List<E> list = findEntities(criteria, sorting);

		// transform list
		return transformEntityList(list, criteria.getCriteriaType().isScalar());
	}

	private <E extends IEntity> IScalar scalarize(final E entity) {
		final BeanWrapper bw = new BeanWrapperImpl(entity);
		final Map<String, Object> map = new LinkedHashMap<String, Object>();
		for(final PropertyDescriptor pd : bw.getPropertyDescriptors()) {
			final String pname = pd.getName();
			if(bw.isWritableProperty(pname)) {
				map.put(pname, bw.getPropertyValue(pname));
			}
		}
		return new Scalar(entity.entityClass(), map);
	}

	private <E extends IEntity> List<SearchResult<?>> transformEntityList(final List<E> entityList, final boolean isScalar) {
		final List<SearchResult<?>> slist = new ArrayList<SearchResult<?>>(entityList.size());
		for(final E e : entityList) {
			if(isScalar) {
				slist.add(new SearchResult<IScalar>(scalarize(e)));
			}
			else {
				slist.add(new SearchResult<E>(e));
			}
		}
		return slist;
	}

	public <E extends IEntity> List<E> findByIds(Class<E> entityType, final List<Integer> ids, Sorting sorting) {
		final List<E> list = new ArrayList<E>();
		final Collection<E> clc = entityGraph.getEntitiesByType(entityType);
		if(clc != null) {
			for(final E e : clc) {
				for(final Integer id : ids) {
					if(id.equals(e.getId())) {
						list.add(e);
					}
				}
			}
			if(sorting != null && list.size() > 1) {
				Collections.sort(list, new SortColumnBeanComparator<E>(sorting.getPrimarySortColumn()));
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> E findEntity(final ICriteria<E> criteria) throws InvalidCriteriaException {
		final List<SearchResult<?>> list = find(criteria, null);
		if(list == null || list.size() < 1) {
			throw new EntityNotFoundException("No matching entity found.");
		}
		else if(list.size() > 1) {
			throw new NonUniqueResultException("More than one matching entity found.");
		}
		assert list.size() == 1;
		return (E) list.get(0).getElement();
	}

	public <E extends IEntity> E load(final IBusinessKey<E> key) {
		if(key == null || !key.isSet()) {
			throw new IllegalArgumentException("Empty or unset business key");
		}
		final Collection<E> clc = entityGraph.getEntitiesByType(key.getType());
		if(clc != null) {
			for(final E e : clc) {
				try {
					if(BusinessKeyUtil.equals(e, key)) {
						return e;
					}
				}
				catch(final RuntimeException e1) {
					throw new PersistenceException(e1.getMessage(), e1);
				}
			}
		}
		throw new EntityNotFoundException(key.descriptor() + " not found.");
	}

	public <E extends IEntity> E load(final PrimaryKey<E> key) {
		final E e = entityGraph.getEntity(key);
		if(e == null) {
			throw new EntityNotFoundException(key.descriptor() + " not found.");
		}
		return e;
	}

	public <N extends INamedEntity> N load(final NameKey<N> key) {
		if(key == null || !key.isSet()) {
			throw new IllegalArgumentException("Empty or unset name key");
		}
		final Collection<N> clc = entityGraph.getEntitiesByType(key.getType());
		N rslt = null;
		if(clc != null) {
			for(final N e : clc) {
				String name;
				final BeanWrapper bw = new BeanWrapperImpl(e);
				try {
					final Object o = bw.getPropertyValue(key.getNameProperty());
					name = (String) o;
				}
				catch(final RuntimeException re) {
					break;
				}
				if(key.getName().equals(name)) {
					if(rslt == null) {
						rslt = e;
					}
					else {
						throw new NonUniqueResultException("More than one matching entity found.");
					}
				}
			}
		}
		if(rslt == null) {
			throw new EntityNotFoundException(key.descriptor() + " not found.");
		}
		return rslt;
	}

	public <E extends IEntity> List<E> loadAll(Class<E> entityType) {
		final List<E> list = new ArrayList<E>();
		final Collection<E> clc = entityGraph.getEntitiesByType(entityType);
		if(clc != null) list.addAll(clc);
		return list;
	}

	public <E extends IEntity> E persist(final E entity) {

		// validate the version
		if(!entityGraph.contains(new PrimaryKey<E>(entity))) {
			if(entity.getVersion() != null) {
				throw new PersistenceException("Attempt to add non-existant yet versioned entity");
			}
		}
		else {
			// remove old
			entityGraph.removeEntity(entity);
		}

		// attempt to persist
		try {
			entityGraph.setEntity(entity);
		}
		catch(final IllegalStateException e) {
			throw new EntityExistsException(entity.descriptor() + " already exists.");
		}
		catch(final NonUniqueBusinessKeyException e) {
			throw new EntityExistsException("Non-unique entity " + entity.descriptor() + ": " + e.getMessage(), e);
		}

		// set date created/modified
		if(entity instanceof ITimeStampEntity) {
			final Date now = new Date();
			if(entity.isNew()) {
				((ITimeStampEntity) entity).setDateCreated(now);
			}
			((ITimeStampEntity) entity).setDateModified(now);
		}

		// increment version
		Integer version = entity.getVersion();
		if(version == null) {
			version = new Integer(0);
		}
		else {
			version++;
		}
		entity.setVersion(version);

		return entity;
	}

	public <E extends IEntity> Collection<E> persistAll(final Collection<E> entities) {
		for(final E e : entities) {
			persist(e);
		}
		return entities;
	}

	public <E extends IEntity> void purge(final E entity) {
		entityGraph.removeEntity(entity);
	}

	public <E extends IEntity> void purgeAll(final Collection<E> entities) {
		if(entities == null || entities.size() < 1) return;
		for(final E e : entities) {
			purge(e);
		}
	}

	public <E extends IEntity> List<E> getEntitiesFromIds(final Class<E> entityClass, final Collection<Integer> ids,
			final Sorting sorting) {
		final List<E> list = new ArrayList<E>();
		final Collection<E> clc = entityGraph.getEntitiesByType(entityClass);
		if(clc != null) {
			for(final E e : clc) {
				for(final Integer id : ids) {
					if(e.getId().equals(id)) {
						list.add(e);
					}
				}
			}
			if(sorting != null) {
				Collections.sort(list, new SortColumnBeanComparator<E>(sorting.getPrimarySortColumn()));
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public <E extends IEntity> List<Integer> getIds(final ICriteria<E> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		final List<SearchResult<?>> list = find(criteria, sorting);
		if(list == null) {
			return null;
		}
		final List<Integer> idlist = new ArrayList<Integer>();
		for(final SearchResult<?> e : list) {
			idlist.add(((E) e.getElement()).getId());
		}
		return idlist;
	}

	public <E extends IEntity> IPageResult<SearchResult<?>> getPage(final ICriteria<E> criteria, Sorting sorting,
			final int offset, final int pageSize) throws InvalidCriteriaException {
		List<SearchResult<?>> elist = find(criteria, sorting);
		if(elist == null) {
			elist = new ArrayList<SearchResult<?>>();
		}
		final int size = elist.size();
		if(size >= 1) {
			int fi = offset;
			int li = fi + pageSize;
			if(fi > size - 1) {
				fi = size - 1;
			}
			if(li > size - 1) {
				li = size; // NOTE: exclusive index
			}
			elist = elist.subList(fi, li);
		}
		final List<SearchResult<?>> subList = elist;
		return new IPageResult<SearchResult<?>>() {

			@Override
			public List<SearchResult<?>> getPageList() {
				return subList;
			}

			@Override
			public int getResultCount() {
				return size;
			}
		};
	}
}
