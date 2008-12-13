/**
 * 
 */
package com.tll.client.validate;

/**
 * CharacterValidator
 * @author jpk
 */
public class CharacterValidator implements IValidator {

	public static final CharacterValidator INSTANCE = new CharacterValidator();

	/**
	 * Constructor
	 */
	private CharacterValidator() {
		super();
	}

	public Object validate(Object value) {
		if(value == null || value instanceof Character) return value;
		final String s = value.toString();
		return s.length() < 1 ? null : new Character(value.toString().charAt(0));
	}

}
