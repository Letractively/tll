/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.mvc.view.user;

import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.field.user.UserPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.SmbizEntityType;

/**
 * UserEditView
 * @author jpk
 */
@SuppressWarnings("synthetic-access")
public class UserEditView extends EditView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		//private static final ViewOptions viewOptions = new ViewOptions(true, true, true, true, true);

		private Class() {
			super("userEdit");
		}

		@Override
		public UserEditView newView() {
			return new UserEditView();
		}

		/*
		@Override
		public ViewOptions getViewOptions() {
			return viewOptions;
		}
		*/
	}
	
	/**
	 * Constructor
	 */
	public UserEditView() {
		super(new UserPanel(), null);
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}

	@Override
	protected AuxDataRequest getNeededAuxData() {
		final AuxDataRequest auxDataRequest = new AuxDataRequest();
		auxDataRequest.requestEntityList(SmbizEntityType.AUTHORITY);
		return auxDataRequest;
	}

}
