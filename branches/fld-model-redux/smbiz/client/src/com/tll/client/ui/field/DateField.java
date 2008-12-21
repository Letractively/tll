/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import com.tll.client.util.GlobalFormat;

/**
 * DateField
 * @author jpk
 */
// TODO add popup calendar to this field widget. This is what will distinguish
// it from a TextField.
public class DateField extends TextField {

	/**
	 * Constructor
	 * @param propName
	 * @param lblText
	 * @param format
	 */
	public DateField(String propName, String lblText, GlobalFormat format) {
		super(propName, lblText, 10);
		setFormat(format);
		// NOTE: we *don't* set a validator here. field validators are managed by
		// IFieldBinding instances.
		/*
		switch(format) {
			case DATE:
				addValidator(DateValidator.DATE_VALIDATOR);
				break;
			case TIME:
				addValidator(DateValidator.TIME_VALIDATOR);
				break;
			default:
			case TIMESTAMP:
				addValidator(DateValidator.TIMESTAMP_VALIDATOR);
				break;
		}
		*/
	}

}
