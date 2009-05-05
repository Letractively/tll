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

	@Override
	public final ErrorDisplay getDisplayType() {
		return ErrorDisplay.LOCAL;
	}

	@Override
	public MsgPopupRegistry getMsgPopupRegistry() {
		return mregistry;
	}

	@Override
	public void setMsgPopupRegistry(MsgPopupRegistry mregistry) {
		if(mregistry == null) throw new IllegalArgumentException("Null mregistry");
		this.mregistry = mregistry;
	}

	@Override
	public void handleError(IWidgetRef source, IError error) {
		// we only handle single type errors
		if(error.getType() == Type.SINGLE) {
			if(source == null) throw new IllegalArgumentException("Null source");
			final ErrorClassifier sourcing = error.getClassifier();
			mregistry.getOrCreateOperator(source.getWidget()).addMsgs(((Error) error).getMessages(),
					sourcing == null ? null : sourcing
							.hashCode());
		}
	}

	@Override
	public void resolveError(ErrorClassifier sourcing, IWidgetRef source) {
		if(sourcing == null) {
			mregistry.getOperator(source.getWidget(), false).clearMsgs();
		}
		else {
			mregistry.getOperator(source.getWidget(), false).removeMsgs(sourcing.hashCode());
		}
	}

	@Override
	public void clear(ErrorClassifier classifier) {
		mregistry.getAllOperator().removeMsgs(classifier.hashCode());
	}

	@Override
	public void clear() {
		mregistry.clear();
	}
}
