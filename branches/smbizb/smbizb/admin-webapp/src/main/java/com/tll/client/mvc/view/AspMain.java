/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.view.account.IspListingView;
import com.tll.client.mvc.view.intf.InterfacesView;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.view.ViewLink;
import com.tll.common.model.SmbizEntityType;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;

/**
 * AspMain - ASP root view.
 * @author jpk
 */
public class AspMain extends MainView {

	public static final Class klas = new Class();

	public static final class Class extends MainViewClass {

		public Class() {
			super(SmbizEntityType.ASP);
		}

		@Override
		public AspMain newView() {
			return new AspMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public AspMain() {
		super();

		final AccountSearch as = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.ISP);
		as.setNamedQuery("account.ispList");

		links.append(new ViewLink("Isp Listing", "Isp Listing", new StaticViewInitializer(IspListingView.klas)));
		links.append(new ViewLink("Interfaces", "Interfaces", new StaticViewInitializer(InterfacesView.klas)));
		links.append(new Hyperlink("Site Summary", "siteSmry"));
		addWidget(links);
	}

	public String getLongViewName() {
		return "ASP Main";
	}

	protected Widget getViewWidgetInternal() {
		return links;
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}
}
