/**
 * The Logic Lab
 */
package com.tll.common.model;

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
		return new CharacterPropertyValue(propertyName, metadata, value == null ? null : new Character(value.charValue()));
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
			if(obj instanceof String && ((String) obj).length() == 1) {
				this.value = Character.valueOf(((String) obj).charAt(0));
				return;
			}
			throw new IllegalArgumentException("The value must be a Character");
		}
		this.value = (Character) obj;
	}

	public Character getCharacter() {
		return value;
	}
}
