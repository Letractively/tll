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
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.DateValidator;

/**
 * DateField
 * @author jpk
 */
public class DateField extends AbstractField<Date> implements ChangeHandler<Date> {

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

	private final DateBox db;

	/**
	 * Constructor
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param dateFormat
	 */
	public DateField(String propName, String labelText, String helpText, GlobalFormat dateFormat) {
		super(propName, labelText, helpText);
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
		db = new DateBox();
		db.getDatePicker().addChangeHandler(this);
		db.setDateFormat(Fmt.getDateTimeFormat(dateFormat));
	}

	public void onChange(ChangeEvent<Date> event) {
		super.onChange(this);
		fireWidgetChange();
		changeSupport.firePropertyChange("value", event.getOldValue(), event.getNewValue());
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
		return db.getDatePicker().getSelectedDate();
	}

	public void setValue(Object value) {
		final Date old = getValue();
		final Date newval = value == null ? null : getRenderer().render(value);
		if(old != newval && (old != null && !old.equals(newval)) || (newval != null && !newval.equals(old))) {
			db.getDatePicker().setSelectedDate(newval);
		}
	}
}
