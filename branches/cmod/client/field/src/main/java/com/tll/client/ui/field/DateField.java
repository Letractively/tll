/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

/**
 * DateField
 * @author jpk
 * @param <B> the bound type
 */
public class DateField<B> extends AbstractField<B, Date> implements ValueChangeHandler<Date>, IHasFormat {

	/**
	 * DateBox - Extended to support {@link Focusable}.
	 * @author jpk
	 */
	private static final class DateBox extends com.google.gwt.user.datepicker.client.DateBox implements Focusable {
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
	@SuppressWarnings("synthetic-access")
	DateField(String name, String propName, String labelText, String helpText, IConverter<Date, B> converter) {
		super(name, propName, labelText, helpText);
		setConverter(converter);
		db = new DateBox();
		db.getDatePicker().addValueChangeHandler(this);
		db.setFormat(new DefaultFormat(Fmt.getDateTimeFormat(GlobalFormat.DATE)));
	}

	public GlobalFormat getFormat() {
		return GlobalFormat.DATE;
	}

	public void setFormat(GlobalFormat format) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected FocusWidget getEditable() {
		return db.getTextBox();
	}

	public String getText() {
		return db.getTextBox().getText();
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
		if(nativeValue == null) {
			db.setValue(null);
		}
		else {
			db.setValue(nativeValue);
			//setSelectedDate(nativeValue);
		}
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}

	public void onValueChange(ValueChangeEvent<Date> event) {
		final Date old = seldate;
		seldate = event.getValue();
		super.onChange(null); // TODO fix
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, seldate);
		fireChangeListeners();
	}
}
