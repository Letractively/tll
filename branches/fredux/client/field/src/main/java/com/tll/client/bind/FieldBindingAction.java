/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldErrorHandler;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.BillboardValidationFeedback;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ScalarError;
import com.tll.client.validate.ValidationException;
import com.tll.common.bind.IModel;

/**
 * FieldBindingAction - Action that binds fields to model properties enabling
 * bi-directional communication between the two.
 * @author jpk
 */
public final class FieldBindingAction implements IBindingAction<FieldGroup> {

	private final GlobalMsgPanel globalMsgPanel;
	
	private final IErrorHandler bindingErrorHandler;

	private final IErrorHandler globalErrorHandler;

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
	 * @param globalMsgPanel the optional global message panel. If specified, a
	 *        global validation error handler will direct feedback to it.
	 */
	public FieldBindingAction(GlobalMsgPanel globalMsgPanel) {
		if(globalMsgPanel == null) throw new IllegalArgumentException();
		this.globalMsgPanel = globalMsgPanel;
		globalErrorHandler = new BillboardValidationFeedback(globalMsgPanel);

		// create the binding error handler
		bindingErrorHandler =
				new ErrorHandlerDelegate(new FieldErrorHandler(new MsgPopupRegistry()));
	}
	
	public GlobalMsgPanel getGlobalMsgPanel() {
		return globalMsgPanel;
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
		
		globalErrorHandler.clear();
		try {
			// validate
			rootGroup.validate(true);
			
			// update the model
			binding.setLeft();
		}
		catch(final ValidationException e) {
			globalErrorHandler.handleError(null, e.getError());
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
			globalErrorHandler.handleError(null, new ScalarError(emsg));
			throw new ValidationException(e.getMessage());
		}
	}

	@Override
	public void set(IBindableWidget<FieldGroup> widget) {
		if(rootGroup != null) throw new IllegalStateException("Already set.");
		Log.debug("FieldBindingAction.set(): " + widget);
		rootGroup = widget.getValue();
		model = widget.getModel();
		ensureSet();
		widget.getValue().setErrorHandler(bindingErrorHandler);
	}
	
	@Override
	public void bind(IBindableWidget<FieldGroup> widget) {
		ensureSet();
		// we only allow binding of the set root field group
		if(widget.getValue() != rootGroup) {
			throw new IllegalArgumentException("May only bind the root field group.");
		}
		final Binding b = new Binding();
		// create bindings for all provided field widgets in the root field group
		for(final IFieldWidget<?> fw : widget.getValue().getFieldWidgets(null)) {
			Log.debug("Binding field: " + fw + " to model [" + model + "]." + fw.getPropertyName());
			b.getChildren().add(
					new Binding(model, fw.getPropertyName(), null, null, null, fw, IBindableWidget.PROPERTY_VALUE, null, fw,
							bindingErrorHandler));
		}
		bind(b);
	}

	@Override
	public void bindIndexed(IBindableWidget<Collection<IModel>> widget, String indexedPropertyName) {
		ensureSet();
		Log.debug("Binding indexed field panel: " + widget);
		// add binding to the many value collection only
		bind(new Binding(model, indexedPropertyName, null, null, null, widget,
				IBindableWidget.PROPERTY_VALUE, null, null, null));
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
		rootGroup = null;
		model = null;
	}
}
