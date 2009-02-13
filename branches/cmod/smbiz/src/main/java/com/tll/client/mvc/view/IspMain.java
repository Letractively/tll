/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.SmbizAdmin;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.HtmlListPanel;
import com.tll.common.AdminContext;
import com.tll.common.model.RefKey;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.SmbizEntityType;

/**
 * IspMain - ISP root view.
 * @author jpk
 */
public class IspMain extends MainView {

	public static final Class klas = new Class();

	public static final class Class extends MainViewClass {

		public Class() {
			super(SmbizEntityType.ISP);
		}

		@Override
		public IView newView() {
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

		AccountSearch as = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.MERCHANT);
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
