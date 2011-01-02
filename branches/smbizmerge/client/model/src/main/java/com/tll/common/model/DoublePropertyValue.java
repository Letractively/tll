/**
 * The Logic Lab
 */
package com.tll.common.model;

import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

/**
 * DoublePropertyValue - Generic holder for {@link Double}s.
 * <p>
 * NOTE: the property metadata's property type may point to a float but we
 * "emmulate" floats as doubles due to the way js handles numbers.
 * @see <a
 *      href="http://babbage.cs.qc.edu/courses/cs341/IEEE-754references.html">IEEE-754</a>
 * @author jpk
 */
public class DoublePropertyValue extends AbstractPropertyValue {

	protected Double value;

	/**
	 * Constructor
	 */
	public DoublePropertyValue() {
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public DoublePropertyValue(final String propertyName, final Double value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public DoublePropertyValue(final String propertyName, final PropertyMetadata metadata, final Double value) {
		super(propertyName, metadata);
		this.value = value;
	}

	@Override
	public PropertyType getType() {
		return PropertyType.DOUBLE;
	}

	@Override
	public IPropertyValue copy() {
		return new DoublePropertyValue(propertyName, metadata, value == null ? null : new Double(value.doubleValue()));
	}

	@Override
	public final Object getValue() {
		return value;
	}

	@Override
	protected void doSetValue(final Object obj) {
		if(obj == null) {
			this.value = null;
		}
		else if(obj instanceof Double) {
			this.value = (Double) obj;
		}
		else {
			throw new IllegalArgumentException("The value must be a Double");
		}
	}

	public Double getDouble() {
		return value;
	}
}
