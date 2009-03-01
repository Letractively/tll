/**
 * The Logic Lab
 * @author jpk Dec 30, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.Position;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.ValidationException;
import com.tll.common.msg.Msg;

/**
 * FieldValidationFeedback
 * @author jpk
 */
public final class FieldValidationFeedback implements IValidationFeedback {

	private final MsgPopupRegistry mregistry;

	/**
	 * Constructor
	 * @param mregistry Registry that is responsible for the life-cycle of popup
	 *        messages.
	 */
	public FieldValidationFeedback(MsgPopupRegistry mregistry) {
		if(mregistry == null) throw new IllegalArgumentException();
		this.mregistry = mregistry;
	}

	public void handleException(Object source, ValidationException exception) {
		((Widget) source).removeStyleName(IField.Styles.DIRTY);
		((Widget) source).addStyleName(IField.Styles.INVALID);
		if(exception.getErrors() != null) {
			for(final Msg m : exception.getErrors()) {
				mregistry.addMsg(m, (Widget) source, false).showMsgs(Position.BOTTOM, -1, true);
			}
		}
	}

	public void resolve(Object source) {
		((Widget) source).removeStyleName(IField.Styles.INVALID);
		mregistry.getOperator((Widget) source, false).clearMsgs();
	}
}
