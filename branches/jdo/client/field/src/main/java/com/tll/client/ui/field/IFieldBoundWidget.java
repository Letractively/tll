/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.bind.FieldModelBinding;
import com.tll.common.model.IHasModel;

/**
 * IFieldBoundWidget
 * @author jpk
 */
public interface IFieldBoundWidget extends IHasFieldGroup, IHasModel {

	/**
	 * @return the indexed children or <code>null<code> if there are none.
	 */
	IIndexedFieldBoundWidget[] getIndexedChildren();

	/**
	 * @return The binding for this bindable widget.
	 */
	FieldModelBinding getBinding();

	/**
	 * Sets the binding.
	 * @param binding
	 */
	void setBinding(FieldModelBinding binding);
}
