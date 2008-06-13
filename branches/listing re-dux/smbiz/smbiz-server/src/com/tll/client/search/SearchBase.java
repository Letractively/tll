/**
 * The Logic Lab
 * @author jpk Sep 1, 2007
 */
package com.tll.client.search;

import java.util.HashSet;
import java.util.Set;

import com.tll.criteria.CriteriaType;
import com.tll.criteria.IQueryParam;
import com.tll.criteria.SelectNamedQuery;

/**
 * SearchBase
 * @author jpk
 */
public abstract class SearchBase implements ISearch {

	private CriteriaType criteriaType;
	// private boolean retrieveAll = false;

	private SelectNamedQuery namedQuery;
	private Set<IQueryParam> queryParams;

	/**
	 * The name of the server side business key
	 */
	private String businessKeyName;

	/**
	 * Constructor
	 */
	public SearchBase() {
		super();
	}

	/**
	 * Constructor - Use when this search is intended for translation to a
	 * business key.
	 * @param businessKeyName The business key name
	 */
	public SearchBase(String businessKeyName) {
		super();
		this.businessKeyName = businessKeyName;
	}

	/**
	 * Constructor - Use when this search is intended for translation to server
	 * side criteria.
	 * @param criteriaType The criteria type
	 */
	public SearchBase(CriteriaType criteriaType) {
		this();
		this.criteriaType = criteriaType;
	}

	/**
	 * @return the criteriaType
	 */
	public CriteriaType getCriteriaType() {
		return criteriaType;
	}

	/**
	 * @param criteriaType the criteriaType to set
	 */
	public void setCriteriaType(CriteriaType criteriaType) {
		this.criteriaType = criteriaType;
	}

	public SelectNamedQuery getNamedQuery() {
		return namedQuery;
	}

	public void setNamedQuery(SelectNamedQuery namedQuery) {
		this.namedQuery = namedQuery;
	}

	public final Set<IQueryParam> getQueryParams() {
		return queryParams;
	}

	public final void setQueryParam(IQueryParam queryParam) {
		if(queryParams == null) {
			queryParams = new HashSet<IQueryParam>();
		}
		queryParams.add(queryParam);
	}

	/*
	public final boolean isRetrieveAll() {
		return retrieveAll;
	}

	public final void setRetrieveAll(boolean retrieveAll) {
		this.retrieveAll = retrieveAll;
	}
	*/

	public String getBusinessKeyName() {
		return businessKeyName;
	}

	public void setBusinessKeyName(String businessKeyName) {
		this.businessKeyName = businessKeyName;
	}

	public void clear() {
		// this.retrieveAll = false;
		this.businessKeyName = null;
	}

	@Override
	public String toString() {
		String s = "";
		if(criteriaType != null) {
			switch(criteriaType) {
				case ENTITY:
					s = "ENTITY";
					break;
				case ENTITY_NAMED_QUERY:
				case SCALAR_NAMED_QUERY:
					s = namedQuery.toString();
					break;
			}
		}
		else if(businessKeyName != null) {
			s = "Business key: " + businessKeyName;
		}

		return s;
	}

}
