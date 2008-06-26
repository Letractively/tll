package com.tll.dao.mock;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.math.NumberRange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.tll.criteria.Comparator;
import com.tll.criteria.CriterionGroup;
import com.tll.criteria.DBType;
import com.tll.criteria.ICriteria;
import com.tll.criteria.ICriterion;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.listhandler.IPageResult;
import com.tll.listhandler.SearchResult;
import com.tll.listhandler.SortColumnBeanComparator;
import com.tll.listhandler.Sorting;
import com.tll.model.BusinessKeyFactory;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.IScalar;
import com.tll.model.Scalar;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.util.CommonUtil;
import com.tll.util.DateRange;

/**
 * Base mock dao class.
 * @author jpk
 */
public abstract class EntityDao<E extends IEntity> implements IEntityDao<E> {

	protected static final Log log = LogFactory.getLog(EntityDao.class);

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
	public static boolean compare(final Object criterionValue, final Object checkValue, final Comparator cmp,
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

	protected final Class<E> entityClass;

	/**
	 * The set of mock entities managed by this dao.
	 */
	protected final Set<E> set;

	/**
	 * Constructor
	 * @param entityClass
	 */
	public EntityDao(final Class<E> entityClass, final Set<E> set) {
		super();
		if(entityClass == null) {
			throw new IllegalArgumentException("An entity type must be specified.");
		}
		this.entityClass = entityClass;
		this.set = set == null ? new LinkedHashSet<E>() : set;
	}

	public final Class<E> getEntityClass() {
		return entityClass;
	}

	public final void clear() {
		// no op
	}

	/**
	 * Mock dao impls should override as necessary to best provide the satisfying
	 * entities.
	 * @param criteria
	 * @return List of entities that best satisfies the query ref
	 * @throws InvalidCriteriaException
	 */
	protected List<E> processQuery(final ICriteria<? extends E> criteria) {
		// base impl: return all
		return loadAll();
	}

	public final List<E> findEntities(final ICriteria<? extends E> criteria, Sorting sorting)
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
				list = loadAll();
			}
			else {
				list = new ArrayList<E>();
				final BeanWrapper bw = new BeanWrapperImpl();
				final CriterionGroup pg = criteria.getPrimaryGroup();
				if(pg.size() > 0) {
					for(final ICriterion ctn : pg) {
						if(ctn.isGroup()) {
							throw new InvalidCriteriaException(
									"Mock dao implementations are only able to handle a single criterion entityGroup.");
						}
						if(ctn.isSet()) {
							for(final E e : set) {
								final String pn = ctn.getPropertyName();
								Object pv = null;
								bw.setWrappedInstance(e);

								try {
									pv = bw.getPropertyValue(pn);
								}
								catch(final RuntimeException re) {
									throw new InvalidCriteriaException("Invalid " + EntityUtil.typeName(entityClass) + " property: " + pn);
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

		if(list != null && sorting != null) {
			Collections.sort(list, new SortColumnBeanComparator<E>(sorting.getPrimarySortColumn()));
		}

		return list;
	}

	public final List<SearchResult<E>> find(final ICriteria<? extends E> criteria, Sorting sorting)
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

	protected final IScalar scalarize(final E entity) {
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

	protected final List<SearchResult<E>> transformEntityList(final List<E> entityList, final boolean isScalar) {
		final List<SearchResult<E>> slist = new ArrayList<SearchResult<E>>(entityList.size());
		for(final E e : entityList) {
			if(isScalar) {
				slist.add(new SearchResult<E>(scalarize(e)));
			}
			else {
				slist.add(new SearchResult<E>(e));
			}
		}
		return slist;
	}

	public final List<E> findByIds(final List<Integer> ids, Sorting sorting) {
		final List<E> list = new ArrayList<E>();
		for(final E e : set) {
			for(final Integer id : ids) {
				if(id.equals(e.getId())) {
					list.add(e);
				}
			}
		}
		if(sorting != null && list.size() > 1) {
			Collections.sort(list, new SortColumnBeanComparator<E>(sorting.getPrimarySortColumn()));
		}
		return list;
	}

	public final E findEntity(final ICriteria<? extends E> criteria) throws InvalidCriteriaException {
		final List<SearchResult<E>> list = find(criteria, null);
		if(list != null && list.size() == 1) {
			return list.get(0).getEntity();
		}
		return null;
	}

	public void flush() {
		// no-op
	}

	@SuppressWarnings("unchecked")
	public final E load(final BusinessKey<? extends E> key) {
		for(final E e : set) {
			try {
				final BusinessKey<E>[] bks = BusinessKeyFactory.create(e);
				for(final BusinessKey<E> bk : bks) {
					if(bk.equals(key)) {
						return e;
					}
				}
			}
			catch(final BusinessKeyNotDefinedException e1) {
			}
		}
		throw new EntityNotFoundException(key.descriptor() + " not found.");
	}

	public final E load(final PrimaryKey<? extends E> key) {
		for(final E e : set) {
			if(e.equals(key)) {
				return e;
			}
		}
		throw new EntityNotFoundException(key.descriptor() + " not found.");
	}

	public final List<E> loadAll() {
		final List<E> list = new ArrayList<E>();
		list.addAll(set);
		return list;
	}

	public final E persist(final E entity) {
		if(!set.remove(entity)) {
			// ensure business key unique
			set.add(entity);
			if(!EntityUtil.isBusinessKeyUnique(set)) {
				set.remove(entity);
				throw new EntityExistsException("Unable to persist entity: It is non-unique");
			}
		}
		else {
			set.add(entity);
		}
		Integer version = entity.getVersion();
		if(version == null) {
			version = new Integer(0);
		}
		entity.setVersion(version + 1);
		return entity;
	}

	public final Collection<E> persistAll(final Collection<E> entities) {
		for(final E e : entities) {
			persist(e);
		}
		return entities;
	}

	public final void purge(final E entity) {
		for(final E e : set) {
			if(e.equals(entity)) {
				set.remove(e);
				return;
			}
		}
	}

	public final void purgeAll(final Collection<E> entities) {
		set.clear();
	}

	public final List<E> getEntitiesFromIds(final Class<? extends E> entityClass, final Collection<Integer> ids,
			final Sorting sorting) {
		final List<E> list = new ArrayList<E>();
		for(final E e : set) {
			for(final Integer id : ids) {
				if(e.getId().equals(id)) {
					list.add(e);
				}
			}
		}
		if(sorting != null) {
			Collections.sort(list, new SortColumnBeanComparator<E>(sorting.getPrimarySortColumn()));
		}
		return list;
	}

	public final List<Integer> getIds(final ICriteria<? extends E> criteria, Sorting sorting)
			throws InvalidCriteriaException {
		final List<SearchResult<E>> list = find(criteria, sorting);
		if(list == null) {
			return null;
		}
		final List<Integer> idlist = new ArrayList<Integer>();
		for(final SearchResult<E> e : list) {
			idlist.add(e.getEntity().getId());
		}
		return idlist;
	}

	public final IPageResult<SearchResult<E>> getPage(final ICriteria<? extends E> criteria, Sorting sorting,
			final int offset, final int pageSize) throws InvalidCriteriaException {
		List<SearchResult<E>> elist = find(criteria, sorting);
		if(elist == null) {
			elist = new ArrayList<SearchResult<E>>();
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
		final List<SearchResult<E>> subList = elist;
		return new IPageResult<SearchResult<E>>() {

			@Override
			public List<SearchResult<E>> getPageList() {
				return subList;
			}

			@Override
			public int getResultCount() {
				return size;
			}
		};
	}

	@SuppressWarnings("unchecked")
	public final E load(final NameKey key) {
		if(key == null || key.getName() == null) return null;
		if(set != null) {
			final BeanWrapper bw = new BeanWrapperImpl();
			for(final E e : set) {
				String name;
				bw.setWrappedInstance(e);
				try {
					final Object o = bw.getPropertyValue(INamedEntity.NAME);
					name = (String) o;
				}
				catch(final RuntimeException re) {
					return null;
				}
				if(key.getName().equals(name)) {
					return e;
				}
			}
		}
		return null;
	}
}
