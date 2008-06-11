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
 * DefaultTableCellTransformer - The default {@link ITableCellTransformer}.
 * @author jpk
 */
public class DefaultTableCellTransformer implements ITableCellTransformer {

	/**
	 * Default implementation of row value translation. Sub-classes are
	 * responsible for overriding this method to facilitate special cases.
	 */
	public String[] getCellValues(Model rowData, Column[] columns) {
		String[] vals = new String[columns.length];
		final PropertyPath propPath = new PropertyPath();
		for(int i = 0; i < columns.length; i++) {
			Column col = columns[i];
			String prop = col.getPropertyName();
			if(Column.ROW_COUNT_COL_PROP.equals(prop)) {
				vals[i] = null;
			}
			else if(prop != null) {
				propPath.parse(prop);
				IPropertyValue pv = rowData.getValue(propPath);

				// self formatting type..
				if(pv.getType().isSelfFormatting()) {
					vals[i] = ((ISelfFormattingPropertyValue) pv).asString();
				}

				// format the value..
				else {
					vals[i] = Fmt.format(pv.getValue(), col.format);
				}
			}
			else {
				vals[i] = null;
			}
		}
		return vals;
	}
}
