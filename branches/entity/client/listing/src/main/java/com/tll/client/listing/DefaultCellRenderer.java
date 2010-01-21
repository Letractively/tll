/**
 * The Logic Lab
 * @author jpk
 * @since Mar 19, 2009
 */
package com.tll.client.listing;

import com.tll.common.model.Model;

/**
 * DefaultCellRenderer - May be extended for custom row rendering.
 * @author jpk
 */
public class DefaultCellRenderer implements ITableCellRenderer<Model> {

	private static final DefaultCellRenderer instance = new DefaultCellRenderer();

	public static final DefaultCellRenderer get() {
		return instance;
	}

	/**
	 * Constructor
	 */
	private DefaultCellRenderer() {
		super();
	}

	@Override
	public String getCellValue(Model rowData, Column column) {
		return ModelPropertyFormatter.pformat(rowData, column.getPropertyName(), column.getFormat());
	}

}
