/**
 * The Logic Lab
 * @author jpk
 * Mar 14, 2008
 */
package com.tll.client.admin.mvc.view;

import com.tll.client.event.type.ShowViewRequest;
import com.tll.client.event.type.StaticViewRequest;
import com.tll.client.event.type.ViewRequestEvent;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.model.EntityType;

/**
 * MainView - The root view base class for all account types.
 * @author jpk
 */
public abstract class MainView extends AbstractView {

	/**
	 * MainViewClass - Base class for all main view classes.
	 * @author jpk
	 */
	public static abstract class MainViewClass extends ViewClass {

		private static final ViewOptions viewOptions = new ViewOptions(false, false, false, false, false);

		/**
		 * Provides the view class for the root view based on the account type.
		 * @param accountType
		 * @return The ViewClass of the appropriate main view.
		 */
		public static ViewClass getMainViewClass(EntityType accountType) {
			return ViewClass.findClassByViewName(getMainViewName(accountType));
		}

		private static String getMainViewName(EntityType accountType) {
			return accountType.name() + "_MAIN";
		}

		protected MainViewClass(EntityType accountType) {
			super(getMainViewName(accountType));
		}

		@Override
		public final ViewOptions getViewOptions() {
			return viewOptions;
		}
	}

	public final void refresh() {
		// base impl no-op
	}

	@Override
	public final void doInitialization(ViewRequestEvent viewRequest) {
		// no-op
	}

	@Override
	protected void doDestroy() {
		// no-op
	}

	@Override
	public final ShowViewRequest newViewRequest() {
		return new StaticViewRequest(this, getViewClass());
	}
}
