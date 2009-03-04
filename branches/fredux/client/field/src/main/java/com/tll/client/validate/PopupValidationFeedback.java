/**
 * The Logic Lab
 * @author jpk Dec 30, 2008
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetProvider;
import com.tll.client.ui.Position;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.msg.Msg;

/**
 * PopupValidationFeedback - Provides "local" validation feedback via popup
 * messages.
 * @param <T> the source type
 * @author jpk
 */
public final class PopupValidationFeedback<T extends IWidgetProvider> implements IValidationFeedback<T> {

	private final MsgPopupRegistry mregistry;

	/**
	 * Constructor
	 * @param mregistry Registry that is responsible for the life-cycle of popup
	 *        messages.
	 */
	public PopupValidationFeedback(MsgPopupRegistry mregistry) {
		if(mregistry == null) throw new IllegalArgumentException();
		this.mregistry = mregistry;
	}

	public void handleException(T widgetProvider, ValidationException exception) {
		widgetProvider.getWidget().removeStyleName(IField.Styles.DIRTY);
		widgetProvider.getWidget().addStyleName(IField.Styles.INVALID);
		if(exception.getErrors() != null) {
			for(final Msg m : exception.getErrors()) {
				mregistry.addMsg(m, widgetProvider.getWidget(), false).showMsgs(Position.BOTTOM, -1, true);
			}
		}
	}

	public void resolve(T widgetProvider) {
		widgetProvider.getWidget().removeStyleName(IField.Styles.INVALID);
		mregistry.getOperator(widgetProvider.getWidget(), false).clearMsgs();
	}
}
