/**
 * The Logic Lab
 * @author jpk
 * Jun 28, 2008
 */
package com.tll.client.admin.ui.field.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IDataProvider;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.listhandler.Sorting;

/**
 * ParameterListingPanel
 * @author jpk
 */
public class ParameterListingPanel extends Composite {

	/**
	 * ParameterListingConfig
	 * @author jpk
	 */
	private static final class ParameterListingConfig implements IListingConfig<FieldGroup> {

		private final ITableCellRenderer<FieldGroup> cellRenderer = new ITableCellRenderer<FieldGroup>() {

			public String getCellValue(FieldGroup rowData, Column column) {
				for(IField field : rowData) {
					if(field.getPropertyName().endsWith(column.getPropertyName())) {
						return field.getValue();
					}
				}
				return null;
			}
		};

		private final Column[] columns;

		/**
		 * Constructor
		 */
		public ParameterListingConfig() {
			super();

			// assemble the columns
			columns = new Column[3];
			columns[0] = new Column("Name", "name");
			columns[1] = new Column("Code", "code");
			columns[2] = new Column("Description", "description");
		}

		public String getCaption() {
			return null;
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
			return "Param";
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
			return null;
		}

		public IAddRowDelegate getAddRowHandler() {
			return null;
		}
	}

	private static final ParameterListingConfig paramListingConfig = new ParameterListingConfig();

	private final List<ParameterPanel> panels = new ArrayList<ParameterPanel>();

	private final DataListingWidget<FieldGroup> listing;

	/**
	 * Constructor
	 * @param panels
	 */
	public ParameterListingPanel(ParameterPanel[] panels) {
		super();
		if(panels != null) {
			for(ParameterPanel panel : panels) {
				this.panels.add(panel);
			}
		}

		listing = ListingFactory.createListingWidget(this, paramListingConfig, new IDataProvider<FieldGroup>() {

			public FieldGroup[] getData() {
				return ParameterListingPanel.this.getData();
			}
		});

		initWidget(listing);
	}

	private FieldGroup[] getData() {
		FieldGroup[] arr = new FieldGroup[ParameterListingPanel.this.panels.size()];
		int i = 0;
		for(ParameterPanel panel : ParameterListingPanel.this.panels) {
			arr[i++] = panel.getFields();
		}
		return arr;
	}

}
