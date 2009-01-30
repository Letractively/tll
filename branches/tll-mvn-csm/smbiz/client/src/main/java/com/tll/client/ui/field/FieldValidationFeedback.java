/**
 * The Logic Lab
 * @author jpk
 * Dec 30, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.ValidationException;

/**
 * FieldValidationFeedback
 * @author jpk
 */
public final class FieldValidationFeedback implements IValidationFeedback {

	private static FieldValidationFeedback instance;

	public static FieldValidationFeedback instance() {
		if(instance == null) {
			instance = new FieldValidationFeedback();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	private FieldValidationFeedback() {
	}

	public void handleException(Object source, ValidationException exception) {
		MsgManager.instance().post(false, exception.getErrors(), Position.BOTTOM, (Widget) source, -1, true).show();
		((Widget) source).removeStyleName(IField.Styles.DIRTY);
		((Widget) source).addStyleName(IField.Styles.INVALID);
	}

	public void resolve(Object source) {
		MsgManager.instance().clear((Widget) source, false);
		((Widget) source).removeStyleName(IField.Styles.INVALID);
	}
}
