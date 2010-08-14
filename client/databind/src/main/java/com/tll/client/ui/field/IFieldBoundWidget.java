/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui.field;

import com.tll.client.bind.BindingException;
import com.tll.client.model.IHasModel;
import com.tll.client.model.IHasModelChangeTracker;
import com.tll.client.validate.IHasErrorHandler;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;

/**
 * IFieldBoundWidget - Defines the api for data transfer between ui field
 * widgets and model data.
 * @author jpk
 */
public interface IFieldBoundWidget extends IHasFieldGroup, IHasModel, IHasErrorHandler, IHasModelChangeTracker {

	/**
	 * @return the indexed children or <code>null<code> if there are none.
	 */
	IIndexedFieldBoundWidget[] getIndexedChildren();

	/**
	 * Binds model data to fields.
	 * @throws BindingException
	 */
	void bind() throws BindingException;

	/**
	 * Unbinds existing bindings.
	 */
	void unbind();

	/**
	 * @return <code>true</code> if the widget is bound.
	 */
	boolean isBound();

	/**
	 * Transfers model data to the fields through the defined bindings. If the
	 * widget is not bound, nothing happens.
	 * @throws BindingException When the data transfer fails through the defined
	 *         bindings.
	 */
	void updateFields() throws BindingException;

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws NoChangesException When no field edits were performed
	 * @throws ValidationException When the root field group fails to validate.
	 * @throws BindingException When a property change event fails to complete
	 *         successfully (post validation).
	 */
	void updateModel() throws NoChangesException, ValidationException, BindingException;

	/**
	 * @return New model instance containing a sub-set of the root model subject
	 *         to editing or <code>null</code> if no properties changed.
	 */
	Model getChangedModel();
}
