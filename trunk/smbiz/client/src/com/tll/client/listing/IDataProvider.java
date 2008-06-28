/**
 * The Logic Lab
 * @author jpk
 * May 10, 2008
 */
package com.tll.client.listing;

import com.tll.client.model.IData;

/**
 * IDataProvider - Generification of providing data to a listing.
 * @author jpk
 */
public interface IDataProvider<D extends IData> {

	/**
	 * @return Array of data.
	 */
	D[] getData();
}
