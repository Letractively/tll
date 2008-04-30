/**
 * The Logic Lab
 * @author jpk
 * Aug 31, 2007
 */
package com.tll.client.data;

import com.tll.client.model.Model;
import com.tll.listhandler.IPage;

/**
 * ListingPayload
 * @author jpk
 */
public class ListingPayload extends Payload {

	private IPage<Model> page;

	/**
	 * Constructor
	 */
	public ListingPayload() {
		super();
	}

	public IPage<Model> getPage() {
		return page;
	}

	public void setPage(IPage<Model> page) {
		this.page = page;
	}
}
