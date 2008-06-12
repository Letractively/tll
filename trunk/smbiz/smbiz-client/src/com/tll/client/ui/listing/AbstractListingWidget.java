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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.IModelChangeListener;
import com.tll.client.event.type.ModelChangeEvent;
import com.tll.client.listing.IListingOperator;
import com.tll.client.model.IData;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.ui.CSS;
import com.tll.listhandler.IPage;
import com.tll.listhandler.Sorting;

/**
 * AbstractListingWidget - Base class for all listing {@link Widget}s in the
 * app.
 * @author jpk
 */
public final class AbstractListingWidget extends Composite implements HasFocus, SourcesTableEvents, SourcesMouseEvents, IModelChangeListener {

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
	// protected final ListingNavBar navBar;
	/**
	 * The main "listing" panel containing all widgets comprising this widget.
	 */
	private final FocusPanel focusPanel = new FocusPanel();

	/**
	 * Wrapped around the listing table enabling vertical scrolling.
	 */
	private final ScrollPanel portal = new ScrollPanel();

	/**
	 * The operator that performs actual listing commands for this listing.
	 */
	private IListingOperator operator;

	/**
	 * Constructor
	 * @param config The listing configuration Can't be <code>null</code>.
	 */
	public AbstractListingWidget(String caption, Widget table, Widget navBar) {
		super();
		FlowPanel tableViewPanel = new FlowPanel();
		tableViewPanel.setStylePrimaryName(STYLE_TABLE_VIEW);

		// add a caption if specified
		if(caption != null) {
			Label lbl = new Label(caption);
			lbl.setStyleName(STYLE_CAPTION);
			tableViewPanel.add(lbl);
		}

		// portal
		portal.setStyleName(CSS.PORTAL);
		portal.add(table);
		tableViewPanel.add(portal);

		// generate nav bar
		if(navBar != null) tableViewPanel.add(navBar);

		focusPanel.add(tableViewPanel);

		// TODO determine the purpose of this
		// focusPanel.addKeyboardListener(table);

		initWidget(focusPanel);
	}

	/**
	 * Sets the listing operator for this listing.
	 * @param operator the operator to set
	 */
	public final void setOperator(IListingOperator operator) {
		if(operator == null) {
			throw new IllegalArgumentException("A listing operator must be specified.");
		}
		table.setListingOperator(operator);
		if(navBar != null) navBar.setListingOperator(operator);
		this.operator = operator;
	}

	private void addRow(Model rowData) {
		table.addRow(rowData);
		if(navBar != null) navBar.increment();
	}

	private void updateRow(int rowIndex, Model rowData) {
		table.updateRow(rowIndex, rowData);
	}

	// TODO do we need this?
	/*
	private void deleteRow(int rowIndex) {
		table.deleteRow(rowIndex);
		if(navBar != null) navBar.decrement();
	}
	*/

	private void markRowDeleted(int rowIndex) {
		table.markRowDeleted(rowIndex);
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

	public final void addTableListener(TableListener listener) {
		table.addTableListener(listener);
	}

	public final void removeTableListener(TableListener listener) {
		table.removeTableListener(listener);
	}

	public final void addMouseListener(MouseListener listener) {
		focusPanel.addMouseListener(listener);
	}

	public final void removeMouseListener(MouseListener listener) {
		focusPanel.removeMouseListener(listener);
	}

	/**
	 * Updates the listing with row data and the sorting directive.
	 * @param page The row data
	 * @param sorting The sorting directive. May be <code>null</code>
	 */
	public final void setPage(IPage<? extends IData> page, Sorting sorting) {
		table.setPage(page, sorting);
		if(navBar != null) {
			navBar.setPage(page);
			navBar.getWidget().setVisible(true);
		}
		// DeferredCommand.addCommand(new FocusCommand(focusPanel, true));
	}

	public final void onModelChangeEvent(ModelChangeEvent event) {
		switch(event.getChangeOp()) {
			case ADDED:
				// TODO make this check more robust
				if(this.getElement().isOrHasChild(event.getWidget().getElement())) {
					// i.e. the add button in the nav bar was the source of the model
					// change..
					addRow(event.getModel());
				}
				break;
			case UPDATED: {
				RefKey modelRef = event.getModel().getRefKey();
				int rowIndex = table.getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					// TODO determine how to handle named query specific model data!!
					updateRow(rowIndex, event.getModel());
				}
				break;
			}
			case DELETED: {
				RefKey modelRef = event.getModelRef();
				int rowIndex = table.getRowIndex(modelRef);
				if(rowIndex != -1) {
					assert rowIndex > 0; // header row
					markRowDeleted(rowIndex);
				}
				break;
			}

		}
	}
}
