/**
 * The Logic Lab
 */
package com.tll.client.model;

import com.tll.model.schema.PropertyMetadata;
import com.tll.model.schema.PropertyType;

/**
 * CharacterPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
public class CharacterPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

	protected Character value;

	/**
	 * Constructor
	 */
	public CharacterPropertyValue() {
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
	public CharacterPropertyValue(String propertyName, Character value) {
		this(propertyName, null, value);
	}

	/**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
	public CharacterPropertyValue(String propertyName, PropertyMetadata metadata, Character value) {
		super(propertyName, metadata);
		this.value = value;
	}

	public PropertyType getType() {
		return PropertyType.CHAR;
	}

	public IPropertyValue copy() {
		return new CharacterPropertyValue(getPropertyName(), metadata, value == null ? null : new Character(value
				.charValue()));
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value == null ? null : value.toString();
	}

	@Override
	protected void doSetValue(Object obj) {
		if(obj != null && obj instanceof Character == false) {
			throw new IllegalArgumentException("The value must be a Character");
		}
		this.value = (Character) obj;
	}

	public Character getCharacter() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!super.equals(obj)) return false;
		if(getClass() != obj.getClass()) return false;
		final CharacterPropertyValue other = (CharacterPropertyValue) obj;
		if(value == null) {
			if(other.value != null) return false;
		}
		else if(!value.equals(other.value)) return false;
		return true;
	}

}
