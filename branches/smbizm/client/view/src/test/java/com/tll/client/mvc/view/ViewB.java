/**
 * The Logic Lab
 * @author jpk
 * @since Mar 23, 2009
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Label;

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
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	public void refresh() {
	}

	@Override
	public String getLongViewName() {
		return "View B";
	}

}
