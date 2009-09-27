/**
 * The Logic Lab
 * @author jpk
 * @since May 17, 2009
 */
package com.tll.common.data;

import com.tll.common.search.ISearch;

/**
 * LoadRequest - Generic way of loading model data that doesn't necessarily have
 * clean entity boundaries.
 * @author jpk
 * @param <S> the search type
 */
public class LoadRequest<S extends ISearch> extends ModelRequest {

	/**
	 * The client-side search criteria.
	 */
	private S search;

	/**
	 * The aux data request. May be <code>null</code>.
	 */
	private AuxDataRequest auxDataRequest;

	/**
	 * Constructor
	 */
	public LoadRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param search
	 */
	public LoadRequest(S search) {
		super();
		setSearch(search);
	}

	/**
	 * @return the search
	 */
	public S getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(S search) {
		this.search = search;
	}

	/**
	 * @return the auxDataRequest
	 */
	public AuxDataRequest getAuxDataRequest() {
		return auxDataRequest;
	}

	/**
	 * @param auxDataRequest the auxDataRequest to set
	 */
	public void setAuxDataRequest(AuxDataRequest auxDataRequest) {
		this.auxDataRequest = auxDataRequest;
	}

	@Override
	public String descriptor() {
		return search == null ? "Load Request" : "Load Request for: " + search;
	}

}
