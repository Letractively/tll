/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.ui.listing;

import java.util.Collection;
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
			editPropertyPath.parse(parentPropertyPath);
			editPropertyPath.indexUnbound();
			parentFieldGroup.addFields(editPropertyPath.toString(), fieldProvider.getFields());
			fieldGroupPanel.setParentPropertyPath(editPropertyPath.toString());

			// apply model metadata only to the target fields
			Model newEntity = AuxDataCache.instance().getEntityPrototype(entityType);
			assert newEntity != null;
			parentFieldGroup.bindModel(editPropertyPath.toString(), newEntity.getBindingRef());

			fieldGroupPanel.draw();

			// reset only the target fields
			final Collection<IField> clc = getEditFields();
			for(IField fld : clc) {
				fld.reset();
			}

			editPanel.setEditMode(true);

			dialog.setText("Add Parameter");
			dialog.center();
		}
	};

	private final String listingElementName;

	private final EntityType entityType;

	private final DataListingWidget<FieldRow> listing;

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
	private final PropertyPath editPropertyPath = new PropertyPath();

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
		return new PropertyPath(parentPropertyPath, rowIndex - 1, isUnbound);
	}

	private FieldRow[] getData() {
		final Map<Integer, Set<IField>> map = new HashMap<Integer, Set<IField>>();

		PropertyPath pp = new PropertyPath();

		// get all elidgible fields for the listing
		Set<IField> set = parentFieldGroup.getFields(parentPropertyPath);

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

	public Set<IField> getEditFields() {
		return parentFieldGroup.getFields(editPropertyPath.toString());
	}

	public void onEditEvent(EditEvent event) {
		assert editPropertyPath.length() > 0;
		switch(event.getOp()) {
			case CANCEL:
				if(editPropertyPath.isUnboundIndexed()) {
					parentFieldGroup.removeFields(parentFieldGroup.getFields(editPropertyPath.toString()));
				}
				break;
			case SAVE:
				listing.refresh();
				break;
			case DELETE:
				if(editPropertyPath.isUnboundIndexed()) {
					// new entity
					parentFieldGroup.removeField(fieldGroupPanel.getFieldGroup());
					listing.refresh();
				}
				else {
					// extisting
					fieldGroupPanel.getFieldGroup().addPendingDeletion(editPropertyPath.toString());
				}
				break;
		}
		dialog.hide();
	}

	public void refresh() {
		listing.refresh();
	}
}
