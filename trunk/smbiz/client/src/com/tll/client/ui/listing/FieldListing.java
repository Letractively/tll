/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.ui.listing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import com.tll.client.event.IEditListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.field.IFieldProvider;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.IAddRowDelegate;
import com.tll.client.listing.IDataProvider;
import com.tll.client.listing.IListingConfig;
import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.listing.ListingFactory;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.PropertyPath;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.field.DelegatingFieldGroupPanel;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.listhandler.Sorting;
import com.tll.model.EntityType;

/**
 * FieldListing - Listing Widget dedicated to IField type data.
 * @author jpk
 */
public final class FieldListing extends Composite implements IEditListener {

	/**
	 * The dedicated field type table cell renderer.
	 */
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

	private final AbstractRowOptions rowOptions = new AbstractRowOptions() {

		@Override
		protected String getListingElementName() {
			return listingElementName;
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			final PropertyPath pp = getRowPropertyPath(rowIndex, false);
			if(pp.isUnboundIndexed()) {
				// new entity
				parentFieldGroup.removeFields(parentFieldGroup.getFields(pp.toString()));
				listing.refresh();
			}
			else {
				// existing entity
				parentFieldGroup.addPendingDeletion(pp.toString());
				listing.markRowDeleted(rowIndex);
			}
		}

		@Override
		protected void doEditRow(int rowIndex) {
			final PropertyPath pp = getRowPropertyPath(rowIndex, false);
			pp.index(rowIndex - 1);
			fieldGroupPanel.setParentPropertyPath(pp.toString());

			editPanel.setEditMode(false);

			dialog.setText("Edit Parameter");
			dialog.center();
		}
	};

	private final IAddRowDelegate addRowDelegate = new IAddRowDelegate() {

		public void handleAddRow() {

			// stub a new set of fields and add to parent group
			final PropertyPath pp = new PropertyPath(parentPropertyPath);
			pp.indexUnbound();
			parentFieldGroup.addFields(pp.toString(), fieldProvider.getFields());
			fieldGroupPanel.setParentPropertyPath(pp.toString());

			// apply model metadata to the newly created fields
			// TODO fix
			// Model newEntity =
			// AuxDataCache.instance().getEntityPrototype(entityType);
			// assert newEntity != null;
			// editPanel.getFields().bindModel(newEntity.getBindingRef());

			editPanel.setEditMode(true);

			dialog.setText("Add Parameter");
			dialog.center();
		}
	};

	private final String listingElementName;

	private final EntityType entityType;

	private final DataListingWidget<FieldGroup> listing;

	/**
	 * The parent path the points to an indexable property that serves as the
	 * source of the listing data.
	 */
	private final String parentPropertyPath;

	/**
	 * The FieldGroup that contains the fields used as listing data for this
	 * listing.
	 */
	private final FieldGroup parentFieldGroup;

	/**
	 * Provides new field instances of those fields to be shown in the UI. Called
	 * on when <em>adding</em> a listing row.
	 */
	private final IFieldProvider fieldProvider;

	private final DelegatingFieldGroupPanel fieldGroupPanel;

	/**
	 * The single dedicated EditPanel instance employed for adding and editing
	 * listing rows.
	 */
	private final EditPanel editPanel;
	/**
	 * Houses the {@link #editPanel} instance for UI display.
	 */
	private final Dialog dialog;

	/**
	 * The "current" property path of the row subject to editing.
	 */
	private String editPropertyPath;

	/**
	 * Constructor
	 * @param listingElementName
	 * @param entityType
	 * @param columns
	 * @param parentPropertyPath
	 * @param parentFieldGroup
	 * @param fieldProvider
	 * @param fieldRenderer
	 */
	public FieldListing(final String listingElementName, EntityType entityType, final Column[] columns,
			String parentPropertyPath, FieldGroup parentFieldGroup, IFieldProvider fieldProvider, IFieldRenderer fieldRenderer) {

		this.listingElementName = listingElementName;
		this.parentFieldGroup = parentFieldGroup;
		this.parentPropertyPath = parentPropertyPath;
		this.fieldProvider = fieldProvider;
		this.entityType = entityType;

		fieldGroupPanel = new DelegatingFieldGroupPanel(listingElementName, parentFieldGroup, fieldRenderer);

		editPanel = new EditPanel(true, false);
		editPanel.setFieldPanel(fieldGroupPanel);
		editPanel.addEditListener(this);

		dialog = new Dialog(null, false);
		dialog.setWidget(editPanel);
		final IListingConfig<FieldGroup> listingConfig = new IListingConfig<FieldGroup>() {

			public boolean isIgnoreCaseWhenSorting() {
				return true;
			}

			public String getCaption() {
				return listingElementName;
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
				return FieldListing.this.getData();
			}
		};

		listing = ListingFactory.createListingWidget(this, listingConfig, dataProvider);

		initWidget(listing);
	}

	private PropertyPath getRowPropertyPath(int rowIndex, boolean isUnbound) {
		return new PropertyPath(parentPropertyPath, rowIndex - 1, isUnbound);
	}

	private FieldGroup[] getData() {
		final Map<Integer, FieldGroup> map = new HashMap<Integer, FieldGroup>();

		PropertyPath pp = new PropertyPath();

		// get all elidgible fields for the listing
		final Set<IField> set = parentFieldGroup.getFields(parentPropertyPath);

		// split the resultant set of fields..
		for(IField fld : set) {
			pp.parse(fld.getPropertyName());
			// we can safely assume the option property path's depth is 1 since it is
			// always relative to an interface
			pp = pp.nested(1);
			Integer index;
			try {
				index = new Integer(pp.indexAt(pp.depth() - 1));
			}
			catch(MalformedPropPathException e) {
				throw new IllegalStateException();
			}
			if(!map.containsKey(index)) {
				map.put(index, new FieldGroup(listingElementName, fieldGroupPanel, fieldGroupPanel));
			}
			map.get(index).addField(null, fld);
		}

		FieldGroup[] arr = new FieldGroup[map.size()];
		int i = 0;
		for(FieldGroup fg : map.values()) {
			arr[i++] = fg;
		}
		return arr;
	}

	public void onEditEvent(EditEvent event) {
		switch(event.getOp()) {
			case CANCEL:
				fieldGroupPanel.getFields().reset();
				break;
			case SAVE:
				if(editPropertyPath == null) {
					// new entity
					parentFieldGroup.addField(PropertyPath.indexUnbound(parentPropertyPath), fieldGroupPanel.getFields());
				}
				listing.refresh();
				break;
			case DELETE:
				if(editPropertyPath == null) {
					// new entity
					parentFieldGroup.removeField(fieldGroupPanel.getFields());
					listing.refresh();
				}
				else {
					// extisting
					fieldGroupPanel.getFields().addPendingDeletion(editPropertyPath);
				}
				break;
		}
		dialog.hide();
	}

	public void refresh() {
		listing.refresh();
	}
}
