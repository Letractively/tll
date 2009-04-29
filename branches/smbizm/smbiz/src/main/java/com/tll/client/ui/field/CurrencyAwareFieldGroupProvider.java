/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.client.ui.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;

/**
 * CurrencyAwareFieldGroupProvider
 * @author jpk
 */
public abstract class CurrencyAwareFieldGroupProvider extends AbstractFieldGroupProvider {

	/**
	 * Map of app available currencies.
	 */
	private static Map<Integer, String> currencyMap;

	/**
	 * Creates a new {@link SelectField} of app recognized currencies.
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @return select field containing the app currencies
	 */
	protected static final SelectField<Integer> fcurrencies(String name, String propName, String labelText,
			String helpText) {
		return fselect(name, propName, labelText, helpText, getCurrencyDataMap());
	}

	/**
	 * Provides a map of the available currencies.
	 * @return Map of the the system currency ids keyed by the data store currency
	 *         id.
	 */
	protected static Map<Integer, String> getCurrencyDataMap() {
		if(currencyMap == null) {
			final List<Model> currencies = AuxDataCache.get().getEntityList(SmbizEntityType.CURRENCY);
			if(currencies == null) return null;
			currencyMap = new HashMap<Integer, String>();
			final StringBuilder sb = new StringBuilder();
			for(final Model e : currencies) {
				sb.setLength(0);
				sb.append(e.asString("symbol"));
				sb.append(" - ");
				sb.append(e.getName());
				currencyMap.put(e.getId(), sb.toString());
			}
			return currencyMap;
		}
		return currencyMap;
	}
}
