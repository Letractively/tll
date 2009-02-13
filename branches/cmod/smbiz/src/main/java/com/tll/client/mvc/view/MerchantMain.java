/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.mvc.view;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.mvc.view.IView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.HtmlListPanel;
import com.tll.model.SmbizEntityType;

/**
 * MerchantMain - ISP root view.
 * @author jpk
 */
public class MerchantMain extends MainView {

	public static final Class klas = new Class();

	public static final class Class extends MainViewClass {

		public Class() {
			super(SmbizEntityType.MERCHANT);
		}

		@Override
		public IView newView() {
			return new MerchantMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	/**
	 * Constructor
	 */
	public MerchantMain() {
		super();
		addWidget(links);
	}

	public String getLongViewName() {
		return "Merchant Main";
	}

	protected Widget getViewWidgetInternal() {
		return links;
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}
}
