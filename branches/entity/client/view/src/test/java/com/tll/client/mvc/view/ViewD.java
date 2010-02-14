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
public class ViewD extends AbstractView<StaticViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "ViewD";
		}

		@Override
		public ViewD newView() {
			return new ViewD();
		}
	}

	@Override
	protected void doInitialization(StaticViewInitializer initializer) {
		addWidget(new Label("This is view D"));
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
		return "View D";
	}

}
