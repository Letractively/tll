/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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
			addStyleName(Styles.TBOX);
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

		/**
		 * Resolves the "show" value for the given value. If a corresponding data
		 * token exists for the given value, it is used as the show value.
		 * Otherwise, the given value will be the show value.
		 * @param value the value
		 * @return the "show" value for the given value.
		 */
		private String resolveShowValue(final String value) {
			final String tkn = getToken(value);
			return tkn == null ? value : tkn;
		}

		@Override
		public void setValue(final String newValue) {
			super.setValue(resolveShowValue(newValue));
		}

		@Override
		public void setValue(final String value, final boolean fireEvents) {
			super.setValue(resolveShowValue(value), fireEvents);
		}

		MultiWordSuggestOracle getOracle() {
			return (MultiWordSuggestOracle) getSuggestOracle();
		}

		@Override
		public HandlerRegistration addFocusHandler(final FocusHandler handler) {
			return addDomHandler(handler, FocusEvent.getType());
		}

		@Override
		public HandlerRegistration addBlurHandler(final BlurHandler handler) {
			return addDomHandler(handler, BlurEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
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
	SuggestField(final String name, final String propName, final String labelText, final String helpText,
			final Map<String, String> data) {
		super(name, propName, labelText, helpText);
		sb = new Impl();
		// fires when suggest box is manually edited
		sb.addValueChangeHandler(this);

		// fires when, you guessed it, a selection is clicked
		sb.addSelectionHandler(new SelectionHandler<Suggestion>() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void onSelection(final SelectionEvent<Suggestion> event) {
				ValueChangeEvent.fire(sb, getDataValue(event.getSelectedItem().getReplacementString()));
			}
		});
		sb.addFocusHandler(this);
		sb.addBlurHandler(this);
		// setConverter(ToStringConverter.INSTANCE);
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
	public void setEnabled(final boolean enabled) {
		sb.getTextBox().setEnabled(enabled);
		super.setEnabled(enabled);
	}

	@Override
	public void setData(final Map<String, String> data) {
		super.setData(data);
		buildOracle();
	}

	@Override
	public void addDataItem(final String name, final String value) {
		super.addDataItem(name, value);
		sb.getOracle().add(name);
	}

	@Override
	public void removeDataItem(final String value) {
		super.removeDataItem(value);
		buildOracle();
	}

	@Override
	public String doGetText() {
		return sb.getText();
	}

	@Override
	public void setText(final String text) {
		sb.setText(text);
	}

	@Override
	public IEditable<String> getEditable() {
		return sb;
	}
}
