/**
 * The Logic Lab
 * @author jpk
 * Mar 7, 2009
 */
package com.tll.client.validate;

import com.tll.client.ui.msg.MsgPopupRegistry;


/**
 * IPopupErrorHandler
 * @author jpk
 */
public interface IPopupErrorHandler extends IErrorHandler {

	/**
	 * @return the employed {@link MsgPopupRegistry}.
	 */
	MsgPopupRegistry getMsgPopupRegistry();
}
