/**
 * The Logic Lab
 * @author jpk Mar 2, 2009
 */
package com.tll.client.validate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.msg.IHasMsgDisplay;
import com.tll.client.ui.msg.IMsgDisplay;
import com.tll.client.validate.IError.Type;

/**
 * BillboardValidationFeedback
 * @author jpk
 */
public final class BillboardValidationFeedback implements IErrorHandler, IHasMsgDisplay {

	private IMsgDisplay msgDisplayWidget;

	/**
	 * Constructor
	 * @param msgDisplayWidget the message display widget to which messages are
	 *        appended by way of this error handler.
	 */
	public BillboardValidationFeedback(IMsgDisplay msgDisplayWidget) {
		setMsgDisplay(msgDisplayWidget);
	}
	
	@Override
	public IMsgDisplay getMsgDisplay() {
		return msgDisplayWidget;
	}

	@Override
	public void setMsgDisplay(IMsgDisplay msgDisplay) {
		if(msgDisplay == null) throw new IllegalArgumentException();
		this.msgDisplayWidget = msgDisplay;
	}

	/**
	 * Posts sourced errors.
	 * @param wref
	 * @param errors
	 */
	private void post(IWidgetRef wref, Collection<IError> errors) {
		assert errors != null;
		for(final IError e : errors) {
			post(wref, e);
		}
	}

	/**
	 * Posts a single sourced error.
	 * @param wref
	 * @param error
	 */
	private void post(IWidgetRef wref, IError error) {
		assert error != null;
		if(error.getType() != Type.SINGLE)
			throw new IllegalArgumentException("Nested composite errors are not supported.");
		msgDisplayWidget.add(wref, ((Error) error).getMessages());
	}

	/**
	 * Posts unsourced errors.
	 * @param errors
	 */
	private void post(Collection<IError> errors) {
		assert errors != null;
		for(final IError e : errors) {
			post(e);
		}
	}

	/**
	 * Posts a single unsourced error.
	 * @param error
	 */
	private void post(IError error) {
		assert error != null;
		if(error.getType() != Type.SINGLE)
			throw new IllegalArgumentException("Nested composite errors are not supported.");
		msgDisplayWidget.add(((Error) error).getMessages());
	}

	@Override
	public void handleError(IWidgetRef source, IError error, int attribs) {
		// we handle only global errors either sourced or unsourced
		if(Attrib.isGlobal(attribs)) {
			if(error.getType() == Type.COMPOSITE) {
				// unsourced
				final List<IError> unsourced = ((Errors) error).getUnsourcedErrors();
				if(unsourced != null) {
					post(unsourced);
				}

				// sourced
				final Map<IWidgetRef, List<IError>> map = ((Errors) error).getSourcedErrors();
				if(map != null) {
					for(final IWidgetRef wref : map.keySet()) {
						post(wref, map.get(wref));
					}
				}
			}
			else if(error.getType() == Type.SINGLE) {
				post(source, error);
			}
			else {
				throw new IllegalStateException("Unhandled error type: " + error.getType());
			}
		}
	}

	@Override
	public void resolveError(IWidgetRef source) {
		msgDisplayWidget.remove(source);
	}

	@Override
	public void clear() {
		msgDisplayWidget.clear();
	}
}
