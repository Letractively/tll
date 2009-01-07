/**
 * The Logic Lab
 * @author jpk
 * Dec 27, 2008
 */
package com.tll.client.convert;

/**
 * BooleanConverter
 * @author jpk
 * @param <I> the input type
 */
public class BooleanConverter<I> implements IConverter<Boolean, I> {

	public static final BooleanConverter<Object> INSTANCE = new BooleanConverter<Object>();

	/**
	 * Constructor
	 */
	private BooleanConverter() {
		super();
	}

	public Boolean convert(Object o) {
		if(o == null) {
			return null;
		}
		if(o instanceof Boolean) {
			return (Boolean) o;
		}
		return Boolean.valueOf(o.toString());
	}

}
