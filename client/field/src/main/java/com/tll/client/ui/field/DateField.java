/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import java.util.Date;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
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

		/**
		 * Constructor
		 */
		public Impl() {
			super();
			getTextBox().addStyleName(Styles.TBOX);
			getTextBox().setVisibleLength(8);
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

		@Override
		public HandlerRegistration addFocusHandler(final FocusHandler handler) {
			return addDomHandler(handler, FocusEvent.getType());
		}
	}

	/**
	 * ToDateConverter
	 * @author jpk
	 */
	final class ToDateConverter implements IConverter<Date, Object> {

		@SuppressWarnings("synthetic-access")
		@Override
		public Date convert(final Object in) throws IllegalArgumentException {
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

	/**
	 * Constructor
	 * @param name
	 * @param propName
	 * @param labelText
	 * @param helpText
	 * @param format
	 */
	DateField(final String name, final String propName, final String labelText, final String helpText,
			final GlobalFormat format) {
		super(name, propName, labelText, helpText);
		dbox = new Impl();
		dbox.addValueChangeHandler(this);
		dbox.addFocusHandler(this);
		dbox.addBlurHandler(this);
		setFormat(format);
	}

	@Override
	public GlobalFormat getFormat() {
		return dateFormat;
	}

	@Override
	public void setFormat(final GlobalFormat format) {
		if(format != null && !format.isDateFormat()) throw new IllegalArgumentException();
		this.dateFormat = format == null ? GlobalFormat.DATE : format;
		dbox.setFormat(new DefaultFormat(Fmt.getDateTimeFormat(dateFormat)));
		// setConverter(new ToDateConverter());
	}

	@Override
	public IEditable<Date> getEditable() {
		return dbox;
	}

	@Override
	public String doGetText() {
		return dbox.getTextBox().getText();
	}

	@Override
	public void setText(final String text) {
		dbox.getTextBox().setText(text);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		dbox.setEnabled(enabled);
		super.setEnabled(enabled);
	}
}
