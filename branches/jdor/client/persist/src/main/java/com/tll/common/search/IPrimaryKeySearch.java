/**
 * The Logic Lab
 * @author jpk
 * @since May 18, 2009
 */
package com.tll.common.search;

import com.tll.common.model.ModelKey;

/**
 * IPrimaryKeySearch - Searches by entity primary key and implies the
 * subsequently loaded model data is a marshaled entity.
 * @author jpk
 */
public interface IPrimaryKeySearch extends ISearch {

	/**
	 * @return the primary key in {@link ModelKey} form.
	 */
	ModelKey getKey();
}
