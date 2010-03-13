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
public class ViewE extends AbstractView<StaticViewInitializer> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		@Override
		public String getName() {
			return "ViewE";
		}

		@Override
		public ViewE newView() {
			return new ViewE();
		}
	}

	@Override
	protected void doInitialization(StaticViewInitializer initializer) {
		addWidget(new Label("This is view E"));
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
		return "View E";
	}

}
