/**
 * The Logic Lab
 * @author jpk
 * Jan 6, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.bind.Binding;
import com.tll.client.bind.IBindable;
import com.tll.client.validate.ValidationFeedbackManager;

/**
 * FieldBindingHelper
 * @author jpk
 */
public final class FieldBindingHelper {

	/**
	 * Adds a model/field group binding.
	 * @param <M> the model type
	 * @param binding The binding to which the new binding is added
	 * @param model The model
	 * @param fg The field group
	 * @param property The common property path that resolves the target property
	 *        for both the model and the field group
	 */
	public static <M extends IBindable> void addBinding(Binding binding, M model, FieldGroup fg, String property) {
		binding.getChildren().add(
				new Binding(model, null, null, fg, fg.getField(property), ValidationFeedbackManager.instance(), property));
	}

}
