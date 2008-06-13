package com.tll.criteria;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

	private SelectNamedQuery namedQueryDefinition;

	private CriterionGroup primaryGroup = new CriterionGroup();

	private Set<IQueryParam> queryParams;

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
	 * Constructor - Use this constructor for criteria pointing to a named query.
	 * @param namedQueryDefinition The named query definition
	 * @param queryParams The possible query parameters
	 */
	public Criteria(SelectNamedQuery namedQueryDefinition, Set<IQueryParam> queryParams) {
		super();
		this.criteriaType =
				namedQueryDefinition.isScalar() ? CriteriaType.SCALAR_NAMED_QUERY : CriteriaType.ENTITY_NAMED_QUERY;
		this.entityClass = EntityUtil.entityClassFromType(namedQueryDefinition.getEntityType());
		this.namedQueryDefinition = namedQueryDefinition;
		this.queryParams = queryParams;
	}

	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	public Class<? extends E> getEntityClass() {
		return entityClass;
	}

	public SelectNamedQuery getNamedQueryDefinition() {
		return namedQueryDefinition;
	}

	public Collection<IQueryParam> getQueryParams() {
		return queryParams;
	}

	public void setQueryParam(IQueryParam param) {
		if(queryParams == null) {
			queryParams = new HashSet<IQueryParam>();
		}
		queryParams.add(param);
	}

	public boolean isSet() {
		return criteriaType.isQuery() ? (namedQueryDefinition != null) : (primaryGroup != null && primaryGroup.isSet());
	}

	public ICriterionGroup getPrimaryGroup() {
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
