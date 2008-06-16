/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.IListingListener;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IListingConfig;
import com.tll.client.model.IData;
import com.tll.client.ui.CSS;

/**
 * ListingWidget - Base class for all listing {@link Widget}s in the app.
 * @param <R> The row data type.
 * @author jpk
 */
public abstract class ListingWidget<R extends IData> extends Composite implements HasFocus, SourcesTableEvents, SourcesMouseEvents, IListingListener<R>, IModelChangeListener {

	/**
	 * The css class the top-most containing div gets.
	 */
	private static final String STYLE_TABLE_VIEW = "tableView";

	/**
	 * The css class for table view captions.
	 */
	private static final String STYLE_CAPTION = "caption";

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

	protected RowContextPopup rowPopup;

	/**
	 * Constructor
	 * @param config The listing configuration Can't be <code>null</code>.
	 * @param addRowDelegate The optional delegate for adding rows.
	 */
	public ListingWidget(IListingConfig<R> config, IAddRowDelegate addRowDelegate) {
		super();
		if(config == null) throw new IllegalArgumentException("A listing configuration must be specified.");

		FlowPanel tableViewPanel = new FlowPanel();
		tableViewPanel.setStylePrimaryName(STYLE_TABLE_VIEW);

		// add a caption if specified
		if(config.getCaption() != null) {
			Label caption = new Label(config.getCaption());
			caption.setStyleName(STYLE_CAPTION);
			tableViewPanel.add(caption);
		}

		// portal
		portal.setStyleName(CSS.PORTAL);
		tableViewPanel.add(portal);

		// generate nav bar
		if(config.isShowNavBar()) {
			navBar = new ListingNavBar<R>(config);
			tableViewPanel.add(navBar.getWidget());
			if(addRowDelegate != null) {
				navBar.setAddRowDelegate(addRowDelegate);
			}
		}
		else {
			navBar = null;
		}

		focusPanel.add(tableViewPanel);

		initWidget(focusPanel);
	}

	public final void setRowPopup(RowContextPopup rowPopup) {
		if(this.rowPopup != null) {
			removeMouseListener(this.rowPopup);
			removeTableListener(this.rowPopup);
		}
		this.rowPopup = rowPopup;
		if(rowPopup != null) {
			addTableListener(rowPopup);
			addMouseListener(rowPopup);
		}
	}

	public void addRow(R rowData) {
		// table.addRow(rowData);
		if(navBar != null) navBar.increment();
	}

	public void updateRow(int rowIndex, R rowData) {
		// table.updateRow(rowIndex, rowData);
	}

	public void deleteRow(int rowIndex) {
		// table.deleteRow(rowIndex);
		if(navBar != null) navBar.decrement();
	}

	public void markRowDeleted(int rowIndex) {
		// table.markRowDeleted(rowIndex);
	}

	public final void addMouseListener(MouseListener listener) {
		focusPanel.addMouseListener(listener);
	}

	public final void removeMouseListener(MouseListener listener) {
		focusPanel.removeMouseListener(listener);
	}

	/**
	 * @return The parent {@link Panel} containing all constituent listing
	 *         {@link Widget}s.
	 */
	protected final FocusPanel getListingPanel() {
		return focusPanel;
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

	public final void addFocusListener(FocusListener listener) {
		focusPanel.addFocusListener(listener);
	}

	public final void removeFocusListener(FocusListener listener) {
		focusPanel.removeFocusListener(listener);
	}

	public final void addKeyboardListener(KeyboardListener listener) {
		focusPanel.addKeyboardListener(listener);
	}

	public final void removeKeyboardListener(KeyboardListener listener) {
		focusPanel.removeKeyboardListener(listener);
	}

	public void onListingEvent(ListingEvent<R> event) {
		if(navBar != null) navBar.onListingEvent(event);
	}

	public void onModelChangeEvent(ModelChangeEvent event) {
		// base impl: no-op
	}

}
