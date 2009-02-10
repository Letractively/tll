/**
 * The Logic Lab
 * @author jpk
 * Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * ListingNavBarImageBundle
 * @author jpk
 */
public interface ListingNavBarImageBundle extends com.google.gwt.user.client.ui.ImageBundle {

	/**
	 * page_first
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-first.gif")
	AbstractImagePrototype page_first();

	/**
	 * page_first_disabled
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-first-disabled.gif")
	AbstractImagePrototype page_first_disabled();

	/**
	 * page_last
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-last.gif")
	AbstractImagePrototype page_last();

	/**
	 * page_last_disabled
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-last-disabled.gif")
	AbstractImagePrototype page_last_disabled();

	/**
	 * page_next
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-next.gif")
	AbstractImagePrototype page_next();

	/**
	 * page_next_disabled
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-next-disabled.gif")
	AbstractImagePrototype page_next_disabled();

	/**
	 * page_prev
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-prev.gif")
	AbstractImagePrototype page_prev();

	/**
	 * page_prev_disabled
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/page-prev-disabled.gif")
	AbstractImagePrototype page_prev_disabled();
}
