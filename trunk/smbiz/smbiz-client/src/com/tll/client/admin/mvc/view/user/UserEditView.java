/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.mvc.view.user;

import com.tll.client.admin.ui.field.UserPanel;
import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewOptions;

/**
 * UserEditView
 * @author jpk
 */
public class UserEditView extends EditView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private static final ViewOptions viewOptions = new ViewOptions(true, true, true, true, true);

		private Class() {
			super("userEdit");
		}

		@Override
		public IView newView() {
			return new UserEditView();
		}

		@Override
		public ViewOptions getViewOptions() {
			return viewOptions;
		}

	}

	/**
	 * Constructor
	 */
	public UserEditView() {
		super(new UserPanel(null), null);
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}
}
