/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.admin.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.HtmlListPanel;
import com.tll.client.ui.ViewRequestLink;
import com.tll.model.EntityType;

/**
 * CustomerMain - ISP root view.
 * @author jpk
 */
public class CustomerMain extends MainView {

	public static final Class klas = new Class();

	public static final class Class extends MainViewClass {

		public Class() {
			super(EntityType.CUSTOMER);
		}

		@Override
		public IView newView() {
			return new CustomerMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public CustomerMain() {
		super();
		links.add(new ViewRequestLink("Customer Listing", "Customer Listing", null));
		addWidget(links);
	}

	public String getLongViewName() {
		return "Customer Main";
	}

	protected Widget getViewWidgetInternal() {
		return links;
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}
}
