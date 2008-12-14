/**
 * The Logic Lab
 * @author jpk
 * Jun 13, 2008
 */
package com.tll.client.ui.listing;

import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.IListingConfig;
import com.tll.client.model.IData;

/**
 * DataListingWidget - Listing that lists arbitrary data. May be extended to
 * handle say model change events.
 * @author jpk
 */
public class DataListingWidget<R extends IData> extends ListingWidget<R> {

	/**
	 * Constructor
	 * @param config
	 */
	public DataListingWidget(IListingConfig<R> config) {
		super(config, new ListingTable<R>(config));
	}

	public void onModelChangeEvent(ModelChangeEvent event) {
		// base impl: no op
	}

}
