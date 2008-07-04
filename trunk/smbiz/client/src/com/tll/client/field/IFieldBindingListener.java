/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.field;

import com.tll.client.model.Model;

/**
 * IFieldBindingListener - Definition to embellish field binding before and
 * after the core binding operation.
 * @author jpk
 */
public interface IFieldBindingListener {

	/**
	 * Called just before the given model is bound to a FieldGroup.
	 * @param model The model to be bound.
	 */
	void onBeforeBind(Model model);

	/**
	 * Called just after a Model is bound.
	 */
	void onAfterBind();
}
