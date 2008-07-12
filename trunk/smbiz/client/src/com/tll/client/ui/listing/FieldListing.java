/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.ui.listing;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

		private final IField[] arr;

		/**
		 * Constructor
		 * @param arr
		 */
		public FieldRow(IField[] arr) {
			super();
			this.arr = arr;
		}

		public IField[] getArr() {
			return arr;
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
			editPropertyPath = new PropertyPath(indexablePropertyPath, rowIndex - 1, false);

			// extract the target fields and create a separate edit field group
			Set<IField> fields = fieldGroup.getFields(editPropertyPath.toString());
			FieldGroup editGroup = new FieldGroup(fields, entityType.getName(), editPanel);
			fieldGroupPanel.setFieldGroup(editGroup);

			editPanel.setEditMode(false);

			dialog.setText("Edit Parameter");
			dialog.center();
		}

		@Override
		protected void doDeleteRow(int rowIndex) {
			final PropertyPath pp = getRowPropertyPath(rowIndex, false);
			if(pp.isUnboundIndexed()) {
				// new entity
				fieldGroup.removeFields(fieldGroup.getFields(pp.toString()));
				listing.refresh();
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

			// apply model metadata only to the target fields
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
	 * {@link #fieldGroup} are resolved.
	 */
	private final String indexablePropertyPath;

	/**
	 * Contains the {@link IField}s used as listing data.
	 */
	private final FieldGroup fieldGroup;

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
				return FieldListing.this.getData();
			}
		};

		listing = ListingFactory.createListingWidget(this, listingConfig, dataProvider);

		initWidget(listing);
	}

	private PropertyPath getRowPropertyPath(int rowIndex, boolean isUnbound) {
		return new PropertyPath(indexablePropertyPath, rowIndex - 1, isUnbound);
	}

	private FieldRow[] getData() {
		final Map<Integer, Set<IField>> map = new HashMap<Integer, Set<IField>>();

		PropertyPath pp = new PropertyPath();

		// get all elidgible fields for the listing
		Set<IField> set = fieldGroup.getFields(indexablePropertyPath);

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
				map.put(index, new HashSet<IField>());
			}
			map.get(index).add(fld);
		}

		FieldRow[] arr = new FieldRow[map.size()];
		int i = 0, j = 0;
		for(Set<IField> fs : map.values()) {
			IField[] farr = new IField[fs.size()];
			for(IField fld : fs) {
				farr[i++] = fld;
			}
			arr[j++] = new FieldRow(farr);
		}
		return arr;
	}

	public void onEditEvent(EditEvent event) {
		switch(event.getOp()) {
			case ADD:
				// create new unbound index property path
				PropertyPath pp = new PropertyPath(indexablePropertyPath);
				pp.indexUnbound();
				// add the fields to the parent field group pre-pending the unbound
				// property path
				fieldGroup.addFields(pp.toString(), editPanel.getFields());
				// NOTE: we fall through
			case UPDATE:
				listing.refresh();
				break;
			case DELETE:
				assert editPropertyPath.length() > 0;
				if(editPropertyPath.isUnboundIndexed()) {
					// new entity
					fieldGroup.removeField(fieldGroupPanel.getFieldGroup());
					listing.refresh();
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
		listing.refresh();
	}
}
