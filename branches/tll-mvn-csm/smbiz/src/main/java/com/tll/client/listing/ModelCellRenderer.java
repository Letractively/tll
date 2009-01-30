/**
 * The Logic Lab
 * @author jpk Jan 19, 2008
 */
package com.tll.client.listing;

import com.tll.common.model.IPropertyValue;
import com.tll.common.model.ISelfFormattingPropertyValue;
import com.tll.common.model.Model;
import com.tll.common.model.NullNodeInPropPathException;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.UnsetPropertyException;
import com.tll.common.util.Fmt;

/**
 * ModelCellRenderer - Table cell renderer for {@link Model} row data.
 * @author jpk
 */
public final class ModelCellRenderer implements ITableCellRenderer<Model> {

	public String getCellValue(Model rowData, Column column) {
		IPropertyValue pv;
		try {
			// resolve the property
			pv = rowData.getPropertyValue(column.getPropertyName());

			// self formatting type?
			if(pv.getType().isSelfFormatting()) {
				return ((ISelfFormattingPropertyValue) pv).asString();
			}

			// format the value..
			return Fmt.format(pv.getValue(), column.getFormat());
		}
		catch(UnsetPropertyException e) {
			return null;
		}
		catch(NullNodeInPropPathException e) {
			return null;
		}
		catch(PropertyPathException e) {
			throw new IllegalStateException(e);
		}
	}
}
