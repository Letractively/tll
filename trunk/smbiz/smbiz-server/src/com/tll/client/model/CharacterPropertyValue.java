/**
 * The Logic Lab
 */
package com.tll.client.model;

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
	 * @param name
	 * @param pdata
	 * @param value
	 */
	public CharacterPropertyValue(String name, PropertyData pdata, Character value) {
		super(name, pdata);
		this.value = value;
	}

	public String descriptor() {
		return "Character property";
	}

	public PropertyType getType() {
		return PropertyType.CHAR;
	}

	public void clear() {
		this.value = null;
	}

	public IPropertyValue copy() {
		return new CharacterPropertyValue(getPropertyName(), pdata, value == null ? null : new Character(value.charValue()));
	}

	public final Object getValue() {
		return value;
	}

	public String asString() {
		return value == null ? null : value.toString();
	}

	public void setValue(Object obj) {
		if(obj instanceof Character == false) {
			throw new IllegalArgumentException("The value must be a Character");
		}
		setCharacter((Character) obj);
	}

	public void setCharacter(Character value) {
		this.value = value;
	}

	public Character getCharacter() {
		return value;
	}
}
