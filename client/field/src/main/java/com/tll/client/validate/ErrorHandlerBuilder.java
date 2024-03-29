package com.tll.client.validate;

import com.tll.client.ui.field.FieldErrorHandler;
import com.tll.client.ui.msg.IMsgDisplay;
import com.tll.client.ui.msg.MsgPopupRegistry;

/**
 * ErrorHandlerBuilder - Builds {@link IErrorHandler} instances based on desired
 * modes of validation feedback.
 * @author jpk
 */
public abstract class ErrorHandlerBuilder {

	/**
	 * Assembles an appropriate {@link IErrorHandler} in the form of an
	 * {@link ErrorHandlerDelegate}.
	 * @param billboard include global (billboard) error display?
	 *        <em><code>msgDisplay</code> must be specified</em>
	 * @param field include local field error feedback?
	 * @param msgDisplay msg display implmentation. May be <code>null</code>.
	 *        providing field validation feedback
	 * @return A new {@link IErrorHandler} impl instance.
	 */
	public static ErrorHandlerDelegate build(final boolean billboard, final boolean field, final IMsgDisplay msgDisplay) {
		FieldErrorHandler feh = null;
		BillboardValidationFeedback bvf = null;

		if(billboard && msgDisplay != null) {
			bvf = new BillboardValidationFeedback(msgDisplay);
		}
		if(field) {
			feh = new FieldErrorHandler(new MsgPopupRegistry());
		}

		if(feh != null && bvf != null) {
			return new ErrorHandlerDelegate(bvf, feh);
		}

		if(feh != null) return new ErrorHandlerDelegate(feh);
		if(bvf != null) return new ErrorHandlerDelegate(bvf);

		throw new IllegalArgumentException("No error handelr built");
	}

}
