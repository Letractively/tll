/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.bind.IBindingAction;
import com.tll.common.bind.IHasModel;

/**
 * IFieldBoundWidget
 * @author jpk
 */
public interface IFieldBoundWidget extends IHasFieldGroup, IHasModel {

	/**
	 * @return the indexed children of <code>null<code> if there are none.
	 */
	IIndexedFieldBoundWidget[] getIndexedChildren();

	/**
	 * @return The action for this bindable widget.
	 */
	IBindingAction getAction();

	/**
	 * Sets the action.
	 * @param action
	 */
	void setAction(IBindingAction action);
}
