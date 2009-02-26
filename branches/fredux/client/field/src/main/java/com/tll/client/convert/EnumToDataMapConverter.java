/**
 * The Logic Lab
 * @author jpk
 * Feb 25, 2009
 */
package com.tll.client.convert;

import java.util.HashMap;
import java.util.Map;

import com.tll.INameValueProvider;


/**
 * EnumToDataMapConverter
 * @author jpk
 */
public class EnumToDataMapConverter implements IConverter<Map<String, String>, Class<? extends Enum<?>>> {
	
	public static final EnumToDataMapConverter INSTANCE = new EnumToDataMapConverter();

	/**
	 * Constructor
	 */
	private EnumToDataMapConverter() {
		super();
	}

	@Override
	public Map<String, String> convert(Class<? extends Enum<?>> enmType) throws IllegalArgumentException {
		final HashMap<String, String> map = new HashMap<String, String>();
		for(final Enum<?> enm : enmType.getEnumConstants()) {
			if(enm instanceof INameValueProvider) {
				final INameValueProvider<?> nvp = (INameValueProvider<?>) enm;
				final String sval = ToStringConverter.INSTANCE.convert(nvp.getValue());
				map.put(sval, nvp.getName());
			}
			else {
				map.put(enm.name(), enm.name());
			}
		}
		return map;
	}

}
