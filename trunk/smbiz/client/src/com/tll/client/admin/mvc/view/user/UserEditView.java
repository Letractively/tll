/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.mvc.view.user;

import com.tll.client.admin.ui.field.user.UserPanel;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.AbstractFieldGroupModelBinding;
import com.tll.client.model.Model;
import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.model.EntityType;

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
	 * UserEditBinding
	 * @author jpk
	 */
	private static final class UserEditBinding extends AbstractFieldGroupModelBinding {

		@Override
		protected Model doResolveModel(EntityType modelType) throws IllegalArgumentException {
			if(modelType == EntityType.USER)
				return getModel(null);
			else if(modelType == EntityType.ACCOUNT) return getModel("account");

			return null;
		}

	}

	/**
	 * Constructor
	 */
	public UserEditView() {
		super(new UserEditBinding(), new UserPanel(), null);
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}

	@Override
	protected AuxDataRequest getNeededAuxData() {
		AuxDataRequest auxDataRequest = new AuxDataRequest();
		auxDataRequest.requestEntityList(EntityType.AUTHORITY);
		return auxDataRequest;
	}

}
