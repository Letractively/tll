/**
 * The Logic Lab
 * @author jpk
 * Feb 11, 2009
 */
package com.tll.client.admin.ui.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tll.client.cache.AuxDataCache;
import com.tll.client.convert.IdConverter;
import com.tll.client.ui.field.AbstractFieldGroupProvider;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.SelectField;
import com.tll.client.ui.field.SimpleComparator;
import com.tll.common.model.Model;
import com.tll.model.SmbizEntityType;

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
	 * The currency id converter.
	 */
	private static final IdConverter currencyIdConverter = new IdConverter(getCurrencyDataMap());

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
		return FieldFactory.fselect(name, propName, labelText, helpText, getCurrencyDataMap().keySet(),
				SimpleComparator.INSTANCE, currencyIdConverter);
	}

	/**
	 * Provides a map of the available currencies.
	 * @return Map of the the system currency ids keyed by the data store currency
	 *         id.
	 */
	protected static Map<Integer, String> getCurrencyDataMap() {
		if(currencyMap == null) {
			final List<Model> currencies = AuxDataCache.instance().getEntityList(SmbizEntityType.CURRENCY);
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
