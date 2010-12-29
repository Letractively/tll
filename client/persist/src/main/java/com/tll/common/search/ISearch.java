/**
 * The Logic Lab
 * @author jpk Aug 30, 2007
 */
package com.tll.common.search;

import org.springframework.ui.Model;

import com.tll.IMarshalable;

/**
 * ISearch - Client side search criteria definition for a single {@link Model}
 * instance that may or may not represent a server side entity.
 * @author jpk
 */
public interface ISearch extends IMarshalable {

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
