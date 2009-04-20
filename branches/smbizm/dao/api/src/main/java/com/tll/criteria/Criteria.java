package com.tll.criteria;

import java.util.ArrayList;
import java.util.List;

import com.tll.model.IEntity;

/**
 * Intitial implementation of default primaryGroup object. Currently, this class
 * is fairly simple and only supports AND criterions.
 * @author jpk
 * @param <E>
 */
public final class Criteria<E extends IEntity> implements ICriteria<E> {

	private static final long serialVersionUID = 4274638102260498756L;

	private CriteriaType criteriaType;

	private Class<E> entityClass;

	private ISelectNamedQueryDef namedQueryDefinition;

	private final CriterionGroup primaryGroup = new CriterionGroup();

	private List<IQueryParam> queryParams;

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
	public Criteria(Class<E> entityClass) {
		super();
		this.criteriaType = CriteriaType.ENTITY;
		assert entityClass != null;
		this.entityClass = entityClass;
	}

	/**
	 * Constructor - Use this constructor for criteria pointing to a named query.
	 * @param namedQueryDefinition The named query definition
	 * @param queryParams The possible query parameters
	 */
	@SuppressWarnings("unchecked")
	public Criteria(ISelectNamedQueryDef namedQueryDefinition, List<IQueryParam> queryParams) {
		super();
		this.criteriaType =
				namedQueryDefinition.isScalar() ? CriteriaType.SCALAR_NAMED_QUERY : CriteriaType.ENTITY_NAMED_QUERY;
		this.entityClass = (Class<E>) namedQueryDefinition.getEntityType();
		this.namedQueryDefinition = namedQueryDefinition;
		this.queryParams = queryParams;
	}

	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public ISelectNamedQueryDef getNamedQueryDefinition() {
		return namedQueryDefinition;
	}

	public List<IQueryParam> getQueryParams() {
		return queryParams;
	}

	public void setQueryParam(IQueryParam param) {
		if(queryParams == null) {
			queryParams = new ArrayList<IQueryParam>();
		}
		queryParams.add(param);
	}

	public boolean isSet() {
		return criteriaType.isQuery() ? (namedQueryDefinition != null) : (primaryGroup != null && primaryGroup.isSet());
	}

	public CriterionGroup getPrimaryGroup() {
		return primaryGroup;
	}

	public void clear() {
		if(primaryGroup != null) {
			primaryGroup.clear();
		}
		if(queryParams != null) {
			queryParams.clear();
		}
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("Type: ");
		sb.append(criteriaType);
		if(entityClass != null) {
			sb.append(" (");
			sb.append(entityClass);
			if(criteriaType.isQuery()) {
				sb.append(", base query name: ");
				sb.append(namedQueryDefinition.getQueryName());
			}
			sb.append(")");
		}
		sb.append(" Criteria");
		sb.append(" (Set: ");
		sb.append(isSet() ? "Yes)" : "No)");
		return sb.toString();
	}

}
