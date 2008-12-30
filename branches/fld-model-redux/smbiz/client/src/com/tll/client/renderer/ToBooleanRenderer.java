/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2008
 */
package com.tll.client.renderer;

/**
 * ToBooleanRenderer
 * @author jpk
 */
public class ToBooleanRenderer<I> implements IRenderer<Boolean, I> {

	public static final ToBooleanRenderer<Object> INSTANCE = new ToBooleanRenderer<Object>();

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
