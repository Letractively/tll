/**
 * The Logic Lab
 * @author jpk Mar 2, 2009
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.msg.IHasMsgDisplay;
import com.tll.client.ui.msg.IMsgDisplay;

/**
 * BillboardValidationFeedback
 * @author jpk
 */
public final class BillboardValidationFeedback extends AbstractErrorHandler implements IHasMsgDisplay {

	private IMsgDisplay msgDisplayWidget;

	/**
	 * Constructor
	 * @param msgDisplayWidget the message display widget to which messages are
	 *        appended by way of this error handler.
	 */
	public BillboardValidationFeedback(final IMsgDisplay msgDisplayWidget) {
		setMsgDisplay(msgDisplayWidget);
	}

	@Override
	public ErrorDisplay getDisplayType() {
		return ErrorDisplay.GLOBAL;
	}

	@Override
	public IMsgDisplay getMsgDisplay() {
		return msgDisplayWidget;
	}

	@Override
	public void setMsgDisplay(final IMsgDisplay msgDisplay) {
		if(msgDisplay == null) throw new IllegalArgumentException("Null msg display");
		this.msgDisplayWidget = msgDisplay;
	}

	@Override
	protected void doHandleError(final Error error) {
		assert error != null;
		if(error.getTarget() == null) {
			msgDisplayWidget.add(error.getMessages(),
					error.getClassifier() == null ? null : Integer.valueOf(error.getClassifier().hashCode()));
		}
		else {
			msgDisplayWidget.add(error.getTarget(), error.getMessages(),
					error.getClassifier() == null ? null : Integer.valueOf(error.getClassifier().hashCode()));
		}
	}

	@Override
	protected void doResolveError(final IWidgetRef source, final ErrorClassifier classifier) {
		if(source == null) {
			msgDisplayWidget.removeUnsourced(classifier == null ? null : Integer.valueOf(classifier.hashCode()));
		}
		else {
			msgDisplayWidget.remove(source, classifier == null ? null : Integer.valueOf(classifier.hashCode()));
		}
	}

	@Override
	public void clear(final ErrorClassifier classifier) {
		msgDisplayWidget.remove(classifier.hashCode());
	}

	@Override
	public void clear() {
		msgDisplayWidget.clear();
	}
}
