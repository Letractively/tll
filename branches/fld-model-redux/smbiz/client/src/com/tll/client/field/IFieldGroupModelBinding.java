/**
 * The Logic Lab
 * @author jpk
 * Dec 18, 2008
 */
package com.tll.client.field;

import com.tll.client.event.ISourcesFieldBindingEvents;
import com.tll.client.model.Model;

/**
 * IFieldGroupModelBinding - Contract for bi-directional data transfer between a
 * {@link FieldGroup} and a {@link Model}.
 * @author jpk
 */
public interface IFieldGroupModelBinding extends ISourcesFieldBindingEvents {

	/**
	 * Sets the root {@link FieldGroup} for this binding.
	 * @param fields The required {@link FieldGroup}
	 */
	void setRootFieldGroup(FieldGroup fields);

	/**
	 * Sets the root {@link Model} for this binding.
	 * @param model The required {@link Model}
	 */
	void setRootModel(Model model);

	/**
	 * Gets either the root or a nested model.
	 * @param propPath The property resolving to the model ref property in the
	 *        root model. If <code>null</code>, the root {@link Model} is
	 *        returned.
	 * @return The resolved {@link Model}
	 */
	Model getModel(String propPath);

	/**
	 * Binds the root {@link FieldGroup} to the root {@link Model}.
	 */
	void bind();

	/**
	 * Unbinds the root {@link FieldGroup} from the root {@link Model}.
	 */
	void unbind();

	/**
	 * Data transfer (model -> field).
	 */
	void setFieldValues();

	/**
	 * Data transfer (field -> model).
	 */
	void setModelValues();

}