/**
 * The Logic Lab
 * @author jpk
 * May 10, 2008
 */
package com.tll.client.listing;

/**
 * IDataProvider - Generification of providing data to a listing.
 * @author jpk
 * @param <D> the data type
 */
public interface IDataProvider<D> {

	/**
	 * @return Array of data.
	 */
	D[] getData();
}
