/**
 * The Logic Lab
 * @author jpk
 * @since May 19, 2009
 */
package com.tll.common.search;

import com.tll.IMarshalable;
import com.tll.common.model.IEntityTypeProvider;
import com.tll.criteria.CriteriaType;

/**
 * IListingSearch - Represents implementation dependent search criteria for
 * multiple model data instances of like type.
 * @author jpk
 */
public interface IListingSearch extends IMarshalable, IEntityTypeProvider {

	/**
	 * @return The server side criteria type.
	 */
	CriteriaType getCriteriaType();

	/**
	 * Is this search instance set?
	 * @return true/false
	 */
	boolean isSet();

	/**
	 * Resets the state of this search instance.
	 */
	void clear();

}
