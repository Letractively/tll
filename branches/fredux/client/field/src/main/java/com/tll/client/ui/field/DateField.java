/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.ValidationException;

/**
 * DateField
 * @author jpk
 */
public class DateField extends AbstractField<Date> implements IHasFormat {
	
	/**
	 * Impl
	 * @author jpk
	 */
	static final class Impl extends DateBox implements IEditable<Date> {

	}
	
	/**
	 * ToDateConverter
	 * @author jpk
	 */
	final class ToDateConverter implements IConverter<Date, Object> {

		@SuppressWarnings("synthetic-access")
		@Override
		public Date convert(Object in) throws IllegalArgumentException {
			try {
				return (Date) DateValidator.get(dateFormat).validate(in);
			}
			catch(final ValidationException e) {
				throw new IllegalArgumentException(e);
			}
		}

	}

	/**
	 * The date display format.
	 */
	private GlobalFormat dateFormat;

	/**
	 * The target date box.
	 */
	private final Impl dbox;
	
	private final ToDateConverter converter;

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param format
	 */
	DateField(String name, String propName, String labelText, String helpText, GlobalFormat format) {
		super(name, propName, labelText, helpText);
		dbox = new Impl();
		dbox.addValueChangeHandler(this);
		setFormat(format);
		converter = new ToDateConverter();
	}

	@Override
	protected IConverter<Date, Object> getConverter() {
		return converter;
	}

	public GlobalFormat getFormat() {
		return dateFormat;
	}

	public void setFormat(GlobalFormat format) {
		if(format != null && !format.isDateFormat()) throw new IllegalArgumentException();
		this.dateFormat = format == null ? GlobalFormat.DATE : format;
		dbox.setFormat(new DefaultFormat(Fmt.getDateTimeFormat(dateFormat)));
	}

	@Override
	protected IEditable<Date> getEditable() {
		return dbox;
	}

	public String getText() {
		return dbox.getTextBox().getText();
	}

	public void setText(String text) {
		dbox.getTextBox().setText(text);
	}
}
