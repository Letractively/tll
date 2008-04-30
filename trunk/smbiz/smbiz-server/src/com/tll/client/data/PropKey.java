/**
 * The Logic Lab
 * @author jpk
 * Nov 12, 2007
 */
package com.tll.client.data;

import com.tll.client.IMarshalable;

/**
 * PropKey - Encapsulates a single property name and formatting directive.
 * @author jpk
 */
// TODO do we really need this class?
public class PropKey implements IMarshalable {

	public String prop;

	public String descriptor() {
		return prop + " property key";
	}

	/**
	 * Constructor
	 */
	public PropKey() {
		super();
	}

	/**
	 * Constructor
	 * @param prop
	 */
	public PropKey(String prop) {
		super();
		this.prop = prop;
	}

	@Override
	public String toString() {
		return "prop: " + prop;
	}

}
