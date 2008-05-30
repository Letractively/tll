/**
 * The Logic Lab
 * @author jpk
 * May 10, 2008
 */
package com.tll.client.listing;

import java.util.List;

import com.tll.client.model.Model;

/**
 * IDataProvider - Generification of providing data to a listing.
 * @author jpk
 */
public interface IDataProvider {

	/**
	 * @return List of data.
	 */
	List<Model> getData();
}
