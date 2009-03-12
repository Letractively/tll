/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.tll.client.convert.ToStringConverter;

/**
 * SuggestField
 * @author jpk
 */
public final class SuggestField extends AbstractDataField<String, String> {

	/**
	 * Impl
	 * @author jpk
	 */
	final class Impl extends SuggestBox implements IEditable<String> {

		/**
		 * Constructor
		 */
		public Impl() {
			super(new MultiWordSuggestOracle());
		}

		@Override
		public String getValue() {
			final String s = super.getValue();
			try {
				return getDataValue(s);
			}
			catch(final IllegalArgumentException e) {
				// ok
			}
			return s;
		}

		MultiWordSuggestOracle getOracle() {
			return (MultiWordSuggestOracle) getSuggestOracle();
		}

		@Override
		public HandlerRegistration addBlurHandler(BlurHandler handler) {
			return addDomHandler(handler, BlurEvent.getType());
		}
		
		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}
	}

	private final Impl sb;
	
	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param data
	 */
	SuggestField(String name, String propName, String labelText, String helpText, Map<String, String> data) {
		super(name, propName, labelText, helpText);
		sb = new Impl();
		// fires when suggest box is manually edited
		sb.addValueChangeHandler(this);
		
		// fires when, you guessed it, a selection is clicked
		sb.addSelectionHandler(new SelectionHandler<Suggestion>() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				ValueChangeEvent.fire(sb, getDataValue(event.getSelectedItem().getReplacementString()));
			}
		});
		sb.addBlurHandler(this);
		setConverter(ToStringConverter.INSTANCE);
		setData(data);
	}
	
	private void buildOracle() {
		final MultiWordSuggestOracle oracle = sb.getOracle();
		oracle.clear();
		final Map<String, String> data = getData();
		if(data != null) {
			for(final String s : data.values()) {
				oracle.add(s);
			}
		}
	}

	@Override
	public void setData(Map<String, String> data) {
		super.setData(data);
		buildOracle();
	}

	@Override
	public void addDataItem(String name, String value) {
		super.addDataItem(name, value);
		sb.getOracle().add(name);
	}

	@Override
	public void removeDataItem(String value) {
		super.removeDataItem(value);
		buildOracle();
	}

	public String getText() {
		return sb.getText();
	}

	public void setText(String text) {
		sb.setText(text);
	}

	@Override
	public IEditable<String> getEditable() {
		return sb;
	}
}
