/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
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

	private final Label lbl;

	/**
	 * Constructor
	 */
	public AspMain() {
		super();

		final AccountSearch as = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.ISP);
		as.setNamedQuery("account.ispList");

		lbl = new Label("This is the ASP main view.");
		addWidget(lbl);
	}

	public String getLongViewName() {
		return "ASP Main";
	}

	protected Widget getViewWidgetInternal() {
		return lbl;
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}
}
