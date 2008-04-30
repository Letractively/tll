package com.tll.model.schema;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Schema based information for a particular field that exists in the schema.
 * @author jpk
 */
public final class FieldData extends AbstractSchemaProperty {

	private final String name;
	private final boolean required;
	/**
	 * The max allowed length. <code>-1</code> indicates undefined.
	 */
	private final int maxLen;

	/**
	 * Constructor
	 * @param propertyType The property type
	 * @param name The property name
	 * @param required I.e. not nullable?
	 * @param maxLen The max allowed String-wise length
	 */
	public FieldData(final PropertyType propertyType, final String name, final boolean required, final int maxLen) {
		super(propertyType);
		this.name = name;
		this.required = required;
		this.maxLen = maxLen;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public boolean isRequired() {
		return required;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", getName()).append("required", isRequired()).append("maxLen",
				getMaxLen()).toString();
	}

}
