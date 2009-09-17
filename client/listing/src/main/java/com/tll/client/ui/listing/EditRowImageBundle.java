/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * EditRowImageBundle
 * @author jpk
 */
public interface EditRowImageBundle extends ImageBundle {

	/**
	 * pencil
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/pencil.gif")
	AbstractImagePrototype edit();

	/**
	 * trash
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/trash.gif")
	AbstractImagePrototype delete();
}
