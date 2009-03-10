/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.validate.Error;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ValidationException;
import com.tll.client.validate.IErrorHandler.Attrib;
import com.tll.common.bind.IModel;

/**
 * FieldBindingAction - Action that binds fields to model properties enabling
 * bi-directional communication between the two.
 * @author jpk
 */
public final class FieldBindingAction implements IBindingAction<FieldGroup> {

	/**
	 * Generalized binding creation routine to bind a {@link FieldGroup} instance
	 * to an {@link IModel} instance.
	 * <p>
	 * All {@link IFieldWidget}s are extracted from the given field group and
	 * bound to the corresponding model properties with the sole exception that
	 * "managed" model properties are <em>not</em> bound.
	 * @param group
	 * @param model
	 * @param errorHandler
	 * @return the created {@link Binding}.
	 */
	public static Binding createBinding(FieldGroup group, IModel model, IErrorHandler errorHandler) {
		final Binding b = new Binding();
		group.setErrorHandler(errorHandler);
		// create bindings for all provided field widgets in the root field group
		for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
			Log.debug("Binding field group: " + group + " to model [" + model + "]." + fw.getPropertyName());
			b.getChildren().add(
					new Binding(model, fw.getPropertyName(), null, null, null, fw, IBindableWidget.PROPERTY_VALUE, null, fw,
							errorHandler));
		}
		return b;
	}

	private final IErrorHandler errorHandler;

	/**
	 * The root field group.
	 */
	private FieldGroup rootGroup;

	/**
	 * The [root] model.
	 */
	private IModel model;

	/**
	 * The sole binding.
	 */
	private final Binding binding = new Binding();

	/**
	 * Constructor
	 */
	public FieldBindingAction() {
		this(null);
	}

	/**
	 * Constructor
	 * @param errorHandler the error handler to employ
	 */
	public FieldBindingAction(IErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	private void ensureSet() throws IllegalStateException {
		if(rootGroup == null) {
			throw new IllegalStateException("Not root field group set.");
		}
		if(model == null) {
			throw new IllegalStateException("Not model set.");
		}
	}

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws ValidationException When the operation fails
	 * @throws IllegalStateException When the action state is invalid.
	 */
	@Override
	public void execute() throws IllegalStateException, ValidationException {
		ensureSet();

		if(errorHandler != null) errorHandler.clear();
		try {
			// validate
			rootGroup.validate();

			// update the model
			binding.setLeft();
		}
		catch(final ValidationException e) {
			if(errorHandler != null) errorHandler.handleError(null, e.getError(), Attrib.GLOBAL.flag());
			throw e;
		}
		catch(final Exception e) {
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
			if(errorHandler != null) errorHandler.handleError(null, new Error(emsg), Attrib.GLOBAL.flag());
			throw new ValidationException(e.getMessage());
		}
	}

	@Override
	public void set(IBindableWidget<FieldGroup> widget) {
		if(rootGroup == null) {
			Log.debug("FieldBindingAction.set(): " + widget);
			rootGroup = widget.getValue();
			model = widget.getModel();
			ensureSet();
		}
	}

	@Override
	public void bind(IBindableWidget<FieldGroup> widget) {
		ensureSet();
		// we only allow binding of the set root field group
		if(widget.getValue() != rootGroup) {
			throw new IllegalArgumentException("Can only bind the field group of the set widget.");
		}
		bind(createBinding(rootGroup, model, errorHandler));
	}

	@Override
	public void bindIndexed(IBindableWidget<Collection<IModel>> widget, String indexedPropertyName) {
		ensureSet();
		Log.debug("Binding indexed field panel: " + widget);
		// add binding to the many value collection only
		bind(new Binding(model, indexedPropertyName, null, null, null, widget, IBindableWidget.PROPERTY_VALUE, null, null,
				null));
	}

	private void bind(Binding b) {
		binding.getChildren().add(b);

		// bind
		b.bind();

		// populate the fields
		b.setRight();
	}

	@Override
	public void unbind(IBindableWidget<FieldGroup> widget) {
		if(widget == null || widget.getValue() != rootGroup) {
			throw new IllegalArgumentException();
		}
		binding.unbind();
		binding.getChildren().clear();
	}

	@Override
	public void unset(IBindableWidget<FieldGroup> widget) {
		unbind(widget);
		rootGroup.clearValue();
		rootGroup = null;
		model = null;
	}
}
