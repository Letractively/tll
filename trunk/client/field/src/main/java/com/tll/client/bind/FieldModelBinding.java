/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.convert.IConverter;
import com.tll.client.model.ModelPropertyChangeTracker;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldBoundWidget;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.ErrorDisplay;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ValidationException;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.model.Model;

/**
 * FieldModelBinding - Binds fields to model properties enabling bi-directional
 * model and view data transfer.
 * <p>
 * This class may be extended to accommodate "custom" bindings.
 * @author jpk
 */
public class FieldModelBinding {

	/**
	 * Creates a model/field binding on a particular property. If the binding
	 * can't be created due to a binding exception, <code>null</code> is
	 * returnred.
	 * @param model
	 * @param modelConverter
	 * @param modelProp
	 * @param modelChangeListener Optional model property change listener in
	 *        addition to the default one that takes in data from a field
	 *        generated property change event
	 * @param fw
	 * @param fieldConverter
	 * @param fieldChangeListener Optional field property change listener in
	 *        addition to the default one that takes in data from a model
	 *        generated property change event
	 * @return new binding instance
	 * @throws BindingException When the binding creation fails
	 */
	static Binding createBinding(Model model, IConverter<Object, Object> modelConverter, String modelProp,
			IPropertyChangeListener modelChangeListener, IFieldWidget<?> fw, IConverter<Object, Object> fieldConverter,
			IPropertyChangeListener fieldChangeListener) throws BindingException {
		final Binding b =
			new Binding(model, modelProp, modelConverter, null, null, fw, IBindableWidget.PROPERTY_VALUE, fieldConverter,
					fw, fw.getErrorHandler());
		if(modelChangeListener != null) b.addPropertyChangeListener(modelChangeListener, true);
		if(fieldChangeListener != null) b.addPropertyChangeListener(fieldChangeListener, false);
		return b;
	}

