/**
 * The Logic Lab
 * @author jpk
 * Dec 30, 2008
 */
package com.tll.client.validate;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.TimedPositionedPopup.Position;

/**
 * ValidationFeedbackManager
 * @author jpk
 */
public final class ValidationFeedbackManager implements IValidationFeedback {

	private static ValidationFeedbackManager instance;

	public static ValidationFeedbackManager instance() {
		if(instance == null) {
			instance = new ValidationFeedbackManager();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	private ValidationFeedbackManager() {
	}

	public void handleException(Object source, ValidationException exception) {
		MsgManager.instance().post(false, exception.getErrors(), Position.BOTTOM, (Widget) source, -1, true).show();
	}

	public void resolve(Object source) {
		MsgManager.instance().clear((Widget) source, false);
	}
}
