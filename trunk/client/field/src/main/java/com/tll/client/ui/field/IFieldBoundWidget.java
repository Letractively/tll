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
public interface IFieldBoundWidget extends IFieldGroupProvider, IHasModel {

	/**
	 * @return the indexed children or <code>null<code> if there are none.
	 */
	IIndexedFieldBoundWidget[] getIndexedChildren();

	/**
	 * @return The binding for this field bound widget.
	 */
	FieldModelBinding getBinding();

	/**
	 * Enables or disables the field bound widget. When disabling, all shown
	 * member fields shall be rendered non-interactable.
	 * @param enable Enable or disable?
	 */
	void enable(boolean enable);

	/**
	 * Resets the state of the fields to reflect the current state of model data.
	 */
	void reset();
}
