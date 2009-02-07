/**
 * The Logic Lab
 * @author jpk
 * Jan 10, 2009
 */
package com.tll.common.convert;

import java.util.Map;

/**
 * IdConverter - Converts a {@link Integer} id to a descriptive String using a
 * reference map.
 * @author jpk
 */
public class IdConverter implements IConverter<String, Integer> {

	/**
	 * Map of ids and their corresponding descriptive {@link String}s.
	 */
	private final Map<Integer, String> idMap;

	/**
	 * Constructor
	 * @param idMap
	 */
	public IdConverter(Map<Integer, String> idMap) {
		super();
		this.idMap = idMap;
	}

	public String convert(Integer in) throws IllegalArgumentException {
		return idMap.get(in);
	}

}
