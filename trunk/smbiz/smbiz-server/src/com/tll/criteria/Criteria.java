package com.tll.criteria;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import com.tll.listhandler.Sorting;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;

/**
 * Intitial implementation of default primaryGroup object. Currently, this class
 * is fairly simple and only supports AND criterions.
 * @author jpk
 */
public final class Criteria<E extends IEntity> implements ICriteria<E> {

	private static final long serialVersionUID = 4274638102260498756L;

	private CriteriaType criteriaType;

	private Class<? extends E> entityClass;

	private String queryName;

	/**
	 * The sorting directive.
	 * <p>
	 * <strong>NOTE: </strong>The sorting column names are the query alias names
	 * when this criteria points to a named query.
	 */
	private Sorting sorting;

	private CriterionGroup primaryGroup = new CriterionGroup();

	private Map<String, String> queryParams;

	/**
	 * Have a non-zero default value for page size to avoid division by zero
	 * errors.
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * Constructor
	 */
	public Criteria() {
		super();
	}

	/**
	 * Constructor - Use this constructor for {@link CriteriaType#ENTITY} type
	 * criteria.
	 * @param entityClass May NOT be <code>null</code>.
	 */
	public Criteria(Class<? extends E> entityClass) {
		super();
		this.criteriaType = CriteriaType.ENTITY;
		assert entityClass != null;
		this.entityClass = entityClass;
	}

	/**
	 * Constructor - Use this constructor for {@link Query} based criteria.
	 * @param entityClass May NOT be <code>null</code>.
	 * @param queryName May NOT be <code>null</code>.
	 * @param isScalarQuery Does the referenced query return scalar results?
	 */
	public Criteria(Class<? extends E> entityClass, String queryName, boolean isScalarQuery) {
		super();
		assert entityClass != null;
		this.entityClass = entityClass;
		setQueryName(queryName, isScalarQuery);
	}

	public String descriptor() {
		return toString();
	}

	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	public void setCriteriaType(CriteriaType criteriaType) {
		if(criteriaType == null) {
			throw new IllegalArgumentException("The criteria type may not be null");
		}
		this.criteriaType = criteriaType;
	}

	public Class<? extends E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<? extends E> entityClass) {
		this.entityClass = entityClass;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName, boolean isScalarQuery) {
		assert queryName != null;
		this.criteriaType = isScalarQuery ? CriteriaType.SCALAR_NAMED_QUERY : CriteriaType.ENTITY_NAMED_QUERY;
		this.queryName = queryName;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParam(String paramName, String paramValue) {
		if(queryParams == null) {
			queryParams = new HashMap<String, String>();
		}
		queryParams.put(paramName, paramValue);
	}

	public boolean isSet() {
		return criteriaType.isQuery() ? (queryName != null) : (primaryGroup != null && primaryGroup.isSet());
	}

	public Sorting getSorting() {
		return sorting;
	}

	public void setSorting(Sorting sorting) {
		this.sorting = sorting;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public ICriterionGroup getPrimaryGroup() {
		return primaryGroup;
	}

	public void clear() {
		if(primaryGroup != null) {
			primaryGroup.clear();
		}
		sorting = null;
		queryName = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Criteria<E> clone() {
		Criteria<E> cln;
		try {
			cln = (Criteria<E>) super.clone();
		}
		catch(final CloneNotSupportedException e) {
			throw new IllegalStateException("Unable to clone Criteria.");
		}
		if(primaryGroup != null) {
			cln.primaryGroup = primaryGroup.clone();
		}
		if(sorting != null) {
			cln.sorting = sorting.copy();
		}
		return cln;
	}

	public ICriteria<E> copy() {
		return clone();
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Type: ");
		sb.append(criteriaType);
		if(entityClass != null) {
			sb.append(" (");
			sb.append(EntityUtil.typeName(entityClass));
			if(criteriaType.isQuery()) {
				sb.append(", query name: ");
				sb.append(queryName);
			}
			sb.append(")");
		}
		sb.append(" Criteria");
		sb.append(" (Set: ");
		sb.append(isSet() ? "Yes)" : "No)");
		return sb.toString();
	}

}
