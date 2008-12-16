/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import com.tll.client.validate.ValidationException;

/**
 * IFieldBinding - Serves to commonize needed methods for a field binding and a
 * group of field bindings.
 * @author jpk
 */
public interface IFieldBinding {

	/**
	 * Binds a field to a model property.
	 */
	void bind();

	/**
	 * Un-binds a field from a model property.
	 */
	void unbind();

	/**
	 * Transfers model data to the field.
	 */
	void push();

	/**
	 * Transfers field data to the model property by validating the field and
	 * sending the validated value to the model property. When field validation
	 * fails no data transfer occurrs.
	 * @throws ValidationException When the field validation fails
	 */
	void pull() throws ValidationException;
}
