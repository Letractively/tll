/**
 * The Logic Lab
 * @author jpk
 * Mar 3, 2009
 */
package com.tll.client.ui.msg;

import com.google.gwt.user.client.ui.Image;
import com.tll.common.msg.Msg.MsgLevel;


/**
 * Util
 * @author jpk
 */
abstract class Util {

	/**
	 * Provides a new {@link Image} containing the associated msg level icon.
	 * @param level The message level
	 * @return Image
	 */
	public static Image getMsgLevelImage(MsgLevel level) {
		switch(level) {
			case WARN:
				return MsgLevelImageBundle.INSTANCE.warn().createImage();
			case ERROR:
				return MsgLevelImageBundle.INSTANCE.error().createImage();
			case FATAL:
				return MsgLevelImageBundle.INSTANCE.fatal().createImage();
			default:
			case INFO:
				return MsgLevelImageBundle.INSTANCE.info().createImage();
		}
	}
}
