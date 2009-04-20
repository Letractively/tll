/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingHandler;
import com.tll.client.listing.IListingOperator;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ListingEvent;

/**
 * ListingWidget - Base class for all listing {@link Widget}s in the app.
 * @author jpk
 * @param <R> The row data type.
 * @param <T> the table widget type
 */
public class ListingWidget<R, T extends ListingTable<R>> extends Composite implements
		Focusable, KeyDownHandler, IListingHandler<R> {

	/**
	 * Styles - (tableview.css)
	 * @author jpk
	 */
	protected static class Styles {

		/**
		 * The css class the top-most containing div gets.
		 */
		public static final String TABLE_VIEW = "tableView";

		/**
		 * The css class for table view captions.
		 */
		public static final String CAPTION = "caption";

		/**
		 * The listing portal style.
		 */
		public static final String PORTAL = "portal";
	} // Styles

	/**
	 * The listing table.
	 */
	protected final T table;

	/**
	 * The listing navigation bar.
	 */
	protected final ListingNavBar<R> navBar;

	/**
	 * The main "listing" panel containing all widgets comprising this widget.
	 */
	protected final FocusPanel focusPanel = new FocusPanel();

	/**
	 * Wrapped around the listing table enabling vertical scrolling.
	 */
	protected final ScrollPanel portal = new ScrollPanel();

	/**
	 * The listing operator
	 */
	private IListingOperator<R> operator;

	/**
	 * The optional row popup.
	 */
	protected RowContextPopup rowPopup;

	/**
	 * Constructor
	 * @param config The listing configuration
	 * @param table {@link ListingTable} implementation
	 */
	public ListingWidget(IListingConfig<R> config, T table) {
		super();
		final FlowPanel tableViewPanel = new FlowPanel();
		tableViewPanel.setStylePrimaryName(Styles.TABLE_VIEW);

		// add a caption if specified
		if(config.getCaption() != null) {
			final Label caption = new Label(config.getCaption());
			caption.setStyleName(Styles.CAPTION);
			tableViewPanel.add(caption);
		}

		// portal
		portal.setStyleName(Styles.PORTAL);
		tableViewPanel.add(portal);

		// table
		portal.add(table);
		focusPanel.addKeyDownHandler(this);
		this.table = table;

		// generate nav bar
		if(config.isShowNavBar()) {
			navBar = new ListingNavBar<R>(config);
			tableViewPanel.add(navBar.getWidget());
		}
		else {
			navBar = null;
		}

		focusPanel.add(tableViewPanel);

		initWidget(focusPanel);
	}

	/**
	 * Sets the operator which is delegated to on behalf of this Widget for
	 * performing listing ops.
	 * @param operator The listing operator
	 */
	public final void setOperator(IListingOperator<R> operator) {
		if(operator == null) throw new IllegalArgumentException();
		if(this.operator != null) throw new IllegalStateException();
		this.operator = operator;
		operator.setSourcingWidget(this);
		this.table.setListingOperator(operator);
		if(navBar != null) navBar.setListingOperator(operator);
		addHandler(this, ListingEvent.TYPE);
	}

	protected final IListingOperator<R> getOperator() {
		return operator;
	}

	public final void clear() {
		operator.clear();
	}

	public final void refresh() {
		operator.refresh();
	}

	/**
	 * Routes row clicks to the given row options delgate.
	 * @param rowOptionsDelegate The row options delegate
	 */
	public final void setRowOptionsDelegate(IRowOptionsDelegate rowOptionsDelegate) {
		if(rowPopup == null) {
			rowPopup = new RowContextPopup(table);
			focusPanel.addMouseDownHandler(rowPopup);
		}
		rowPopup.setDelegate(rowOptionsDelegate);
	}

	/**
	 * Sets the add row delegate in the nav bar which must already be present.
	 * @param addRowDelegate The add row delegate which will handle add row
	 *        requests emanating from the nav bar.
	 */
	public final void setAddRowDelegate(IAddRowDelegate addRowDelegate) {
		if(navBar == null) throw new IllegalStateException();
		navBar.setAddRowDelegate(addRowDelegate);
	}

	/**
	 * @return The number of rows <em>shown</em> in the listing.
	 */
	public final int getNumRows() {
		return table.getRowCount();
	}

	public final void addRow(R rowData) {
		table.addRow(rowData);
		if(navBar != null) navBar.increment();
	}

	public final void updateRow(int rowIndex, R rowData) {
		table.updateRow(rowIndex, rowData);
	}

	public final void deleteRow(int rowIndex) {
		table.deleteRow(rowIndex);
		if(navBar != null) navBar.decrement();
	}

	public final void markRowDeleted(int rowIndex, boolean markDeleted) {
		table.markRowDeleted(rowIndex, markDeleted);
	}

	public final int getTabIndex() {
		return focusPanel.getTabIndex();
	}

	public final void setAccessKey(char key) {
		focusPanel.setAccessKey(key);
	}

	public final void setFocus(boolean focused) {
		focusPanel.setFocus(focused);
	}

	public final void setTabIndex(int index) {
		focusPanel.setTabIndex(index);
	}

	public void onKeyDown(KeyDownEvent event) {
		delegateEvent(table, event);
	}

	public final void onListingEvent(ListingEvent<R> event) {
		table.onListingEvent(event);
		if(navBar != null) navBar.onListingEvent(event);
	}
}
