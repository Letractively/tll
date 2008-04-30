/**
 * The Logic Lab
 * @author jpk Sep 1, 2007
 */
package com.tll.client.search;

import java.util.HashMap;
import java.util.Map;

/**
 * SearchBase
 * @author jpk
 */
public abstract class SearchBase implements ISearch {

	private int searchType;
	private boolean retrieveAll = false;

	private String queryName;
	private Map<String, String> queryParams;

	/**
	 * Constructor
	 */
	public SearchBase() {
		super();
	}

	/**
	 * Constructor
	 * @param searchType May NOT be <code>null</code>.
	 */
	public SearchBase(int searchType) {
		this();
		assert searchType == TYPE_ENTTY || searchType == TYPE_ENTTY_QUERY || searchType == TYPE_SCALER_QUERY;
		this.searchType = searchType;
	}

	public String descriptor() {
		return "Search criteria";
	}

	/**
	 * @return the searchType
	 */
	public int getSearchType() {
		return searchType;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public final String getQueryName() {
		return queryName;
	}

	public final void setQueryName(String searchName) {
		this.queryName = searchName;
	}

	public final Map<String, String> getQueryParams() {
		return queryParams;
	}

	public final void setQueryParam(String paramName, String paramValue) {
		if(queryParams == null) {
			queryParams = new HashMap<String, String>();
		}
		queryParams.put(paramName, paramValue);
	}

	public final boolean isRetrieveAll() {
		return retrieveAll;
	}

	public final void setRetrieveAll(boolean retrieveAll) {
		this.retrieveAll = retrieveAll;
	}

	public void clear() {
		this.retrieveAll = false;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof SearchBase == false) return false;
		final SearchBase that = (SearchBase) obj;
		return (that.searchType == this.searchType) && that.queryName != null && that.queryName.equals(this.queryName)
				&& that.retrieveAll == this.retrieveAll;
	}

	@Override
	public int hashCode() {
		int hash = searchType;
		if(queryName != null) {
			hash = hash * 31 + queryName.hashCode();
		}
		hash += (retrieveAll ? 31 : 0);
		return hash;
	}

	@Override
	public String toString() {
		String s = "";
		switch(searchType) {
			case TYPE_ENTTY:
				s = "ENTITY";
				break;
			case TYPE_ENTTY_QUERY:
				s = "ENTITY_QUERY (query:" + queryName + ")";
				break;
			case TYPE_SCALER_QUERY:
				s = "SCALAR_QUERY (query:" + queryName + ")";
				break;
		}
		return s;
	}

}
