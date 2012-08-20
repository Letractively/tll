/**
 * 
 */
package com.tll.types;

import java.text.NumberFormat;

import com.tll.IMarshalable;

/**
 * Encapsulates a numerical value with a string-wise formatting directive used
 * to format the number for display.
 * @author jpk
 */
public class FNum implements IMarshalable {

	private Double value;

	private String format;

	/**
	 * Constructor
	 */
	public FNum() {
	}

	/**
	 * Constructor
	 * @param value
	 */
	public FNum(Double value) {
		this(value, null);
	}

	/**
	 * Constructor
	 * @param value
	 * @param format
	 */
	public FNum(Double value, String format) {
		this();
		setValue(value);
		setFormat(format);
	}

	/**
	 * @return The numerical value
	 */
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return The string-wise formatting directive by which the
	 *         <code>value</code> is formatted for display. The "format" of this
	 *         property is consistent with the formatting rules for Java's decimal
	 *         formatting.
	 */
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return NumberFormat.getInstance().format(getValue().doubleValue());
	}
}
