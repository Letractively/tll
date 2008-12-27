/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.widgetideas.client.event.ChangeEvent;
import com.google.gwt.widgetideas.client.event.ChangeHandler;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.DateValidator;

/**
 * DateField
 * @author jpk
 */
public class DateField extends AbstractField<Date> implements ChangeHandler<Date>, HasText {

	/**
	 * DateBox - Extended to support {@link HasFocus}.
	 * @author jpk
	 */
	private static final class DateBox extends com.google.gwt.widgetideas.datepicker.client.DateBox implements HasFocus {

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

	}

	private DateBox db;
	private final GlobalFormat dateFormat;

	private final ChangeListenerCollection changeListeners = new ChangeListenerCollection();

	/**
	 * Constructor
	 * @param propName
	 * @param lblTxt
	 * @param dateFormat
	 */
	public DateField(String propName, String lblTxt, GlobalFormat dateFormat) {
		super(propName, lblTxt);
		switch(dateFormat) {
			case DATE:
				setRenderer(DateValidator.DATE_VALIDATOR);
				break;
			case TIME:
				setRenderer(DateValidator.DATE_VALIDATOR);
				break;
			case TIMESTAMP:
				setRenderer(DateValidator.DATE_VALIDATOR);
				break;
			default:
				throw new IllegalArgumentException("A date type format must be specified.");
		}
		this.dateFormat = dateFormat;
	}

	/**
	 * @return The ascribed date format.
	 */
	public GlobalFormat getDateFormat() {
		return dateFormat;
	}

	public DateBox getDateBox() {
		if(db == null) {
			db = new DateBox();
			db.getDatePicker().addChangeHandler(this);
			db.setDateFormat(Fmt.getDateTimeFormat(dateFormat));
		}
		return db;
	}

	public void onChange(ChangeEvent<Date> event) {
		super.onChange(this);
		changeListeners.fireChange(this);
	}

	@Override
	protected HasFocus getEditable(String value) {
		return getDateBox();
	}

	@Override
	protected String getEditableValue() {
		return db == null ? null : db.getText();
	}

	public String getText() {
		if(isReadOnly()) {
			return getFieldValue();
		}
		return getEditableValue();
	}

	public void setText(String text) {
		if(isReadOnly()) {
			setFieldValue(text);
		}
		throw new IllegalStateException("Date fields only provide text when they are read-only.");
	}

	public Date getValue() {
		if(isReadOnly()) {
			return getRenderer().render(getFieldValue());
		}
		return db.getDatePicker().getSelectedDate();
	}

	public void setValue(Object value) {
		final Date dvalue = getRenderer().render(value);
		if(isReadOnly()) {
			setFieldValue(dvalue == null ? null : Fmt.format(dvalue, dateFormat));
		}
		getDateBox().getDatePicker().setSelectedDate(getRenderer().render(value));
	}

	public void addChangeListener(ChangeListener listener) {
		changeListeners.add(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		changeListeners.remove(listener);
	}

}
