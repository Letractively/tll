/**
 * The Logic Lab
 * @author jpk
 * @since Mar 23, 2009
 */
package com.tll.client.view;

import com.google.gwt.user.client.ui.Label;
import com.tll.client.view.AbstractView;
import com.tll.client.view.ViewClass;

/**
 * ViewB
 * @author jpk
 */
public class ViewB extends AbstractView<ViewBInit> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "ViewB";
		}

		@Override
		public ViewB newView() {
			return new ViewB();
		}
	}

	@Override
	protected void doInitialization(ViewBInit initializer) {
		addWidget(new Label("This is view B"));
	}

	@Override
	protected void doDestroy() {
	}

	@Override
	public ViewClass getViewClass() {
		return klas;
	}

	@Override
	public String getLongViewName() {
		return "View B";
	}

}
