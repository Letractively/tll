package com.tll.client.ui.msg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;
import com.tll.common.msg.Msg;

/**
 * MsgLevelImageBundle - Images for {@link Msg} related UI artifacts.
 * @author jpk
 */
public interface MsgLevelImageBundle extends ImageBundle {

	/**
	 * The message level image bundle instance.
	 */
	static final MsgLevelImageBundle INSTANCE = (MsgLevelImageBundle) GWT.create(MsgLevelImageBundle.class);

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