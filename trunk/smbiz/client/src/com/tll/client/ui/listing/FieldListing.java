/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.ui.listing;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tll.client.event.IEditListener;
import com.tll.client.event.type.EditEvent;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.listing.AbstractRowOptions;
import com.tll.client.listing.Column;
import com.tll.client.listing.ITableCellRenderer;
import com.tll.client.model.PropertyPath;
import com.tll.client.ui.Dialog;
import com.tll.client.ui.field.DelegatingFieldGroupPanel;
import com.tll.client.ui.field.EditPanel;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * FieldListing - Listing Widget dedicated to IField type data.
 * @author jpk
 */
public class FieldListing extends DataListingWidget<FieldGroup> implements IEditListener {

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
				// new param
				parentFieldGroup.removeFields(parentFieldGroup.getFields(pp.toString()));
				refresh();
			}
			else {
				// existing param
				parentFieldGroup.addPendingDeletion(pp.toString());
				markRowDeleted(rowIndex);
			}
		}

		@Override
		protected void doEditRow(int rowIndex) {
			final PropertyPath pp = getRowPropertyPath(rowIndex, false);
			editPanel.setEditMode(false);
			dialog.setText("Edit Parameter");
			dialog.center();
		}
	};

	private String listingElementName;

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
	 * Constructor
	 * @param listingElementName
	 * @param parentPropertyPath
	 * @param parentFieldGroup
	 * @param fieldRenderer
	 */
	public FieldListing(String listingElementName, String parentPropertyPath, FieldGroup parentFieldGroup,
			IFieldRenderer fieldRenderer) {
		super(null);
		this.parentFieldGroup = parentFieldGroup;
		this.parentPropertyPath = parentPropertyPath;

		fieldGroupPanel = new DelegatingFieldGroupPanel(listingElementName, parentFieldGroup, fieldRenderer);

		editPanel = new EditPanel(true, false);
		editPanel.setFieldPanel(fieldGroupPanel);
		editPanel.addEditListener(this);

		dialog = new Dialog(null, false);
		dialog.setWidget(editPanel);
	}

	private PropertyPath getRowPropertyPath(int rowIndex, boolean isUnbound) {
		return new PropertyPath(parentPropertyPath, rowIndex - 1, isUnbound);
	}

	private FieldGroup[] getData() {
		final Map<Integer, FieldGroup> map = new HashMap<Integer, FieldGroup>();

		PropertyPath pp = new PropertyPath();

		// get all elidgible fields for the listing
		final Set<IField> set = parentFieldGroup.getFields(parentPropertyPath);

		// split the resultant set of fields based on the parameter portion of the
		// property path
		for(IField fld : set) {
			pp.parse(fld.getPropertyName());
			// we can safely assume the option property path's depth is 1 since it is
			// always relative to an interface
			pp = pp.nested(1);
			Integer paramIndex = new Integer(pp.indexAt(0));
			if(!map.containsKey(paramIndex)) {
				map.put(paramIndex, new FieldGroup(listingElementName, null, null));
			}
			map.get(paramIndex).addField(null, fld);
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
}
