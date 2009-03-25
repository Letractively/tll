/**
 * The Logic Lab
 * @author jpk
 * Mar 14, 2008
 */
package com.tll.client.mvc.view;

import com.tll.common.model.IEntityType;
import com.tll.model.SmbizEntityType;

/**
 * MainView - The root view base class for all account types.
 * @author jpk
 */
public abstract class MainView extends AbstractView<IViewKeyProvider> {

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
		public static ViewClass getMainViewClass(IEntityType accountType) {
			return ViewClass.findClassByViewName(getMainViewName(accountType));
		}

		private static String getMainViewName(IEntityType accountType) {
			final SmbizEntityType set = IEntityType.Util.toEnum(SmbizEntityType.class, accountType);
			return set.getValue() + "_MAIN";
		}

		protected MainViewClass(IEntityType accountType) {
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
	public final void doInitialization(IViewKeyProvider viewRequest) {
		// no-op
	}

	@Override
	protected void doDestroy() {
		// no-op
	}
}
