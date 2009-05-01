/**
 * The Logic Lab
 * @author jpk
 * @since Mar 23, 2009
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Label;

/**
 * ViewA
 * @author jpk
 */
public class ViewA extends AbstractView<ViewAInit> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "ViewA";
		}

		@Override
		public ViewA newView() {
			return new ViewA();
		}
	}

	@Override
	protected void doInitialization(ViewAInit initializer) {
		addWidget(new Label("This is view A"));
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
		return "View A";
	}
}
