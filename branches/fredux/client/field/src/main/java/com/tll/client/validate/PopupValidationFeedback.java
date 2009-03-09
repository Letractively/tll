/**
 * The Logic Lab
 * @author jpk Dec 30, 2008
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.IError.Type;

/**
 * PopupValidationFeedback - Provides "local" validation feedback via popup
 * messages.
 * @author jpk
 */
public class PopupValidationFeedback implements IPopupErrorHandler {

	protected final MsgPopupRegistry mregistry;

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

	public void handleError(IWidgetRef source, IError error) {
		// we only handle scalar errors
		if(error.getType() == Type.SCALAR) {
			mregistry.addMsgs(((ScalarError) error).getMessages(), source.getWidget(), true);
		}
	}

	public void resolveError(IWidgetRef source) {
		mregistry.getOperator(source.getWidget(), false).clearMsgs();
	}
	
	@Override
	public void clear() {
		mregistry.clear();
	}
}
