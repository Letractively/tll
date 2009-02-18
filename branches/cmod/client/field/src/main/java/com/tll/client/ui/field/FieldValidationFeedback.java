/**
 * The Logic Lab
 * @author jpk
 * Dec 30, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.Position;
import com.tll.client.ui.msg.MsgManager;
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
		MsgManager.get().post(exception.getErrors(), (Widget) source, true).show(Position.BOTTOM, -1);
		((Widget) source).removeStyleName(IField.Styles.DIRTY);
		((Widget) source).addStyleName(IField.Styles.INVALID);
	}

	public void resolve(Object source) {
		MsgManager.get().clear((Widget) source, false);
		((Widget) source).removeStyleName(IField.Styles.INVALID);
	}
}
