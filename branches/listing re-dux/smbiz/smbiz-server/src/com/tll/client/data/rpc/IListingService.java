/**
 * The Logic Lab
 * @author jpk
 * Aug 30, 2007
 */
package com.tll.client.data.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.tll.client.data.IListingCommand;
import com.tll.client.data.ListingPayload;
import com.tll.client.search.ISearch;

/**
 * IListingService
 * @author jpk
 */
public interface IListingService extends RemoteService {

	/**
	 * Processes a listing command.
	 * @param listingCommand
	 * @return ListingPayload
	 */
	ListingPayload process(IListingCommand<? extends ISearch> listingCommand);
}
