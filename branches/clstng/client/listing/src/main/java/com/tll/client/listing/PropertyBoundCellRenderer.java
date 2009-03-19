/**
 * The Logic Lab
 * @author jpk
 * @since Mar 19, 2009
 */
package com.tll.client.listing;

import com.tll.common.model.Model;

/**
 * PropertyBoundCellRenderer
 * @author jpk
 */
public class PropertyBoundCellRenderer implements ITableCellRenderer<Model, PropertyBoundColumn> {
	
	private static final PropertyBoundCellRenderer instance = new PropertyBoundCellRenderer();

	public static final PropertyBoundCellRenderer get() {
		return instance;
	}

	/**
	 * Constructor
	 */
	private PropertyBoundCellRenderer() {
		super();
	}

	@Override
	public String getCellValue(Model rowData, PropertyBoundColumn column) {
		return ModelPropertyFormatter.pformat(rowData, column.getPropertyName(), column.getFormat());
	}

}
