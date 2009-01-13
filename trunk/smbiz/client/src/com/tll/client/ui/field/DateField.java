/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;
import com.google.gwt.widgetideas.client.event.KeyDownEvent;
import com.google.gwt.widgetideas.client.event.KeyDownHandler;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * DateField
 * @author jpk
 * @param <B> the bound type
 */
public class DateField<B> extends AbstractField<B, Date> implements ChangeHandler<Date>, IHasFormat {

	/**
	 * DateBox - Extended to support {@link HasFocus}.
	 * @author jpk
	 */
	private static final class DateBox extends com.google.gwt.widgetideas.datepicker.client.DateBox implements HasFocus,
			KeyDownHandler {

		public void addKeyboardListener(KeyboardListener listener) {
			throw new UnsupportedOperationException();
		}

		public void removeKeyboardListener(KeyboardListener listener) {
			throw new UnsupportedOperationException();
		}

		public void addFocusListener(FocusListener listener) {
			throw new UnsupportedOperationException();
		}

		public void removeFocusListener(FocusListener listener) {
			throw new UnsupportedOperationException();
		}

		public void onKeyDown(KeyDownEvent event) {
			if(event.isAlphaNumeric()) {
				// disallow this!
				event.getBrowserEvent().preventDefault();
				// event.getBrowserEvent().cancelBubble(true);
			}
		}

	}

	private final DateBox db;

	/**
	 * The currently selected date
	 */
	private Date seldate;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param converter
	 */
	DateField(String name, String propName, String labelText, String helpText, IConverter<Date, B> converter) {
		super(name, propName, labelText, helpText);
		setConverter(converter);
		db = new DateBox();
		db.getDatePicker().addChangeHandler(this);
		db.setDateFormat(Fmt.getDateTimeFormat(GlobalFormat.DATE));
	}

	public GlobalFormat getFormat() {
		return GlobalFormat.DATE;
	}

	public void setFormat(GlobalFormat format) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected HasFocus getEditable() {
		return db;
	}

	public String getText() {
		return db.getText();
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	public Date getValue() {
		return seldate;
	}

	@Override
	protected void setNativeValue(Date nativeValue) {
		// NOTE: this fires the onChange event
		db.getDatePicker().setSelectedDate(nativeValue);
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	public void onChange(ChangeEvent<Date> event) {
		seldate = event.getNewValue();
		super.onChange(this);
		changeSupport.firePropertyChange(PROPERTY_VALUE, event.getOldValue(), seldate);
		fireChangeListeners();
	}
}
