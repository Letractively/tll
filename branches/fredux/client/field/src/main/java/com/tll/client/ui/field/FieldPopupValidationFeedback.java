/**
 * The Logic Lab
 * @author jpk Dec 30, 2008
 */
package com.tll.client.ui.field;

import com.tll.client.ui.Position;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.ValidationException;
import com.tll.common.msg.Msg;

/**
 * FieldPopupValidationFeedback
 * @author jpk
 */
public final class FieldPopupValidationFeedback implements IValidationFeedback<IField> {

	private final MsgPopupRegistry mregistry;

	/**
	 * Constructor
	 * @param mregistry Registry that is responsible for the life-cycle of popup
	 *        messages.
	 */
	public FieldPopupValidationFeedback(MsgPopupRegistry mregistry) {
		if(mregistry == null) throw new IllegalArgumentException();
		this.mregistry = mregistry;
	}

	public void handleException(IField field, ValidationException exception) {
		field.getWidget().removeStyleName(IField.Styles.DIRTY);
		field.getWidget().addStyleName(IField.Styles.INVALID);
		if(exception.getErrors() != null) {
			for(final Msg m : exception.getErrors()) {
				mregistry.addMsg(m, field.getWidget(), false).showMsgs(Position.BOTTOM, -1, true);
			}
		}
	}

	public void resolve(IField field) {
		field.getWidget().removeStyleName(IField.Styles.INVALID);
		mregistry.getOperator(field.getWidget(), false).clearMsgs();
	}
}
