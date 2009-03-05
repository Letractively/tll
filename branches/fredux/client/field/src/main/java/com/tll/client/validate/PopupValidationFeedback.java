/**
 * The Logic Lab
 * @author jpk Dec 30, 2008
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetProvider;
import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.Position;
import com.tll.client.ui.msg.IMsgOperator;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.IError.Type;
import com.tll.common.msg.Msg;

/**
 * PopupValidationFeedback - Provides "local" validation feedback via popup
 * messages.
 * @author jpk
 */
public final class PopupValidationFeedback implements IValidationFeedback {

	private final MsgPopupRegistry mregistry;

	/**
	 * Constructor
	 * @param mregistry The required message popup registry
	 */
	public PopupValidationFeedback(MsgPopupRegistry mregistry) {
		if(mregistry == null) throw new IllegalArgumentException();
		this.mregistry = mregistry;
	}

	public MsgPopupRegistry getMsgPopupRegistry() {
		return mregistry;
	}

	public void handleError(IWidgetRef widgetProvider, IError error) {
		// we only handle scalar errors
		if(error.getType() == Type.SCALAR) {
			for(final Msg m : ((ScalarError) error).getMessages()) {
				mregistry.addMsg(m, widgetProvider.getWidget(), false).showMsgs(Position.BOTTOM, -1, true);
			}
		}
	}

	public void resolveError(IWidgetRef widgetProvider) {
		mregistry.getOperator(widgetProvider.getWidget(), false).clearMsgs();
	}
	
	/**
	 * Specific to popup validation feedback, this method turns on or off the
	 * display of the popup validation messages.
	 * @param widgetProvider
	 */
	public void toggleNotification(IWidgetProvider widgetProvider) {
		final IMsgOperator op = mregistry.getOperator(widgetProvider.getWidget(), false);
		op.showMsgs(op.isShowing());
	}
}
