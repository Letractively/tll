package com.tll.client.ui.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

/**
 * ToolbarImageBundle - Images for {@link Toolbar} related UI artifacts.
 * @author jpk
 */
public interface ToolbarImageBundle extends ImageBundle {

	/**
	 * The message level image bundle instance.
	 */
	static final ToolbarImageBundle INSTANCE = (ToolbarImageBundle) GWT.create(ToolbarImageBundle.class);

	/**
	 * split
	 * @return the split prototype
	 */
	@Resource(value = "com/tll/public/images/split.gif")
	AbstractImagePrototype split();
}