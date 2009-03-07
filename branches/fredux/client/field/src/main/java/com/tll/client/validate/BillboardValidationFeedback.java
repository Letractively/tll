/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.validate;

import java.util.List;
import java.util.Map;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.validate.IError.Type;

/**
 * BillboardValidationFeedback
 * @author jpk
 */
public final class BillboardValidationFeedback implements IErrorHandler {

	private final GlobalMsgPanel msgPanel;

	/**
	 * Constructor
	 * @param msgPanel the panel to which validation messages are appended
	 */
	public BillboardValidationFeedback(GlobalMsgPanel msgPanel) {
		if(msgPanel == null) throw new IllegalArgumentException();
		this.msgPanel = msgPanel;
	}

	@Override
	public void handleError(IWidgetRef source, IError error) {
		if(error instanceof Errors) {
			final Map<IWidgetRef, List<IError>> map = ((Errors) error).getSourcedErrors();
			for(final IWidgetRef wref : map.keySet()) {
				final List<IError> errors = map.get(wref);
				for(final IError e : errors) {
					if(e.getType() == Type.SCALAR) {
						msgPanel.add(wref, ((ScalarError) e).getMessages());
					}
				}
			}
		}
		else if(error instanceof ScalarError) {
			msgPanel.add(source, ((ScalarError) error).getMessages());
		}
	}

	@Override
	public void resolveError(IWidgetRef source) {
		msgPanel.remove(source);
	}

	@Override
	public void clear() {
		msgPanel.clear();
	}
}
