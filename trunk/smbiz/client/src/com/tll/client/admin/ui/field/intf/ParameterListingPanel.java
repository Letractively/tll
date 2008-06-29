/**
 * The Logic Lab
 * @author jpk
 * Jun 28, 2008
 */
package com.tll.client.admin.ui.field.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.tll.client.event.IEditListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IDataProvider;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.listhandler.Sorting;

/**
 * ParameterListingPanel
 * @author jpk
 */
public final class ParameterListingPanel extends Composite implements IEditListener {

	private static final String listingElementName = "Paremeter";

	private static final ITableCellRenderer<FieldGroup> cellRenderer = new ITableCellRenderer<FieldGroup>() {

		public String getCellValue(FieldGroup rowData, Column column) {
			for(IField field : rowData) {
				if(field.getPropertyName().endsWith(column.getPropertyName())) {
					return field.getValue();
				}
			}
			return null;
		}
	};

	private static final Column[] columns = new Column[] {
		new Column("Name", "name"),
		new Column("Code", "code"),
		new Column("Description", "description") };

	private final AbstractRowOptions rowOptions = new AbstractRowOptions() {

		@Override
		protected String getListingElementName() {
			return listingElementName;
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			// TODO
		}

		@Override
		protected void doEditRow(int rowIndex) {
			// TODO
		}
	};

	private static final IAddRowDelegate addRowDelegate = new IAddRowDelegate() {

		public void handleAddRow() {
			// TODO
		}
	};

	private final FieldGroup parentFieldGroup;

	private final List<ParameterPanel> panels = new ArrayList<ParameterPanel>();

	private final DataListingWidget<FieldGroup> listing;

	private final EditPanel editPanel;
	private final Dialog dialog;

	/**
	 * Constructor
	 * @param parentFieldGroup Used when adding or deleting parameters
	 * @param panels
	 */
	public ParameterListingPanel(FieldGroup parentFieldGroup, ParameterPanel[] panels) {
		super();
		this.parentFieldGroup = parentFieldGroup;
		if(panels != null) {
			for(ParameterPanel panel : panels) {
				this.panels.add(panel);
			}
		}

		final IListingConfig<FieldGroup> listingConfig = new IListingConfig<FieldGroup>() {

			public String getCaption() {
				return "Parameters";
			}

			public ITableCellRenderer<FieldGroup> getCellRenderer() {
				return cellRenderer;
			}

			public Column[] getColumns() {
				return columns;
			}

			public Sorting getDefaultSorting() {
				return null;
			}

			public String getListingElementName() {
				return listingElementName;
			}

			public int getPageSize() {
				return -1;
			}

			public boolean isShowNavBar() {
				return true;
			}

			public boolean isShowRefreshBtn() {
				return false;
			}

			public boolean isSortable() {
				return false;
			}

			public IRowOptionsDelegate getRowOptionsHandler() {
				return rowOptions;
			}

			public IAddRowDelegate getAddRowHandler() {
				return addRowDelegate;
			}
		};

		final IDataProvider<FieldGroup> dataProvider = new IDataProvider<FieldGroup>() {

			public FieldGroup[] getData() {
				return ParameterListingPanel.this.getData();
			}
		};

		listing = ListingFactory.createListingWidget(this, listingConfig, dataProvider);

		initWidget(listing);

		editPanel = new EditPanel(true, false);
		editPanel.addEditListener(this);
		dialog = new Dialog(listing, true);
		dialog.setWidget(editPanel);
	}

	public void onEditEvent(EditEvent event) {
		// event.get
		// TODO
	}

	private FieldGroup[] getData() {
		FieldGroup[] arr = new FieldGroup[ParameterListingPanel.this.panels.size()];
		int i = 0;
		for(ParameterPanel panel : ParameterListingPanel.this.panels) {
			arr[i++] = panel.getFields();
		}
		return arr;
	}

	/**
	 * Renders the listing.
	 */
	public void refresh() {
		listing.refresh();
	}
}
