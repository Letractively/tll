/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.ui.listing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import com.tll.client.cache.AuxDataCache;
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
import com.tll.client.model.IData;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.Model;
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
	 * FieldRow - Dedicated data type for field listings.
	 * @author jpk
	 */
	private static final class FieldRow implements IData {

		/**
		 * The field row data.
		 */
		private final IField[] arr;

		/**
		 * The row index (the numeric index contained w/in the
		 * {@link #indexedPropertyPath}.
		 */
		private final int rowIndex;

		/**
		 * Is this row data unbound?
		 */
		private final boolean unbound;

		/**
		 * Constructor
		 * @param arr The row data array
		 * @param rowIndex The row index
		 * @param unbound Is the row data unbound?
		 */
		public FieldRow(IField[] arr, int rowIndex, boolean unbound) {
			super();
			this.arr = arr;
			this.rowIndex = rowIndex;
			this.unbound = unbound;
		}

		public IField[] getArr() {
			return arr;
		}

		public int getRowIndex() {
			return rowIndex;
		}

		public boolean isUnbound() {
			return unbound;
		}
	}

	/**
	 * The dedicated field type table cell renderer.
	 */
	private static final ITableCellRenderer<FieldRow> cellRenderer = new ITableCellRenderer<FieldRow>() {

		public String getCellValue(FieldRow rowData, Column column) {
			for(IField field : rowData.getArr()) {
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
		protected void doEditRow(int rowIndex) {
			// calculate the target property path and retain in for use in handling
			// ensuing edit event
			FieldRow fr = rowData.get(rowIndex - 1);
			editPropertyPath = new PropertyPath(indexablePropertyPath, rowIndex - 1, fr.isUnbound());

			// extract the target fields and create a separate edit field group
			Set<IField> fields = fieldGroup.getFields(editPropertyPath.toString());
			FieldGroup editGroup = new FieldGroup(fields, entityType.getName(), editPanel);

			fieldGroupPanel.clear();
			fieldGroupPanel.setFieldGroup(editGroup);

			// TODO: need to account for the property path prefix since we are an
			// *existing* property - FIX
			fieldGroupPanel.draw();

			editPanel.setEditMode(false);

			dialog.setText("Edit Parameter");
			dialog.center();
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			FieldRow fr = rowData.get(rowIndex - 1);
			PropertyPath pp = new PropertyPath(indexablePropertyPath, fr.rowIndex, fr.isUnbound());
			if(fr.isUnbound()) {
				// new entity
				fieldGroup.removeFields(fieldGroup.getFields(pp.toString()));
				refresh();
			}
			else {
				// existing entity
				fieldGroup.addPendingDelete(pp.toString());
				listing.markRowDeleted(rowIndex);
			}
		}
	};

	private final IAddRowDelegate addRowDelegate = new IAddRowDelegate() {

		public void handleAddRow() {

			// create new field group to hold the pending add fields
			IField[] fields = fieldProvider.getFields();
			Set<IField> set = new HashSet<IField>();
			Collections.addAll(set, fields);
			FieldGroup addGroup = new FieldGroup(set, listingElementName, editPanel);
			fieldGroupPanel.setFieldGroup(addGroup);
			fieldGroupPanel.clear();

			// apply model metadata to the target fields
			Model newEntity = AuxDataCache.instance().getEntityPrototype(entityType);
			assert newEntity != null;
			addGroup.bindModel(newEntity.getBindingRef());

			fieldGroupPanel.draw();

			editPanel.setEditMode(true);

			dialog.setText("Add Parameter");
			dialog.center();
		}
	};

	private final String listingElementName;

	private final EntityType entityType;

	private final DataListingWidget<FieldRow> listing;

	/**
	 * Points to an indexable property by which target fields in the
	 * {@link #fieldGroup} are resolved for listing data.
	 */
	private final String indexablePropertyPath;

	/**
	 * Contains the {@link IField}s used as listing data.
	 */
	private final FieldGroup fieldGroup;

	/**
	 * The generated row data extracted from the fields in the {@link #fieldGroup}
	 */
	private final List<FieldRow> rowData = new ArrayList<FieldRow>();

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
	private PropertyPath editPropertyPath = new PropertyPath();

	/**
	 * Constructor
	 * @param listingElementName
	 * @param entityType
	 * @param columns
	 * @param indexablePropertyPath
	 * @param fieldGroup
	 * @param fieldProvider
	 * @param fieldRenderer
	 */
	public FieldListing(final String listingElementName, EntityType entityType, final Column[] columns,
			String indexablePropertyPath, FieldGroup fieldGroup, IFieldProvider fieldProvider, IFieldRenderer fieldRenderer) {

		this.listingElementName = listingElementName;
		this.fieldGroup = fieldGroup;
		this.indexablePropertyPath = indexablePropertyPath;
		this.fieldProvider = fieldProvider;
		this.entityType = entityType;

		fieldGroupPanel = new DelegatingFieldGroupPanel(listingElementName, fieldGroup, fieldRenderer);

		editPanel = new EditPanel(true, false);
		editPanel.setFieldGroupPanel(fieldGroupPanel);
		editPanel.addEditListener(this);

		dialog = new Dialog(null, false);
		dialog.setWidget(editPanel);
		final IListingConfig<FieldRow> listingConfig = new IListingConfig<FieldRow>() {

			public boolean isIgnoreCaseWhenSorting() {
				return true;
			}

			public String getCaption() {
				return listingElementName;
			}

			public ITableCellRenderer<FieldRow> getCellRenderer() {
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

		final IDataProvider<FieldRow> dataProvider = new IDataProvider<FieldRow>() {

			public FieldRow[] getData() {
				if(FieldListing.this.rowData.size() < 1) {
					generateRowData();
				}
				return FieldListing.this.rowData.toArray(new FieldRow[FieldListing.this.rowData.size()]);
			}
		};

		listing = ListingFactory.createListingWidget(this, listingConfig, dataProvider);

		initWidget(listing);
	}

	private void generateRowData() {
		assert rowData != null;
		rowData.clear();

		// map of fields keyed by index
		final Map<Integer, Set<IField>> map = new HashMap<Integer, Set<IField>>();
		// map of unbound flags keyed by index
		final Map<Integer, Boolean> map2 = new HashMap<Integer, Boolean>();

		PropertyPath pp = new PropertyPath(indexablePropertyPath);

		final int indexOffset = pp.depth() - 1;
		assert indexOffset >= 0;

		// get all elidgible fields for the listing
		Set<IField> set = fieldGroup.getFields(indexablePropertyPath);

		// split the resultant set of fields..
		for(IField fld : set) {
			pp.parse(fld.getPropertyName());
			Integer index;
			try {
				index = pp.indexAt(indexOffset);
			}
			catch(MalformedPropPathException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
			boolean unbound = pp.isUnboundAt(indexOffset);
			if(!map.containsKey(index)) {
				map.put(index, new HashSet<IField>());
				map2.put(index, unbound ? Boolean.TRUE : Boolean.FALSE);
			}
			map.get(index).add(fld);
		}

		// sort the map by the index key
		final List<Integer> keyList = new ArrayList<Integer>(map.keySet());
		Collections.sort(keyList);

		int i;
		for(Integer index : keyList) {
			Set<IField> fs = map.get(index);
			IField[] farr = new IField[fs.size()];
			i = 0; // reset
			for(IField fld : fs) {
				farr[i++] = fld;
			}
			rowData.add(new FieldRow(farr, index.intValue(), map2.get(index).booleanValue()));
		}
	}

	public void onEditEvent(EditEvent event) {
		switch(event.getOp()) {
			case ADD:
				// create new unbound index property path
				PropertyPath pp = new PropertyPath(indexablePropertyPath);
				pp.indexUnbound(rowData.size());
				// add the fields to the parent field group pre-pending the unbound
				// property path
				fieldGroup.addFields(pp.toString(), editPanel.getFields());
				// NOTE: we fall through
			case UPDATE:
				refresh();
				break;
			case DELETE:
				assert editPropertyPath.length() > 0;
				if(editPropertyPath.isUnboundIndexed()) {
					// new entity
					fieldGroup.removeField(fieldGroupPanel.getFieldGroup());
					refresh();
				}
				else {
					// extisting
					fieldGroupPanel.getFieldGroup().addPendingDelete(editPropertyPath.toString());
				}
				break;
		}
		dialog.hide();
	}

	public void refresh() {
		rowData.clear();
		listing.refresh();
	}
}
