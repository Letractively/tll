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
	 * "Throbber" image usu. used when an rpc call is invoked.
	 */
	// AbstractImagePrototype throbber();
	// NOTE: animated gifs don't work with ImageBundles!!
	/**
	 * add (16x16)
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/add.gif")
	AbstractImagePrototype add();

	/**
	 * delete (18x18)
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/delete.gif")
	AbstractImagePrototype delete();

	/**
	 * undo (18x18)
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/undo.gif")
	AbstractImagePrototype undo();

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

	/**
	 * info
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/info.gif")
	AbstractImagePrototype info();

	/**
	 * flderr
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/flderr.gif")
	AbstractImagePrototype flderr();

	/**
	 * fldwrn
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/fldwrn.gif")
	AbstractImagePrototype fldwrn();

	/**
	 * pencil
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/pencil.gif")
	AbstractImagePrototype pencil();

	/**
	 * trash
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/trash.gif")
	AbstractImagePrototype trash();

	/**
	 * warn
	 * @return the image prototype
	 */
	@Resource(value = "com/tll/public/images/warn.gif")
	AbstractImagePrototype warn();

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
