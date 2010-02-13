/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import java.util.List;

import com.tll.common.model.IEntityTypeProvider;
import com.tll.schema.IQueryParam;


/**
 * INamedQuerySearch
 * @author jpk
 */
public interface INamedQuerySearch extends IListingSearch, IEntityTypeProvider {

	/**
	 * @return The name of the server-side named query definition.
	 */
	String getQueryName();

	/**
	 * @return Possible query parameters when a named query is specified.
	 */
	List<IQueryParam> getQueryParams();

}
