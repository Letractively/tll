/**
 * The Logic Lab
 * @author jpk
 * Feb 26, 2009
 */
package com.tll.client.ui;

import java.util.Collection;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * IWidgetRenderer - renders widgets.
 * @author jpk
 */
public interface IWidgetRenderer {

	/**
	 * Renders a collection of like widgets producing a panel that contains them.
	 * @param widgetCollection widget collection to render
	 * @return newly created panel that contains the rendered widgets.
	 */
	Panel render(Collection<? extends Widget> widgetCollection);
}
