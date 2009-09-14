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
public class PopupValidationFeedback extends AbstractErrorHandler implements IHasMsgPopupRegistry {

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
	protected void doHandleError(IWidgetRef source, IError error) {
		// we only handle sourced and single type errors
		if(error.getType() == Type.SINGLE && source != null) {
			final ErrorClassifier sourcing = error.getClassifier();
			mregistry.getOrCreateOperator(source.getWidget()).addMsgs(((Error) error).getMessages(),
					sourcing == null ? null : sourcing
							.hashCode());
		}
	}

	@Override
	protected void doResolveError(IWidgetRef source, ErrorClassifier classifier) {
		if(classifier == null) {
			mregistry.getOperator(source.getWidget(), false).clearMsgs();
		}
		else {
			mregistry.getOperator(source.getWidget(), false).removeMsgs(classifier.hashCode());
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
