/**
 * The Logic Lab
 * @author jpk
 * May 1, 2008
 */
package com.tll.client.util;

import java.util.HashMap;
import java.util.Map;

import com.tll.util.INameValueProvider;

/**
 * ClientEnumUtil
 * @author jpk
 */
// TODO eliminate!!!
public class ClientEnumUtil {

	/**
	 * Converts an enum to a Map factorting in the possibility the given enum type
	 * may be a {@link INameValueProvider} instance. <br>
	 * NOTE: The enum "name" is the map key.
	 * @param enumType
	 * @return a name/value Map
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<?>> Map<String, String> toMap(Class<E> enumType) {
		// TODO temp commented out until GWT fully supports LinkedHashMaps!!!
		// final Map<String, String> map = new LinkedHashMap<String, String>();
		final Map<String, String> map = new HashMap<String, String>();
		for(final Object e : enumType.getEnumConstants()) {
			if(e instanceof INameValueProvider) {
				final INameValueProvider<String> senum = (INameValueProvider<String>) e;
				final Object ov = senum.getValue();
				final String val = ov == null ? null : ov.toString();
				map.put(senum.getName(), val);
			}
			else {
				final String s = e.toString();
				map.put(s, s);
			}
		}
		return map;
	}

}
