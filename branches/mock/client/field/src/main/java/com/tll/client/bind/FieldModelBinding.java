/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldErrorHandler;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldBoundWidget;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.msg.IMsgDisplay;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.BillboardValidationFeedback;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.ErrorDisplay;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ValidationException;
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
	 * @param modelProp
	 * @param modelConverter optional
	 * @param fw
	 * @throws BindingException When the binding creation fails
	 */
	static Binding createBinding(Model model, IConverter<Object, Object> modelConverter, String modelProp,
			IFieldWidget<?> fw, IConverter<Object, Object> fieldConverter) throws BindingException {
		return new Binding(model, modelProp, modelConverter, null, null, fw, IBindableWidget.PROPERTY_VALUE,
				fieldConverter, fw, fw.getErrorHandler());
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
	 * @return the created {@link Binding}.
	 * @throws BindingException When the binding creation fails
	 */
	static Binding createBinding(FieldGroup group, Model model) throws BindingException {
		final Binding b = new Binding();
		// create bindings for all provided field widgets in the root field group
		for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
			// clear the current value to re-mark the field's initial value
			fw.clearValue();
			Log.debug("Binding field group: " + group + " to model [" + model + "]." + fw.getPropertyName());
			try {
				final Binding cb = createBinding(model, null, fw.getPropertyName(), fw, null);
				b.getChildren().add(cb);
			}
			catch(final Exception e) {
				Log.warn("Skipping binding for property: " + fw.getPropertyName());
			}
		}
		return b;
	}

	/**
	 * Convenience builder method that assembles an appropriate
	 * {@link IErrorHandler}.
	 * @param msgDisplay msg display implmentation. May be <code>null</code>.
	 *        providing field validation feedback
	 * @return A new {@link IErrorHandler} impl instance.
	 */
	static IErrorHandler buildErrorHandler(IMsgDisplay msgDisplay) {
		IErrorHandler errorHandler;
		if(msgDisplay == null) {
			errorHandler = new FieldErrorHandler(new MsgPopupRegistry());
		}
		else {
			errorHandler =
				new ErrorHandlerDelegate(new BillboardValidationFeedback(msgDisplay), new FieldErrorHandler(
						new MsgPopupRegistry()));
		}
		return errorHandler;
	}

	/**
	 * The field bound widget. This widget's field group is considered the root
	 * field group.
	 */
	protected IFieldBoundWidget widget;

	/**
	 * Used for displaying validation feedback in a single panel.
	 */
	private IMsgDisplay msgDisplay;

	/**
	 * The sole error handler instance for this binding.
	 */
	private IErrorHandler errorHandler;

	/**
	 * The sole binding.
	 */
	private final Binding binding = new Binding();

	private boolean bound;

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws ValidationException When the root field group fails to validate.
	 * @throws BindingException When a property change event fails to complete
	 *         successfully (post validation).
	 */
	public final void execute() throws ValidationException, BindingException {
		ensureSet();
		final IErrorHandler eh = getErrorHandler();
		eh.clear();
		try {
			// validate
			widget.getFieldGroup().validate();

			// update the model
			binding.setLeft();
		}
		catch(final ValidationException e) {
			eh.handleError(null, e.getError(), ErrorDisplay.ALL_FLAGS);
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
			eh.handleError(null, new Error(ErrorClassifier.CLIENT, emsg), ErrorDisplay.ALL_FLAGS);
			throw e;
		}
	}

	public final void setMsgDisplay(IMsgDisplay msgDisplay) {
		if(errorHandler != null) throw new IllegalStateException("Error handler already set");
		this.msgDisplay = msgDisplay;
	}

	protected final IErrorHandler getErrorHandler() {
		if(errorHandler == null) {
			Log.debug("Building error handler..");
			errorHandler = buildErrorHandler(msgDisplay);
		}
		return errorHandler;
	}

	public final void set(IFieldBoundWidget widget) {
		if(widget == null) {
			throw new IllegalArgumentException("Null widget");
		}
		if(this.widget == widget) {
			return;
		}
		if(widget.getFieldGroup() == null) {
			throw new IllegalArgumentException("No field group specified in field bound widget");
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
		createBindings();
		Log.debug("Binding: " + widget);
		binding.bind();
		binding.setRight();
		bound = true;
	}

	/**
	 * Creates the field/model bindings.
	 * <p>
	 * This is the default implementation and it may be overridden.
	 */
	protected void createBindings() throws BindingException {
		// propagate the binding's error handler
		Log.debug("Propagating error handler: " + widget);
		widget.getFieldGroup().setErrorHandler(getErrorHandler());

		Log.debug("Creating bindings for: " + widget);
		// we only allow binding of the set root field group
		Binding b = createBinding(widget.getFieldGroup(), widget.getModel());
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
				binding.getChildren().add(b);
			}
		}
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
		createBinding(widget.getModel(), null, modelProperty, resolveFieldWidget(fieldName), null);
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
		createBinding(widget.getModel(), modelConverter, modelProperty, resolveFieldWidget(fieldName), fieldConverter);
	}

	private void ensureSet() throws IllegalStateException {
		if(widget == null) {
			throw new IllegalStateException("No field bound widget set.");
		}
	}
}
