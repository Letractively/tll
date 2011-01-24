/**
 * The Logic Lab
 * @author jpk
 * @since May 8, 2009
 */
package com.tll.client.ui.listing;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.HandlerRegistration;
import com.tll.IMarshalable;
import com.tll.client.data.rpc.IRpcHandler;
import com.tll.client.data.rpc.RpcEvent;

/**
 * RemoteListingWidget
 * @param <R> row data type
 * @param <T> table type
 * @author jpk
 */
public class RemoteListingWidget<R extends IMarshalable, T extends ListingTable<R>> extends ListingWidget<R, T> implements IRpcHandler {

	private HandlerRegistration hrRpc;

	private Element glass;

	private boolean isGlassEnabled;

	/**
	 * Constructor
	 * @param listingElementName
	 * @param table
	 * @param navBar
	 */
	public RemoteListingWidget(String listingElementName, T table, ListingNavBar<R> navBar) {
		super(listingElementName, table, navBar);
	}

	public boolean isGlassEnabled() {
		return isGlassEnabled;
	}

	/**
	 * When enabled, the background will be blocked with a semi-transparent pane
	 * the next time it is shown. If the PopupPanel is already visible, the glass
	 * will not be displayed until it is hidden and shown again.
	 * @param enabled true to enable, false to disable
	 */
	public void setGlassEnabled(boolean enabled) {
		this.isGlassEnabled = enabled;
		if(enabled) {
			assert hrRpc == null;
			hrRpc = addHandler(this, RpcEvent.TYPE);
			if(glass == null) {
				glass = Document.get().createDivElement();
				glass.setClassName(ListingStyles.css().glass());

				glass.getStyle().setPosition(Position.ABSOLUTE);
				glass.getStyle().setLeft(0, Unit.PX);
				glass.getStyle().setTop(0, Unit.PX);
			}
		}
		else {
			if(hrRpc != null) {
				hrRpc.removeHandler();
				hrRpc = null;
			}
		}
	}

	@Override
	public void onRpcEvent(RpcEvent event) {
		assert glass != null;
		switch(event.getType()) {
			case SENT:
				// show glass
				Document.get().getBody().appendChild(glass);
				positionGlass();
				break;
			default:
				// hide glass
				Document.get().getBody().removeChild(glass);
				break;
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(glass != null) glass.getStyle().setProperty("visibility", visible ? "visible" : "hidden");
	}

	private void positionGlass() {
		Style style = glass.getStyle();

		int wdgtWidth = getElement().getClientWidth();
		int wdgtHeight = getElement().getClientHeight();
		int wdgtLeft = getElement().getAbsoluteLeft();
		int wdgtTop = getElement().getAbsoluteTop();

		// Hide the glass while checking the document size. Otherwise it would
		// interfere with the measurement.
		style.setDisplay(Display.NONE);
		style.setWidth(0, Unit.PX);
		style.setHeight(0, Unit.PX);

		int width = getElement().getScrollWidth();
		int height = getElement().getScrollHeight();

		// Set the glass size to the larger of the window's client size or the
		// document's scroll size.
		style.setWidth(Math.max(width, wdgtWidth), Unit.PX);
		style.setHeight(Math.max(height, wdgtHeight), Unit.PX);
		style.setLeft(wdgtLeft, Unit.PX);
		style.setTop(wdgtTop, Unit.PX);

		// The size is set. Show the glass again.
		style.setDisplay(Display.BLOCK);
	}
}
