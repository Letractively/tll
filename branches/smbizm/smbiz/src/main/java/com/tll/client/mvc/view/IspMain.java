/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.SmbizAdmin;
import com.tll.client.ui.HtmlListPanel;
import com.tll.common.AdminContext;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;

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
		public IspMain newView() {
			return new IspMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public IspMain() {
		super();

		final AdminContext ac = SmbizAdmin.getAdminContext();
		assert ac != null;
		final ModelKey userAccountRef = ac.getUserAccount().getKey();
		assert userAccountRef != null && userAccountRef.isSet();

		final AccountSearch as = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.MERCHANT);
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
	protected Class getViewClass() {
		return klas;
	}
}
