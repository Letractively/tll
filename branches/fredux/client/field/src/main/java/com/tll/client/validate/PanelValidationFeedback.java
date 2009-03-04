/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;
import com.tll.client.ui.msg.GlobalMsgPanel;

/**
 * PanelValidationFeedback
 * @param <T> the source type
 * @author jpk
 */
public class PanelValidationFeedback<T extends IWidgetRef> implements IValidationFeedback<T> {

	final GlobalMsgPanel msgPanel;

	/**
	 * Constructor
	 * @param panel the panel to which validation messages are appended
	 */
	private PanelValidationFeedback(GlobalMsgPanel msgPanel) {
		super();
		this.msgPanel = msgPanel;
	}

	@Override
	public void handleException(T source, ValidationException exception) {
		msgPanel.add(source, exception.getErrors());
	}

	@Override
	public void resolve(T source) {
		msgPanel.remove(source);
	}
}
