/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.tll.common.msg.Msg;

/**
 * MsgImageBundle - Images for {@link Msg} related UI artifacts.
 * @author jpk
 */
public interface MsgImageBundle extends com.google.gwt.user.client.ui.ImageBundle {

	/**
	 * info
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/info.gif")
	AbstractImagePrototype info();

	/**
	 * warn
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/warn.gif")
	AbstractImagePrototype warn();
	
	/**
	 * error
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/error.gif")
	AbstractImagePrototype error();

	/**
	 * fatal
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/fatal.gif")
	AbstractImagePrototype fatal();
}
