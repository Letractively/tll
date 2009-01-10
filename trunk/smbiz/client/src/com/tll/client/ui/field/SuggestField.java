/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.ToStringConverter;
import com.tll.client.util.ObjectUtil;
import com.tll.client.util.StringUtil;

/**
 * SuggestField
 * @author jpk
 */
public final class SuggestField extends AbstractField<String> implements SuggestionHandler {

	private final SuggestBox sb;
	private String old;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param suggestions The required collection of suggestions.
	 */
	public SuggestField(String name, String propName, String labelText, String helpText, Collection<?> suggestions) {
		super(name, propName, labelText, helpText);
		if(suggestions == null || suggestions.size() < 1) {
			throw new IllegalArgumentException("No suggestions specified.");
		}
		setConverter(ToStringConverter.INSTANCE);

		MultiWordSuggestOracle o = new MultiWordSuggestOracle();
		for(Object s : suggestions) {
			o.add(ToStringConverter.INSTANCE.convert(s));
		}
		sb = new SuggestBox(o);
		// sb.addFocusListener(this);
		sb.addChangeListener(this);
		sb.addEventHandler(this);
	}

	public String getText() {
		return sb.getText();
	}

	public void setText(String text) {
		sb.setText(text);
	}

	@Override
	protected HasFocus getEditable() {
		return sb;
	}

	public void onSuggestionSelected(SuggestionEvent event) {
		String newval = getValue();
		if(changeSupport != null && !ObjectUtil.equals(old, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	public String getValue() {
		String t = sb.getText();
		return StringUtil.isEmpty(t) ? null : t;
	}

	public void setValue(Object value) {
		String old = getValue();
		setText(getConverter().convert(value));
		Object newval = getValue();
		if(changeSupport != null && !ObjectUtil.equals(old, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(sender);
		if(changeSupport != null) changeSupport.firePropertyChange(PROPERTY_VALUE, old, getValue());
		old = getValue();
		fireChangeListeners();
	}
}
