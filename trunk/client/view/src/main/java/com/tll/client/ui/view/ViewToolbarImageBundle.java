package com.tll.client.ui.view;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * ViewToolbarImageBundle
 * @author jpk
 */
public interface ViewToolbarImageBundle extends ImageBundle {

	/**
	 * arrow_sm_right
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/arrow_sm_right.gif")
	AbstractImagePrototype arrow_sm_right();

	/**
	 * arrow_sm_down
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/arrow_sm_down.gif")
	AbstractImagePrototype arrow_sm_down();

	/**
	 * close
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/close.gif")
	AbstractImagePrototype close();

	/**
	 * external (11x11)
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/external.gif")
	AbstractImagePrototype external();

	/**
	 * permalink (11x11)
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/permalink.gif")
	AbstractImagePrototype permalink();

	/**
	 * refresh
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/refresh.gif")
	AbstractImagePrototype refresh();
}