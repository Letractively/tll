/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.mvc.view.user;

import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.ui.field.user.UserPanel;
import com.tll.common.data.AuxDataRequest;
import com.tll.common.model.SmbizEntityType;
import com.tll.refdata.RefDataType;

/**
 * UserEditView
 * @author jpk
 */
public class UserEditView extends EditView {

	public static final Class klas = new Class();

	public static final class Class extends ViewClass {

		private static final ViewOptions VIEW_OPTIONS = new ViewOptions(true, true, true, true, true);

		@Override
		public String getName() {
			return "userEdit";
		}

		@Override
		public UserEditView newView() {
			return new UserEditView();
		}

		@Override
		public ViewOptions getViewOptions() {
			return VIEW_OPTIONS;
		}
	}

	/**
	 * Constructor
	 */
	public UserEditView() {
		super(new UserPanel());
	}

	@Override
	public ViewClass getViewClass() {
		return klas;
	}

	@Override
	protected AuxDataRequest getNeededAuxData() {
		final AuxDataRequest auxDataRequest = new AuxDataRequest();
		auxDataRequest.requestEntityList(SmbizEntityType.CURRENCY);
		auxDataRequest.requestAppRefData(RefDataType.US_STATES);
		auxDataRequest.requestAppRefData(RefDataType.ISO_COUNTRY_CODES);
		auxDataRequest.requestEntityList(SmbizEntityType.AUTHORITY);
		return auxDataRequest;
	}

}
