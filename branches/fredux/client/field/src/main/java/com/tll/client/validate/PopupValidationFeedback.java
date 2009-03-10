/**
 * The Logic Lab
 * @author jpk Dec 30, 2008
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.msg.IHasMsgPopupRegistry;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.IError.Type;

/**
 * PopupValidationFeedback - Provides "local" validation feedback via popup
 * messages.
 * @author jpk
 */
public class PopupValidationFeedback implements IErrorHandler, IHasMsgPopupRegistry {

	protected MsgPopupRegistry mregistry;

	/**
	 * Constructor
	 * @param mregistry The required message popup registry
	 */
	public PopupValidationFeedback(MsgPopupRegistry mregistry) {
		setMsgPopupRegistry(mregistry);
	}

	public MsgPopupRegistry getMsgPopupRegistry() {
		return mregistry;
	}

	public void setMsgPopupRegistry(MsgPopupRegistry mregistry) {
		if(mregistry == null) throw new IllegalArgumentException();
		this.mregistry = mregistry;
	}

	public void handleError(IWidgetRef source, IError error, int attribs) {
		// we only handle single local errors
		if(error.getType() == Type.SINGLE && Attrib.isLocal(attribs)) {
			mregistry.addMsgs(((Error) error).getMessages(), source.getWidget(), true);
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