	/**
	 * Generalized binding creation routine to bind a {@link FieldGroup} instance
	 * to an {@link Model} instance.
	 * <p>
	 * <em>This method rests on the assumption that the field property names correlate directly to existant model properties!</em>
	 * <p>
	 * All {@link IFieldWidget}s are extracted from the given field group and
	 * bound to the corresponding model properties.
	 * @param group
	 * @param model
	 * @param modelChangeTracker Optional model change tracker
	 * @return the created {@link Binding}.
	 * @throws BindingException When the binding creation fails
	 */
	static Binding createBinding(FieldGroup group, Model model, ModelPropertyChangeTracker modelChangeTracker)
	throws BindingException {
		final Binding b = new Binding();
		// create bindings for all provided field widgets in the root field group
		for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
			// clear the current value to re-mark the field's initial value
			fw.clearValue();
			Log.debug("Binding field group: " + group + " to model [" + model + "]." + fw.getPropertyName());
			try {
				final Binding cb = createBinding(model, null, fw.getPropertyName(), modelChangeTracker, fw, null, null);
				b.getChildren().add(cb);
			}
			catch(final Exception e) {
				Log.warn("Skipping binding for property: " + fw.getPropertyName());
			}
		}
		return b;
	}

	/**
	 * The field bound widget. This widget's field group is considered the root
	 * field group.
	 */
	protected IFieldBoundWidget widget;

	/**
	 * The sole error handler instance for this binding.
	 */
	private IErrorHandler errorHandler;

	/**
	 * The sole binding.
	 */
	private final Binding binding = new Binding();

	/**
	 * Tracks which properties have been altered in the model.
	 */
	private ModelPropertyChangeTracker modelChangeTracker;

	private boolean bound;

	private Model changedModel;

	public final void setErrorHandler(IErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public final ModelPropertyChangeTracker getModelChangeTracker() {
		return modelChangeTracker;
	}

	public final void setModelChangeTracker(ModelPropertyChangeTracker modelChangeTracker) {
		this.modelChangeTracker = modelChangeTracker;
	}

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws ValidationException When the root field group fails to validate.
	 * @throws BindingException When a property change event fails to complete
	 *         successfully (post validation).
	 */
	public final void execute() throws ValidationException, BindingException {
		ensureSet();
		if(errorHandler != null) errorHandler.clear();
		try {
			// validate
			widget.getFieldGroup().validate();

			// update the model
			binding.setLeft();

			if(modelChangeTracker != null) {
				// generate the changed model
				changedModel = modelChangeTracker.generateChangeModel();
				modelChangeTracker.clear();	// reset
			}
		}
		catch(final ValidationException e) {
			if(errorHandler != null) errorHandler.handleErrors(e.getErrors(), ErrorDisplay.ALL_FLAGS);
			throw e;
		}
		catch(final BindingException e) {
			String emsg;
			if(e.getCause() != null && e.getCause().getMessage() != null) {
				emsg = e.getCause().getMessage();
			}
			else {
				emsg = e.getMessage();
			}
			if(emsg == null) {
				emsg = "Unknown error occurred.";
			}
			if(errorHandler != null)
				errorHandler.handleError(new Error(ErrorClassifier.CLIENT, null, emsg), ErrorDisplay.ALL_FLAGS);
			throw e;
		}
	}

	public final void set(IFieldBoundWidget widget) {
		if(widget == null) {
			throw new IllegalArgumentException("Null widget");
		}
		if(this.widget == widget) {
			return;
		}
		if(this.widget != null) {
			unset();
		}

		Log.debug("Setting: " + widget);
		this.widget = widget;
	}

	public final void bind() throws BindingException {
		if(bound) return;
		if(widget.getModel() == null) {
			throw new IllegalArgumentException("No model specified in field bound widget");
		}
		if(modelChangeTracker != null) {
			//modelChangeTracker.set(widget.getModel());
			modelChangeTracker.setHandleChanges(false);
		}
		createBindings();
		Log.debug("Binding: " + widget);
		binding.bind();
		binding.setRight();
		if(modelChangeTracker != null) modelChangeTracker.setHandleChanges(true);
		bound = true;
	}

	public final void unbind() {
		if(bound) {
			Log.debug("Un-binding: " + widget);
			// unbind indexed first
			final IIndexedFieldBoundWidget[] indexedWidgets = widget.getIndexedChildren();
			if(indexedWidgets != null) {
				for(final IIndexedFieldBoundWidget iw : indexedWidgets) {
					Log.debug("Un-binding indexed: " + iw);
					iw.clearIndexed();
				}
			}

			binding.unbind();
			binding.getChildren().clear();
			if(modelChangeTracker != null) modelChangeTracker.clear();
			changedModel = null;
			bound = false;
		}
	}

	private void unset() {
		if(widget != null) {
			Log.debug("Un-setting: " + widget);
			unbind();
			widget.getFieldGroup().clearValue();
			widget = null;
		}
	}

	public final boolean isBound() {
		return bound;
	}

	/**
	 * @return Sub-set of the underlying model containing only those properties
	 *         that were altered.
	 */
	public final Model getChangedModel() {
		return changedModel;
	}

	/**
	 * Creates the field/model bindings.
	 * <p>
	 * This is the default implementation and it may be overridden.
	 */
	protected void createBindings() throws BindingException {
		// propagate the binding's error handler
		if(errorHandler != null) {
			Log.debug("Propagating error handler: " + widget);
			widget.getFieldGroup().setErrorHandler(errorHandler);
		}

		Log.debug("Creating bindings for: " + widget);
		// we only allow binding of the set root field group
		Binding b = createBinding(widget.getFieldGroup(), widget.getModel(), modelChangeTracker);
		binding.getChildren().add(b);

		// bind the indexed
		final IIndexedFieldBoundWidget[] indexedWidgets = widget.getIndexedChildren();
		if(indexedWidgets != null) {
			for(final IIndexedFieldBoundWidget iw : indexedWidgets) {
				Log.debug("Creating indexed bindings for: " + iw);
				// add binding to the many value collection only
				b =
					new Binding(widget.getModel(), iw.getIndexedPropertyName(), null, null, null, iw,
							IBindableWidget.PROPERTY_VALUE, null, null, null);
				//b.addPropertyChangeListener(modelChangeTracker, true);
				binding.getChildren().add(b);
			}
		}
	}

	/**
	 * Resolves a field name to an {@link IFieldWidget} instance held in the root
	 * field group.
	 * @param fieldName
	 * @return The found {@link IFieldWidget}
	 * @throws BindingException When the field can't be found
	 */
	private IFieldWidget<?> resolveFieldWidget(String fieldName) throws BindingException {
		final IFieldWidget<?> fw = widget.getFieldGroup().getFieldWidget(fieldName);
		if(fw == null) throw new BindingException("Field of name: " + fieldName + " was not found.");
		return fw;
	}

	/**
	 * Creates a "manual" default binding. This is a provision to allow for
	 * multiple bindings beyond the "stock" bindings that are created
	 * automatically. This binding must first be set.
	 * @param modelProperty the property path that identifies the model property
	 *        to bind
	 * @param fieldName The name of the {@link IFieldWidget} to bind which must be
	 *        a child of the root field group
	 */
	public void addBinding(String modelProperty, String fieldName) {
		ensureSet();
		createBinding(widget.getModel(), null, modelProperty, modelChangeTracker, resolveFieldWidget(fieldName), null, null);
	}

	/**
	 * Creates a "manual" binding with the provision to specify both a model and
	 * field converter.
	 * @param modelProperty
	 * @param modelConverter
	 * @param fieldName The name of the {@link IFieldWidget} to bind which must be
	 *        a child of the root field group
	 * @param fieldConverter
	 * @throws BindingException When the binding can't be created
	 */
	public void addBinding(String modelProperty, IConverter<Object, Object> modelConverter, String fieldName,
			IConverter<Object, Object> fieldConverter) throws BindingException {
		ensureSet();
		createBinding(widget.getModel(), modelConverter, modelProperty, modelChangeTracker, resolveFieldWidget(fieldName),
				fieldConverter, null);
	}

	private void ensureSet() throws IllegalStateException {
		if(widget == null) {
			throw new IllegalStateException("No field bound widget set.");
		}
	}
}
