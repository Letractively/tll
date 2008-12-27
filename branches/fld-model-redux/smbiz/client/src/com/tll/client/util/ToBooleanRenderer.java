/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2008
 */
package com.tll.client.util;



/**
 * ToBooleanRenderer
 * @author jpk
 */
public class ToBooleanRenderer implements IRenderer<Boolean, Object> {

	public static final ToBooleanRenderer INSTANCE = new ToBooleanRenderer();

	/**
	 * Constructor
	 */
	private ToBooleanRenderer() {
		super();
	}

	public Boolean render(Object o) {
		if(o == null) {
			return null;
		}
		if(o instanceof Boolean) {
			return (Boolean) o;
		}
		return Boolean.valueOf(o.toString());
	}

}
