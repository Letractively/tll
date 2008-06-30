/**
 * The Logic Lab
 * @author jpk
 * Jun 28, 2008
 */
package com.tll.client.admin.ui.field.intf;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.tll.client.cache.AuxDataCache;
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
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.listing.DataListingWidget;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * ParameterListingPanel
 * @author jpk
 */
public final class ParameterListingPanel extends Composite implements IEditListener {

	private static final String listingElementName = "Parameter";

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
			ParameterPanel pp = panels.get(rowIndex - 1);
			if(pp.propertyName == null) {
				// new param
				panels.remove(pp);
				parentFieldGroup.removeField(pp.getFields());
				listing.refresh();
			}
			else {
				pp.getFields().addPendingDeletion(pp.propertyName);
				listing.markRowDeleted(rowIndex);
			}
		}

		@Override
		protected void doEditRow(int rowIndex) {
			current = panels.get(rowIndex - 1);
			editPanel.setFieldPanel(current);
			editPanel.setEditMode(false);
			dialog.setText("Edit Parameter");
			dialog.center();
		}
	};

	private final IAddRowDelegate addRowDelegate = new IAddRowDelegate() {

		public void handleAddRow() {
			current = new ParameterPanel(null);
			Model newParam = AuxDataCache.instance().getEntityPrototype(EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
			assert newParam != null;
			current.getFields().bindModel(newParam.getBindingRef());
			editPanel.setFieldPanel(current);
			editPanel.setEditMode(true);
			dialog.setText("Add Parameter");
			dialog.center();
		}
	};

	/**
	 * The property path from the root interface to the parent option.
	 */
	private final String optionPropertyPath;

	private final FieldGroup parentFieldGroup;

	private final List<ParameterPanel> panels = new ArrayList<ParameterPanel>();

	/**
	 * The parameter panel currently under editing.
	 */
	private ParameterPanel current;

	private final DataListingWidget<FieldGroup> listing;

	private final EditPanel editPanel;
	private final Dialog dialog;

	/**
	 * Constructor
	 * @param optionPropertyPath The parent option path from the root interface
	 * @param parentFieldGroup Used when adding or deleting parameters
	 * @param panels
	 */
	public ParameterListingPanel(String optionPropertyPath, FieldGroup parentFieldGroup, ParameterPanel[] panels) {
		super();
		this.optionPropertyPath = optionPropertyPath;
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
		dialog = new Dialog(null, false);
		dialog.setWidget(editPanel);
	}

	public void onEditEvent(EditEvent event) {
		assert current != null;
		switch(event.getOp()) {
			case CANCEL:
				current.getFields().reset();
				break;
			case SAVE:
				if(current.propertyName == null) {
					// new param
					String pendingPath =
							PropertyPath.getPropertyPath(optionPropertyPath, PropertyPath.indexedUnbound("parameters"));
					parentFieldGroup.addField(pendingPath, current.getFields());
					panels.add(current);
				}
				listing.refresh();
				break;
			case DELETE:
				if(current.propertyName == null) {
					// new param
					panels.remove(current);
					parentFieldGroup.removeField(current.getFields());
					listing.refresh();
				}
				else {
					// extisting
					current.getFields().addPendingDeletion(current.propertyName);
				}
				break;
		}
		dialog.hide();
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
