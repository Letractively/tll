/**
 * The Logic Lab
 * @author jpk
 * Mar 14, 2008
 */
package com.tll.client.mvc.view;

import com.tll.client.App;
import com.tll.common.model.IEntityType;
import com.tll.common.model.SmbizEntityType;

/**
 * MainView - The root view base class for all account types.
 * @author jpk
 */
public abstract class MainView extends AbstractView<StaticViewInitializer> {

	/**
	 * MainViewClass - Base class for all main view classes.
	 * @author jpk
	 */
	public static abstract class MainViewClass extends ViewClass {

		private static final ViewOptions VIEW_OPTIONS = new ViewOptions(false, false, false, false, false);

		private final String name;

		/**
		 * Provides the view class for the root view based on the account type.
		 * @param accountType
		 * @return The ViewClass of the appropriate main view.
		 */
		public static ViewClass getMainViewClass(IEntityType accountType) {
			return ViewClass.findClassByViewName(getMainViewName(accountType));
		}

		private static String getMainViewName(IEntityType accountType) {
			final SmbizEntityType set = App.smbizEntityType(accountType);
			return set.getValue() + "_MAIN";
		}

		/**
		 * Constructor
		 * @param accountType
		 */
		protected MainViewClass(IEntityType accountType) {
			this.name = getMainViewName(accountType);
		}

		@Override
		public final String getName() {
			return name;
		}

		@Override
		public final ViewOptions getViewOptions() {
			return VIEW_OPTIONS;
		}
	}

	public final void refresh() {
		// base impl no-op
	}

	@Override
	public final void doInitialization(StaticViewInitializer init) {
		// no-op
	}

	@Override
	protected void doDestroy() {
		// no-op
	}
}
