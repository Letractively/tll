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
import com.tll.common.convert.IConverter;
import com.tll.common.util.ObjectUtil;

/**
 * SuggestField
 * @param <B> The bound type
 * @author jpk
 */
public final class SuggestField<B> extends AbstractField<B, String> implements SuggestionHandler {

	private final SuggestBox sb;

	private String crnt;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param suggestions The required collection of suggestions containing the
	 *        Strings that are offered as suggestions and when selected will
	 *        persist in the bound text box.
	 * @param converter Responsible for converting a given suggestion to a
	 *        presentation worthy token
	 */
	SuggestField(String name, String propName, String labelText, String helpText, Collection<B> suggestions,
			IConverter<String, B> converter) {
		super(name, propName, labelText, helpText);
		if(suggestions == null || suggestions.size() < 1) {
			throw new IllegalArgumentException("No suggestions specified.");
		}
		setConverter(converter);

		MultiWordSuggestOracle o = new MultiWordSuggestOracle();
		for(B s : suggestions) {
			o.add(converter.convert(s));
		}
		sb = new SuggestBox(o);
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
		String newval = event.getSelectedSuggestion().getReplacementString();
		if(!ObjectUtil.equals(crnt, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, crnt, newval);
			crnt = newval;
		}
	}

	public String getValue() {
		return getText();
	}

	@Override
	protected void setNativeValue(String nativeValue) {
		String old = getValue();
		setText(nativeValue);
		String newval = getValue();
		if(!ObjectUtil.equals(old, newval)) {
			changeSupport.firePropertyChange(PROPERTY_VALUE, old, newval);
		}
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	@Override
	public void onChange(Widget sender) {
		super.onChange(this);
		changeSupport.firePropertyChange(PROPERTY_VALUE, crnt, getValue());
		crnt = getValue();
		fireChangeListeners();
	}
}
