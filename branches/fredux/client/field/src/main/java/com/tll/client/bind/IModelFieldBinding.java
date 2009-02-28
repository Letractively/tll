/**
 * The Logic Lab
 * @author jpk
 * Feb 27, 2009
 */
package com.tll.client.bind;

import com.tll.client.ui.field.FieldPanel;
import com.tll.common.bind.IBindable;

/**
 * IModelFieldBinding - Definition for a model <--> field binding action.
 * @author jpk
 */
public interface IModelFieldBinding extends IBindingAction {

	/**
	 * Sets the "root" model in this model/field binding action.
	 * @param model the bindable model
	 */
	void setModel(IBindable model);
	
	/**
	 * @return The root field panel.
	 */
	FieldPanel<?> getRootFieldPanel();
}
