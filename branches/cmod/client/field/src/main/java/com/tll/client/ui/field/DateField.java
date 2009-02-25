/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.datepicker.client.DateBox;
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
	 * FocusableDateBox - For some reason {@link DateBox} has the
	 * {@link Focusable} methods declared yet does not implement the
	 * {@link Focusable} interface!
	 * @author jpk
	 */
	final class FocusableDateBox extends DateBox implements Focusable {

	}

	/**
	 * The target date box.
	 */
	private final FocusableDateBox dbox;

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
		dbox = new FocusableDateBox();
		dbox.addValueChangeHandler(this);
		dbox.setFormat(new DefaultFormat(Fmt.getDateTimeFormat(GlobalFormat.DATE)));
	}

	public GlobalFormat getFormat() {
		return GlobalFormat.DATE;
	}

	public void setFormat(GlobalFormat format) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Focusable getEditable() {
		return dbox;
	}

	public String getText() {
		return dbox.getTextBox().getText();
	}

	public void setText(String text) {
		dbox.getTextBox().setText(text);
	}

	public Date getValue() {
		return seldate;
	}

	@Override
	protected void setNativeValue(Date nativeValue) {
		// NOTE: this fires the onChange event
		dbox.setValue(nativeValue);
	}

	@Override
	protected void doSetValue(B value) {
		setNativeValue(getConverter().convert(value));
	}
	
	public void onValueChange(ValueChangeEvent<Date> event) {
		final Date old = seldate;
		seldate = event.getValue();
		changeSupport.firePropertyChange(PROPERTY_VALUE, old, seldate);
		fireChangeListeners();
	}
}
