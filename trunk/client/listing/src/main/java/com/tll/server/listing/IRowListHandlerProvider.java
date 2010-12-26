/**
 * The Logic Lab
 * @author jpk
 * @since Dec 26, 2010
 */
package com.tll.server.listing;

import com.tll.IMarshalable;
import com.tll.common.data.RemoteListingDefinition;
import com.tll.listhandler.IListHandler;


/**
 * General contract for providing fresh row list handlers given "raw" search criteria.
 * @author jpk
 * @param <S> raw search criteria
 * @param <R> row data type
 */
public interface IRowListHandlerProvider<S extends IMarshalable, R extends IMarshalable> {

	/**
	 * Generates a row list handler from a listing definition
	 * @param listingDef
	 * @return newly created row list handler
	 */
	IListHandler<R> getRowListHandler(RemoteListingDefinition<S> listingDef);
}
