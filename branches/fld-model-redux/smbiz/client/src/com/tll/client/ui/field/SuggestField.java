/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * SuggestField
 * @author jpk
 */
public final class SuggestField extends AbstractDataMapField implements SuggestionHandler, HasText {

	private SuggestBox sb;
	private String old;

	private final ChangeListenerCollection changeListeners = new ChangeListenerCollection();

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
			// sb.addFocusListener(this);
			sb.addChangeListener(this);
			sb.addEventHandler(this);
		}
		return sb;
	}

	public void addChangeListener(ChangeListener listener) {
		getSuggestBox().addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		getSuggestBox().removeChangeListener(listener);
	}

	public String getText() {
		return getFieldValue();
	}

	public void setText(String text) {
		if(!isReadOnly()) {
			getSuggestBox().setText(text);
		}
		else {
			setFieldValue(text);
		}
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

	public String getValue() {
		final SuggestBox sb = getSuggestBox();
		try {
			return sb.getText().length() == 0 ? null : sb.getText();
		}
		catch(RuntimeException re) {
			GWT.log("" + sb, re);
			return null;
		}
	}

	public void setValue(Object value) {
		String old = getValue();
		setText(getRenderer() != null ? getRenderer().render(value) : "" + value);
		if(getValue() != old && getValue() != null && getValue().equals(old)) {
			changeSupport.firePropertyChange("value", old, getValue());
		}
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		changeSupport.firePropertyChange("value", old, getValue());
		old = getValue();
		changeListeners.fireChange(this);
	}
}
