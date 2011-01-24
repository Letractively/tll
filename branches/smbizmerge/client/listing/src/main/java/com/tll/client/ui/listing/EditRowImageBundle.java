/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * EditRowImageBundle
 * @author jpk
 */
public interface EditRowImageBundle extends ClientBundle {

	/**
	 * pencil
	 * @return the image prototype
	 */
	@Source(value = "pencil.gif")
	ImageResource edit();

	/**
	 * trash
	 * @return the image prototype
	 */
	@Source(value = "trash.gif")
	ImageResource delete();
}
