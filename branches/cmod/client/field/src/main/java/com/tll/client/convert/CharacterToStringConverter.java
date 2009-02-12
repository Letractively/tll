/**
 * The Logic Lab
 * @author jpk
 * Jan 12, 2009
 */
package com.tll.client.convert;

/**
 * CharacterToStringConverter
 * @author jpk
 */
public class CharacterToStringConverter implements IConverter<String, Character> {

	public static final CharacterToStringConverter INSTANCE = new CharacterToStringConverter();

	/**
	 * Constructor
	 */
	private CharacterToStringConverter() {
		super();
	}

	public String convert(Character o) throws IllegalArgumentException {
		return o == null ? "" : o.toString();
	}

}
