/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.admin.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.AdminContext;
import com.tll.client.admin.SmbizAdmin;
import com.tll.client.model.IEntityType;
import com.tll.client.model.RefKey;
import com.tll.client.mvc.view.AbstractView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.search.ISearch;
import com.tll.client.search.impl.AccountSearch;
import com.tll.client.ui.HtmlListPanel;

/**
 * IspMain - ISP root view.
 * @author jpk
 */
public class IspMain extends MainView {

	public static final Class klas = new Class();

	public static final class Class extends MainViewClass {

		public Class() {
			super(IEntityType.ISP);
		}

		@Override
		public AbstractView newView() {
			return new IspMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public IspMain() {
		super();

		AdminContext ac = SmbizAdmin.getAdminContext();
		assert ac != null;
		RefKey userAccountRef = ac.getUserAccount().getRefKey();
		assert userAccountRef != null && userAccountRef.isSet();

		AccountSearch as = new AccountSearch(ISearch.TYPE_SCALER_QUERY, IEntityType.MERCHANT);
		as.setParentAccountRef(userAccountRef);

		addWidget(links);
	}

	public String getLongViewName() {
		return "ISP Main";
	}

	protected Widget getViewWidgetInternal() {
		return links;
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}
}
