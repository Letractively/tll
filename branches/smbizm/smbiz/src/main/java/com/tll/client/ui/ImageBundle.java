/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * ImageBundle
 * @author jpk
 */
public interface ImageBundle extends com.google.gwt.user.client.ui.ImageBundle {

	/**
	 * arrow_sm_down
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/arrow_sm_down.gif")
	AbstractImagePrototype arrow_sm_down();
}
