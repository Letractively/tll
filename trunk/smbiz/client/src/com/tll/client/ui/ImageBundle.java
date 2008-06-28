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
	 */
	@Resource(value = "com/tll/public/images/external.gif")
	AbstractImagePrototype external();

	/**
	 * permalink (11x11)
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
	 */
	@Resource(value = "com/tll/public/images/add.gif")
	AbstractImagePrototype add();

	/**
	 * delete (18x18)
	 */
	@Resource(value = "com/tll/public/images/delete.gif")
	AbstractImagePrototype delete();

	/**
	 * undo (18x18)
	 */
	@Resource(value = "com/tll/public/images/undo.gif")
	AbstractImagePrototype undo();

	/**
	 * split
	 */
	@Resource(value = "com/tll/public/images/split.gif")
	AbstractImagePrototype split();

	/**
	 * refresh
	 */
	@Resource(value = "com/tll/public/images/refresh.gif")
	AbstractImagePrototype refresh();

	/**
	 * sort_asc
	 */
	@Resource(value = "com/tll/public/images/sort_asc.gif")
	AbstractImagePrototype sort_asc();

	/**
	 * sort_desc
	 */
	@Resource(value = "com/tll/public/images/sort_desc.gif")
	AbstractImagePrototype sort_desc();

	/**
	 * arrow_sm_right
	 */
	@Resource(value = "com/tll/public/images/arrow_sm_right.gif")
	AbstractImagePrototype arrow_sm_right();

	/**
	 * arrow_sm_down
	 */
	@Resource(value = "com/tll/public/images/arrow_sm_down.gif")
	AbstractImagePrototype arrow_sm_down();

	/**
	 * close
	 */
	@Resource(value = "com/tll/public/images/close.gif")
	AbstractImagePrototype close();

	/**
	 * error
	 */
	@Resource(value = "com/tll/public/images/error.gif")
	AbstractImagePrototype error();

	/**
	 * fatal
	 */
	@Resource(value = "com/tll/public/images/fatal.gif")
	AbstractImagePrototype fatal();

	/**
	 * info
	 */
	@Resource(value = "com/tll/public/images/info.gif")
	AbstractImagePrototype info();

	/**
	 * flderr
	 */
	@Resource(value = "com/tll/public/images/flderr.gif")
	AbstractImagePrototype flderr();

	/**
	 * fldwrn
	 */
	@Resource(value = "com/tll/public/images/fldwrn.gif")
	AbstractImagePrototype fldwrn();

	/**
	 * pencil
	 */
	@Resource(value = "com/tll/public/images/pencil.gif")
	AbstractImagePrototype pencil();

	/**
	 * trash
	 */
	@Resource(value = "com/tll/public/images/trash.gif")
	AbstractImagePrototype trash();

	/**
	 * warn
	 */
	@Resource(value = "com/tll/public/images/warn.gif")
	AbstractImagePrototype warn();

	/**
	 * page_first
	 */
	@Resource(value = "com/tll/public/images/page-first.gif")
	AbstractImagePrototype page_first();

	/**
	 * page_first_disabled
	 */
	@Resource(value = "com/tll/public/images/page-first-disabled.gif")
	AbstractImagePrototype page_first_disabled();

	/**
	 * page_last
	 */
	@Resource(value = "com/tll/public/images/page-last.gif")
	AbstractImagePrototype page_last();

	/**
	 * page_last_disabled
	 */
	@Resource(value = "com/tll/public/images/page-last-disabled.gif")
	AbstractImagePrototype page_last_disabled();

	/**
	 * page_next
	 */
	@Resource(value = "com/tll/public/images/page-next.gif")
	AbstractImagePrototype page_next();

	/**
	 * page_next_disabled
	 */
	@Resource(value = "com/tll/public/images/page-next-disabled.gif")
	AbstractImagePrototype page_next_disabled();

	/**
	 * page_prev
	 */
	@Resource(value = "com/tll/public/images/page-prev.gif")
	AbstractImagePrototype page_prev();

	/**
	 * page_prev_disabled
	 */
	@Resource(value = "com/tll/public/images/page-prev-disabled.gif")
	AbstractImagePrototype page_prev_disabled();
}
