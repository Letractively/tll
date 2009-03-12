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
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.common.search.AccountSearch;
import com.tll.criteria.CriteriaType;
import com.tll.model.SmbizEntityType;

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
		public IView newView() {
			return new AspMain();
		}
	}

	private final HtmlListPanel links = new HtmlListPanel(false);

	// TODO temp debug
	// private final FlowPanel eventsLegend = new FlowPanel();

	/**
	 * Constructor
	 */
	public AspMain() {
		super();

		final AccountSearch as = new AccountSearch(CriteriaType.SCALAR_NAMED_QUERY, SmbizEntityType.ISP);
		as.setNamedQuery("account.ispList");

		links.append(new ViewRequestLink("Isp Listing", "Isp Listing", IspListingView.klas.newViewRequest(this)));
		links.append(new ViewRequestLink("Interfaces", "Interfaces", new StaticViewRequest(this, InterfacesView.klas)));
		links.append(new Hyperlink("Site Summary", "siteSmry"));
		addWidget(links);

		/*
		eventsLegend.add(new Label("BUTTON_LEFT: " + Event.BUTTON_LEFT));
		eventsLegend.add(new Label("BUTTON_MIDDLE: " + Event.BUTTON_MIDDLE));
		eventsLegend.add(new Label("BUTTON_RIGHT: " + Event.BUTTON_RIGHT));
		eventsLegend.add(new Label("ONCHANGE: " + Event.ONBLUR));
		eventsLegend.add(new Label("ONCHANGE: " + Event.ONCHANGE));
		eventsLegend.add(new Label("ONCLICK: " + Event.ONCLICK));
		eventsLegend.add(new Label("ONDBLCLICK: " + Event.ONDBLCLICK));
		eventsLegend.add(new Label("ONERROR: " + Event.ONERROR));
		eventsLegend.add(new Label("ONFOCUS: " + Event.ONFOCUS));
		eventsLegend.add(new Label("ONKEYDOWN: " + Event.ONKEYDOWN));
		eventsLegend.add(new Label("ONKEYPRESS: " + Event.ONKEYPRESS));
		eventsLegend.add(new Label("ONKEYUP: " + Event.ONKEYUP));
		eventsLegend.add(new Label("ONLOAD: " + Event.ONLOAD));
		eventsLegend.add(new Label("LOSECAPTURE: " + Event.ONLOSECAPTURE));
		eventsLegend.add(new Label("ONMOUSEDOWN: " + Event.ONMOUSEDOWN));
		eventsLegend.add(new Label("ONMOUSEMOVE: " + Event.ONMOUSEMOVE));
		eventsLegend.add(new Label("ONMOUSEOUT: " + Event.ONMOUSEOUT));
		eventsLegend.add(new Label("ONMOUSEOVER: " + Event.ONMOUSEOVER));
		eventsLegend.add(new Label("ONMOUSEUP: " + Event.ONMOUSEUP));
		eventsLegend.add(new Label("ONMOUSEWHEEL: " + Event.ONMOUSEWHEEL));
		eventsLegend.add(new Label("ONSCROLL: " + Event.ONSCROLL));
		eventsLegend.add(new Label("FOCUSEVENTS: " + Event.FOCUSEVENTS));
		eventsLegend.add(new Label("KEYEVENTS: " + Event.KEYEVENTS));
		eventsLegend.add(new Label("MOUSEEVENTS: " + Event.MOUSEEVENTS));
		eventsLegend.add(new Label("UNDEFINED: " + Event.UNDEFINED));
		addWidget(eventsLegend);
		*/
	}

	public String getLongViewName() {
		return "ASP Main";
	}

	protected Widget getViewWidgetInternal() {
		return links;
	}

	@Override
	protected ViewClass getViewClass() {
		return klas;
	}
}
