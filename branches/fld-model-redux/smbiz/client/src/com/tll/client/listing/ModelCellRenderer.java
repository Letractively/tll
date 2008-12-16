/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.listing;

import com.tll.client.model.IPropertyValue;
import com.tll.client.model.ISelfFormattingPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.util.Fmt;

/**
 * ModelCellRenderer - Table cell renderer for {@link Model} row data.
 * @author jpk
 */
public final class ModelCellRenderer implements ITableCellRenderer<Model> {

	public String getCellValue(Model rowData, Column column) {
		IPropertyValue pv = rowData.getPropertyValue(new PropertyPath(column.getPropertyName()));

		// self formatting type..
		if(pv.getType().isSelfFormatting()) {
			return ((ISelfFormattingPropertyValue) pv).asString();
		}

		// format the value..
		return Fmt.format(pv.getValue(), column.getFormat());
	}
}
