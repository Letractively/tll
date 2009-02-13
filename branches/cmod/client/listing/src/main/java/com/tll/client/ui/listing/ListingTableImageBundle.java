/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * ListingTableImageBundle
 * @author jpk
 */
public interface ListingTableImageBundle extends ImageBundle {

	/**
	 * sort_asc
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/sort_asc.gif")
	AbstractImagePrototype sort_asc();

	/**
	 * sort_desc
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/sort_desc.gif")
	AbstractImagePrototype sort_desc();
}
