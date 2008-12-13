/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;

/**
 * SuggestField
 * @author jpk
 */
public final class SuggestField extends AbstractDataMapField implements SuggestionHandler {

	private SuggestBox sb;

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 */
	public SuggestField(String propName, String lblTxt, Map<String, String> dataMap) {
		super(propName, lblTxt, dataMap);
	}

	public SuggestBox getSuggestBox() {
		if(sb == null) {
			if(dataMap != null) {
				MultiWordSuggestOracle o = new MultiWordSuggestOracle();
				for(String n : dataMap.keySet()) {
					o.add(n);
				}
				sb = new SuggestBox(o);
			}
			else {
				sb = new SuggestBox();
			}
			sb.addFocusListener(this);
			sb.addEventHandler(this);
		}
		return sb;
	}

	@Override
	protected HasFocus getEditable(String value) {
		getSuggestBox();
		if(value != null) {
			boolean valueSet = false;
			if(dataMap != null) {
				for(String n : dataMap.keySet()) {
					String v = dataMap.get(n);
					if(v != null && v.equals(value)) {
						sb.setText(n);
						valueSet = true;
						break;
					}
				}
			}
			if(!valueSet) {
				// revert to raw value
				sb.setText(value);
			}
		}
		return sb;
	}

	@Override
	protected String getEditableValue() {
		if(sb == null) return null;
		String rawValue = sb.getText();
		if(dataMap == null) return rawValue;
		String value = dataMap.get(rawValue);
		return value == null ? rawValue : value;
	}

	public void onSuggestionSelected(SuggestionEvent event) {
	}

	@Override
	public void setDataMap(Map<String, String> dataMap) {
		super.setDataMap(dataMap);
		sb = null; // force re-create if already set
	}

}