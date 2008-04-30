/**
 * The Logic Lab
 * @author jpk Aug 28, 2007
 */
package com.tll.client.ui.listing;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.IListingListener;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.ListingEvent;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.ClientListingOp;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.ui.CSS;
import com.tll.client.ui.FocusCommand;
import com.tll.listhandler.SortDir;

/**
 * AbstractListingWidget - Base class for all listing {@link Widget}s in the
 * app.
 * @author jpk
 */
public abstract class AbstractListingWidget extends Composite implements IListingListener, HasFocus, IModelChangeListener, IListingOperator {

	/**
	 * The css class the top-most containing div gets.
	 */
	private static final String STYLE_TABLE_VIEW = "tableView";

	/**
	 * The css class for table view captions.
	 */
	private static final String STYLE_CAPTION = "caption";

	/**
	 * The actual listing table.
	 */
	protected final ListingTable table;

	/**
	 * The listing navigation bar.
	 */
	protected final ListingNavBar navBar;

	/**
	 * The main "listing" panel containing all widgets comprising this widget.
	 */
	private final FocusPanel focusPanel = new FocusPanel();

	/**
	 * Wrapped around the listing table enabling vertical scrolling.
	 */
	private final ScrollPanel portal = new ScrollPanel();

	/**
	 * The listing operator.
	 */
	private IListingOperator operator;

	/**
	 * Constructor
	 * @param config The listing configuration Can't be <code>null</code>.
	 */
	public AbstractListingWidget(IListingConfig config) {
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

		// create and initialize the table panel
		table = new ListingTable(config);

		// portal
		portal.setStyleName(CSS.PORTAL);
		portal.add(table.getTableWidget());
		tableViewPanel.add(portal);

		// generate nav bar
		if(config.isShowNavBar()) {
			navBar = new ListingNavBar(config);
			tableViewPanel.add(navBar.getWidget());
		}
		else {
			navBar = null;
		}

		focusPanel.add(tableViewPanel);

		initWidget(focusPanel);
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(IListingOperator operator) {
		this.operator = operator;
		table.setListingOperator(operator);
		if(navBar != null) navBar.setListingOperator(operator);
	}

	public final void clear() {
		operator.clear();
	}

	public final void deleteRow(int rowIndex) {
		operator.deleteRow(rowIndex);
	}

	public final void display() {
		operator.display();
	}

	public final void insertRow(int rowIndex, Model rowData) {
		operator.insertRow(rowIndex, rowData);
	}

	public final void navigate(ClientListingOp navAction, Integer page) {
		operator.navigate(navAction, page);
	}

	public final void refresh() {
		operator.refresh();
	}

	public final void sort(String colName, SortDir direction) {
		operator.sort(colName, direction);
	}

	public final void updateRow(int rowIndex, Model rowData) {
		operator.updateRow(rowIndex, rowData);
	}

	public final RefKey getRowRef(int rowIndex) {
		return table.getRowRef(rowIndex);
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

	public final void onListingEvent(ListingEvent event) {
		assert event.getWidget() == this;
		if(event.getWidget() == this) {
			table.onListingEvent(event);
			if(!event.isSuccess()) {
				if(navBar != null) navBar.getWidget().setVisible(false);
			}
			else {
				if(navBar != null) {
					navBar.onListingEvent(event);
					navBar.getWidget().setVisible(true);
				}
				DeferredCommand.addCommand(new FocusCommand(focusPanel, true));
			}
		}
	}

	public final void onModelChangeEvent(ModelChangeEvent event) {
		switch(event.getChangeOp()) {
			case ADD:
				onListingEvent(new ListingEvent(this, true, ClientListingOp.INSERT_ROW, 0, event.getModel()));
				break;
			case UPDATE: {
				RefKey modelRef = event.getModel().getRefKey();
				int rowIndex = table.getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					// TODO determine how to handle named query specific model data!!
					onListingEvent(new ListingEvent(this, true, ClientListingOp.UPDATE_ROW, rowIndex - 1, event.getModel()));
				}
				break;
			}
			case DELETE: {
				RefKey modelRef = event.getModelRef();
				int rowIndex = table.getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					onListingEvent(new ListingEvent(this, true, ClientListingOp.DELETE_ROW, rowIndex - 1, null));
				}
				break;
			}

		}
	}
}
