/**
 * The Logic Lab
 * @author jpk
 * Aug 31, 2007
 */
package com.tll.client.data;

import com.tll.client.model.IData;
import com.tll.listhandler.IPage;

/**
 * ListingPayload
 * @author jpk
 */
public class ListingPayload extends Payload {

	private IPage<? extends IData> page;

	/**
	 * Constructor
	 */
	public ListingPayload() {
		super();
	}

	public IPage<? extends IData> getPage() {
		return page;
	}

	public void setPage(IPage<? extends IData> page) {
		this.page = page;
	}
}
