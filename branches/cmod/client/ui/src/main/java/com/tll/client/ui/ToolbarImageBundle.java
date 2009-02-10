/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * ToolbarImageBundle
 * @author jpk
 */
public interface ToolbarImageBundle extends ImageBundle {

	/**
	 * split
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/split.gif")
	AbstractImagePrototype split();

	/**
	 * refresh
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/refresh.gif")
	AbstractImagePrototype refresh();
}
