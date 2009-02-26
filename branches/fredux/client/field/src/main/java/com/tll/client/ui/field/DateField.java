/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;

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
	 * The date display format.
	 */
	private GlobalFormat dateFormat;

	/**
	 * The target date box.
	 */
	private final Impl dbox;
	
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
		setFormat(format);
		dbox = new Impl();
		dbox.addValueChangeHandler(this);
		dbox.setFormat(new DefaultFormat(Fmt.getDateTimeFormat(dateFormat)));
	}

	public GlobalFormat getFormat() {
		return dateFormat;
	}

	public void setFormat(GlobalFormat format) {
		if(dateFormat == null || !dateFormat.isDateFormat()) throw new IllegalArgumentException();
		this.dateFormat = format;
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
