/**
 * The Logic Lab
 * @author jpk Nov 7, 2007
 */
package com.tll.client.ui.field;

import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.util.Fmt.DateFormat;
import com.tll.client.validate.DateValidator;

/**
 * DateField
 * @author jpk
 */
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
		DateFormat dateFormat = Fmt.getDateFormat(format);
		switch(dateFormat) {
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
	}

}
