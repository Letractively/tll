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
// TODO can we just get away with a String instead???
public class PropKey implements IMarshalable {

	public String prop;

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
