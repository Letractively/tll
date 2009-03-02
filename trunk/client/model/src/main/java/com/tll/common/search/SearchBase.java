/**
 * The Logic Lab
 * @author jpk Sep 1, 2007
 */
package com.tll.common.search;

import java.util.ArrayList;
import java.util.List;

import com.tll.criteria.CriteriaType;
import com.tll.criteria.IQueryParam;

/**
 * SearchBase
 * @author jpk
 */
public abstract class SearchBase implements ISearch {

	private CriteriaType criteriaType;

	private String namedQuery;
	private List<IQueryParam> queryParams;

	/**
	 * Constructor
	 */
	public SearchBase() {
		super();
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

	public String getNamedQuery() {
		return namedQuery;
	}

	public void setNamedQuery(String namedQuery) {
		this.namedQuery = namedQuery;
	}

	public final List<IQueryParam> getQueryParams() {
		return queryParams;
	}

	public final void setQueryParam(IQueryParam queryParam) {
		if(queryParams == null) {
			queryParams = new ArrayList<IQueryParam>();
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

	public void clear() {
		// this.retrieveAll = false;
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
		return s;
	}

}