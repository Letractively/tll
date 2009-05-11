/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldBoundWidget;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;

/**
 * FieldBinding - Binds fields to model properties enabling bi-directional model
 * and view data transfer.
 * @author jpk
 */
public final class FieldBinding {

	/**
	 * Creates a model/field binding on a particular property. If the binding
	 * can't be created due to a binding exception, <code>null</code> is
	 * returnred.
	 * @param model
	 * @param modelProp
	 * @param fw
	 * @param eh
	 */
	static Binding createBinding(Model model, String modelProp, IFieldWidget<?> fw, IErrorHandler eh) {
		try {
			return new Binding(model, fw.getPropertyName(), null, null, null, fw, IBindableWidget.PROPERTY_VALUE, null, fw,
					eh);
		}
		catch(final RuntimeException e) {
			Log.warn("Skipping binding for property: " + modelProp);
			return null;
		}
	}

	/**
	 * Generalized binding creation routine to bind a {@link FieldGroup} instance
	 * to an {@link Model} instance.
	 * <p>
	 * All {@link IFieldWidget}s are extracted from the given field group and
	 * bound to the corresponding model properties with the sole exception that
	 * "managed" model properties are <em>not</em> bound.
	 * @param group
	 * @param model
	 * @param errorHandler
	 * @return the created {@link Binding}.
	 */
	static Binding createBinding(FieldGroup group, Model model, IErrorHandler errorHandler) {
		final Binding b = new Binding();
		group.setErrorHandler(errorHandler);
		// create bindings for all provided field widgets in the root field group
		for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
			// clear the current value to re-mark the field's initial value
			fw.clearValue();
			Log.debug("Binding field group: " + group + " to model [" + model + "]." + fw.getPropertyName());
			final Binding cb = createBinding(model, fw.getPropertyName(), fw, errorHandler);
			if(cb != null) {
				b.getChildren().add(cb);
			}
		}
		return b;
	}

	/**
	 * The local error handler
	 */
	private final IErrorHandler errorHandler;

	/**
	 * The field bound widget.
	 */
	private IFieldBoundWidget widget;

	/**
	 * The sole binding.
	 */
	private final Binding binding = new Binding();

	private boolean bound;

	/**
	 * Constructor
	 * @param errorHandler The error handler to employ.
	 */
	public FieldBinding(IErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * @return The composite error handler.
	 */
	public IErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws ValidationException When the operation fails
	 * @throws IllegalStateException When the action state is invalid.
	 */
	public void execute() throws IllegalStateException, ValidationException {
		ensureSet();

		if(errorHandler != null) errorHandler.clear();
		try {
			// validate
			widget.getFieldGroup().validate();

			// update the model
			binding.setLeft();
		}
		catch(final ValidationException e) {
			if(errorHandler != null) {
				errorHandler.handleError(null, e.getError());
			}
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
			if(errorHandler != null) {
				errorHandler.handleError(null, new Error(ErrorClassifier.CLIENT, emsg));
			}
			throw new ValidationException(emsg);
		}
	}

	public void set(IFieldBoundWidget widget) {
		if(widget == null) {
			throw new IllegalArgumentException("Null widget");
		}
		if(this.widget == widget) {
			return;
		}
		if(widget.getFieldGroup() == null) {
			throw new IllegalArgumentException("No field group");
		}
		if(widget.getModel() == null) {
			throw new IllegalArgumentException("No model");
		}
		if(this.widget != null) {
			unset();
		}
		Log.debug("Setting: " + widget);
		this.widget = widget;
	}

	public void bind() {
		ensureSet();
		if(bound) return;
		Log.debug("Binding: " + widget);
		// we only allow binding of the set root field group
		bind(createBinding(widget.getFieldGroup(), widget.getModel(), errorHandler));

		// bind the indexed
		final IIndexedFieldBoundWidget[] indexedWidgets = widget.getIndexedChildren();
		if(indexedWidgets != null) {
			for(final IIndexedFieldBoundWidget iw : indexedWidgets) {
				Log.debug("Binding indexed: " + iw);
				// add binding to the many value collection only
				bind(new Binding(widget.getModel(), iw.getIndexedPropertyName(), null, null, null, iw,
						IBindableWidget.PROPERTY_VALUE,
						null, null, null));

			}
		}
		bound = true;
	}

	public void unbind() {
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
			bound = false;
		}
	}

	public void unset() {
		if(isSet()) {
			Log.debug("Un-setting: " + widget);
			unbind();
			widget.getFieldGroup().clearValue();
			widget = null;
		}
	}

	public boolean isSet() {
		return widget != null;
	}

	public boolean isBound() {
		return bound;
	}

	private void ensureSet() throws IllegalStateException {
		if(widget == null) {
			throw new IllegalStateException("Not field bound widget set.");
		}
	}

	private void bind(Binding b) {
		binding.getChildren().add(b);

		// bind
		b.bind();

		// populate the fields
		b.setRight();
	}
}
