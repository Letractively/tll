/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.model.SmbizEntityType;

/**
 * CustomerMain - ISP root view.
 * @author jpk
 */
public class CustomerMain extends MainView {

	public static final Class klas = new Class();

	public static final class Class extends MainViewClass {

		public Class() {
			super(SmbizEntityType.CUSTOMER);
		}

		@Override
		public CustomerMain newView() {
			return new CustomerMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public CustomerMain() {
		super();
		links.append(new ViewRequestLink("Customer Listing", "Customer Listing", null));
		addWidget(links);
	}

	public String getLongViewName() {
		return "Customer Main";
	}

	protected Widget getViewWidgetInternal() {
		return links;
	}

	@Override
	protected Class getViewClass() {
		return klas;
	}
}
