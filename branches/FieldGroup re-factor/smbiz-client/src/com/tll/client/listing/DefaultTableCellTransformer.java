/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.listing;

import com.tll.client.data.PropKey;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.ISelfFormattingPropertyValue;
import com.tll.client.model.MalformedPropPathException;
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
	public String[] getCellValues(Model rowData, PropKey[] propKeys, Column[] columns) {
		String[] vals = new String[columns.length];
		final PropertyPath propPath = new PropertyPath();
		for(int i = 0; i < columns.length; i++) {
			Column col = columns[i];
			String prop = col.getPropertyName();
			if(Column.ROW_COUNT_COL_PROP.equals(prop)) {
				vals[i] = null;
			}
			else {
				PropKey pk = findPropKey(prop, propKeys);
				if(pk != null) {
					try {
						propPath.parse(prop);
					}
					catch(MalformedPropPathException e) {
						throw new IllegalStateException(e.getMessage());
					}
					IPropertyValue pv = rowData.getProp(propPath);

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
		}
		return vals;
	}

	/**
	 * Finds the {@link PropKey} for the given property name.
	 * @param propName
	 * @param propKeys
	 * @return The found prop key or <code>null</code> if not found.
	 */
	protected final PropKey findPropKey(String propName, PropKey[] propKeys) {
		for(PropKey element : propKeys) {
			if(element.prop.equals(propName)) {
				return element;
			}
		}
		return null;
	}

}
