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
public class ViewC extends AbstractView<StaticViewRequest> {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		/**
		 * Constructor
		 */
		public Class() {
			super("ViewC");
		}

		@Override
		public ViewC newView() {
			return new ViewC();
		}
	}

	@Override
	protected void doInitialization(StaticViewRequest viewRequest) {
		addWidget(new Label("This is view C"));
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
		return "View C";
	}

}
