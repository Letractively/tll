/**
 * The Logic Lab
 * @author jpk
 * Dec 18, 2008
 */
package com.tll.client.field;

import com.tll.client.event.IFieldBindingListener;
import com.tll.client.event.ISourcesFieldBindingEvents;
import com.tll.client.model.Model;
import com.tll.model.EntityType;

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
	 * @return The root field group
	 */
	FieldGroup getRootFieldGroup();

	/**
	 * Resolves a single, possibly nested <em>non-indexed</em> model under the
	 * root model of the given type. The resolved model may be the root model.
	 * <p>
	 * This serves as a way for {@link IFieldBindingListener}s to obtain a needed
	 * model reference in a loosely coupled way.
	 * @return The resolved non-<code>null</code> model
	 * @throws IllegalArgumentException When the given model type is not supported
	 *         by this binding.
	 * @throws IllegalStateException When the target model is not preset (
	 *         <code>null</code>) under the root model but is supported.
	 */
	Model resolveModel(EntityType type) throws IllegalArgumentException, IllegalStateException;

	/**
	 * Binds the root {@link FieldGroup} to the root {@link Model}.
	 */
	void bind();

	/**
	 * Unbinds the root {@link FieldGroup} from the root {@link Model} destroying
	 * all added field bindings.
	 */
	void unbind();

	/**
	 * Unbinds the given field removing all related field bindings. This binding
	 * must already be bound.
	 * @param field The field to unbind which may be a nested {@link FieldGroup}.
	 */
	void unbindField(IField field);

	/**
	 * Adds field bindings based on an indexed model. This binding must already be
	 * bound and as such this is operation is considered a "late binding".
	 * @param fields The field subject to binding to the indexed model. May be a
	 *        {@link FieldGroup}.
	 * @param relatedManyPropPath Resolves to the related many model property
	 *        under the root model defining how the given fields will map back to
	 *        the root model
	 * @param modelType The required model type used to obtain a prototype model
	 *        instance in which to bind
	 * @return The resultant property path of the bound indexed model
	 * @throws IllegalArgumentException When a property path related or other type
	 *         of error occurs
	 */
	String bindIndexedModel(IField field, String relatedManyPropPath, EntityType modelType);

	/**
	 * Schedules or un-schedules a nested model under the root model for deletion.
	 * @param modelPropPath The target model property path relative to the bound
	 *        root model.
	 * @param markDeleted Mark or un-mark as deleted?
	 */
	void markDeleted(String modelPropPath, boolean markDeleted);

	/**
	 * Is the given model path scheduled for deletion?
	 * @param modelPropPath The model path relative to the root model
	 * @return true/false
	 */
	boolean isMarkedDeleted(String modelPropPath);

	/**
	 * Data transfer (model -> field).
	 */
	void setFieldValues();

	/**
	 * Data transfer (field -> model).
	 */
	void setModelValues();
}