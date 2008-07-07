/**
 * The Logic Lab
 * @author jpk
 * Jun 28, 2008
 */
package com.tll.client.admin.ui.field.intf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			final PropertyPath pp = getParamPath(rowIndex, false);
			if(pp.isUnboundIndexed()) {
				// new param
				parentFieldGroup.removeFields(parentFieldGroup.getFields(pp.toString()));
				listing.refresh();
			}
			else {
				// existing param
				parentFieldGroup.addPendingDeletion(pp.toString());
				listing.markRowDeleted(rowIndex);
			}
		}

		@Override
		protected void doEditRow(int rowIndex) {
			final PropertyPath pp = new PropertyPath(paramPaths.get(new Integer(rowIndex - 1)));
			ParameterPanel ppanel = new ParameterPanel(pp.toString(), parentFieldGroup);
			editPanel.setFieldPanel(ppanel);
			editPanel.setEditMode(false);
			dialog.setText("Edit Parameter");
			dialog.center();
		}
	};

	private final IAddRowDelegate addRowDelegate = new IAddRowDelegate() {

		public void handleAddRow() {
			final PropertyPath pp = new PropertyPath(optionPropertyPath, PropertyPath.indexUnbound("parameters"));
			ParameterPanel ppanel = new ParameterPanel(pp.toString(), parentFieldGroup);
			Model newParam = AuxDataCache.instance().getEntityPrototype(EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
			assert newParam != null;
			ppanel.getFields().bindModel(newParam.getBindingRef());
			editPanel.setFieldPanel(ppanel);
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

	private final DataListingWidget<FieldGroup> listing;

	private final EditPanel editPanel;
	private final Dialog dialog;

	/**
	 * Constructor
	 * @param optionPropertyPath The parent option path from the root interface
	 * @param parentFieldGroup Used when adding or deleting parameters
	 */
	public ParameterListingPanel(String optionPropertyPath, FieldGroup parentFieldGroup) {
		super();
		this.optionPropertyPath = optionPropertyPath;
		this.parentFieldGroup = parentFieldGroup;

		final IListingConfig<FieldGroup> listingConfig = new IListingConfig<FieldGroup>() {

			public boolean isIgnoreCaseWhenSorting() {
				return true;
			}

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

	private PropertyPath getParamPath(int rowIndex, boolean isUnbound) {
		final PropertyPath p = new PropertyPath(optionPropertyPath);
		p.append("parameters", rowIndex - 1, isUnbound);
	}

	private FieldGroup[] getData() {
		final Map<Integer, List<IField>> map = new HashMap<Integer, List<IField>>();

		final Set<IField> set = parentFieldGroup.getFields(optionPropertyPath);
		final PropertyPath pp = new PropertyPath();

		List<IField> list = new ArrayList<IField>();

		// split the resultant set of fields based on the parameter portion of the
		// property path
		for(IField fld : set) {
			pp.parse(fld.getPropertyName());
			// we can safely assume the option property path's depth is 1 since it is
			// always relative to an interface
			pp = pp.nested(1);
			Integer paramIndex = new Integer(pp.indexAt(0));
			if(!map.containsKey(paramIndex)) {
				map.put(paramIndex, new ArrayList<IField>());
			}
			map.get(paramIndex).add(fld);
		}

		FieldGroup[] arr = new FieldGroup[ParameterListingPanel.this.panels.size()];
		int i = 0;
		for(ParameterPanel panel : ParameterListingPanel.this.panels) {
			arr[i++] = panel.getFields();
		}
		return arr;
	}

	public void onEditEvent(EditEvent event) {
		switch(event.getOp()) {
			case CANCEL:
				current.getFields().reset();
				break;
			case SAVE:
				if(current.propertyName == null) {
					// new param
					String pendingPath =
							PropertyPath.getPropertyPath(optionPropertyPath, PropertyPath.indexUnbound("parameters"));
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

	/**
	 * Renders the listing.
	 */
	public void refresh() {
		listing.refresh();
	}
}
