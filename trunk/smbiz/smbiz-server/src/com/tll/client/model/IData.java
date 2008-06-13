/**
 * The Logic Lab
 * @author jkirton
 * Jun 11, 2008
 */
package com.tll.client.model;

import com.tll.client.IMarshalable;

/**
 * IData - Marker interface indicating support for "data". The meaning is
 * implementation dependant.
 * @author jpk
 */
public interface IData extends IMarshalable {

	/**
	 * @return The unique ref for this data.
	 */
	RefKey getRefKey();
}
