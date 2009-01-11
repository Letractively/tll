/**
 * The Logic Lab
 * @author jpk
 * Jan 10, 2009
 */
package com.tll.client.convert;

import java.util.Map;

/**
 * RefDataMapConverter
 * @author jpk
 */
public class RefDataMapConverter implements IConverter<String, Object> {

	/**
	 * Constructor
	 * @param refDataMap
	 */
	public RefDataMapConverter(Map<String, String> refDataMap) {
		super();
		this.refDataMap = refDataMap;
	}

	private final Map<String, String> refDataMap;

	public String convert(Object in) throws IllegalArgumentException {
		return refDataMap.get(ToStringConverter.INSTANCE.convert(in));
	}

}
