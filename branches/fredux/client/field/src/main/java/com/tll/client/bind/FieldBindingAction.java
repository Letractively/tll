/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FieldValidationStyleHandler;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.PopupValidationFeedback;
import com.tll.client.validate.ScalarError;
import com.tll.client.validate.ValidationException;

/**
 * FieldBindingAction - Action that binds fields to model properties enabling
 * bi-directional communication between the two.
 * @author jpk
 */
public final class FieldBindingAction implements IBindingAction {

	private final IErrorHandler bindingErrorHandler;

	private final IErrorHandler globalErrorHandler;

	/**
	 * The root field panel subject to binding.
	 */
	private FieldPanel<?> root;
	
	Map<IBindableWidget<?>, Binding> bindings = new HashMap<IBindableWidget<?>, Binding>(); 

	/**
	 * Constructor
	 */
	public FieldBindingAction() {
		this(null);
	}

	/**
	 * Constructor
	 * @param globalErrorHandler error handler to employ only when global
	 *        validation is performed. Local (binding) validation error handling
	 *        is taken care of internally.
	 */
	public FieldBindingAction(IErrorHandler globalErrorHandler) {
		this.globalErrorHandler = globalErrorHandler;

		// create the binding error handler
		bindingErrorHandler =
				new ErrorHandlerDelegate(new FieldValidationStyleHandler(), new PopupValidationFeedback(new MsgPopupRegistry()));
	}

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws ValidationException When the operation fails
	 */
	@Override
	public final void execute() throws ValidationException {
		if(root == null) throw new IllegalStateException();
		
		globalErrorHandler.clear();
		try {
			// validate
			root.getFieldGroup().validate();
			
			// update the model
			for(final Binding b : bindings.values()) {
				b.setLeft();
			}
		}
		catch(final ValidationException e) {
			globalErrorHandler.handleError(null, e.getError());
			throw e;
		}
		catch(final Exception e) {
			String emsg;
			if(e.getCause() != null) {
				emsg = e.getCause().getMessage();
			}
			else {
				emsg = e.getMessage();
			}
			globalErrorHandler.handleError(null, new ScalarError(emsg));
			throw new ValidationException(e.getMessage());
		}
	}

	@Override
	public final void set(IBindableWidget<?> widget) {
		if(widget instanceof FieldPanel) {
			if(root != null) throw new IllegalStateException();
			Log.debug("FieldBindingAction.set(): " + widget);
			root = (FieldPanel<?>) widget;
			root.setErrorHandler(bindingErrorHandler);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void bind(IBindableWidget<?> widget) {
		Binding b = null;
		if(widget instanceof IndexedFieldPanel) {
			if(((IndexedFieldPanel) widget).getParentFieldPanel() != root) {
				throw new IllegalArgumentException();
			}
			Log.debug("Binding indexed field panel: " + widget);
			// add binding to the many value collection only
			b =
					new Binding(root.getModel(), ((IndexedFieldPanel<?, ?>) widget).getIndexedPropertyName(), null, null, null,
							widget, IBindableWidget.PROPERTY_VALUE, null, null, null);
		}
		/*else if(root == widget) {*/
		else if(widget instanceof FieldPanel) {
			if(widget.getModel() == null) throw new IllegalArgumentException("No model set");
			final FieldPanel<?> fp = (FieldPanel<?>) widget;
			b = new Binding();
			// create bindings for all provided field widgets in the root field group
			for(final IFieldWidget<?> fw : fp.getFieldGroup().getFieldWidgets(null)) {
				Log.debug("Binding fw: " + fw + " to model [" + fp.getModel() + "]." + fw.getPropertyName());
				b.getChildren().add(
						new Binding(fp.getModel(), fw.getPropertyName(), null, null, null, fw, IBindableWidget.PROPERTY_VALUE,
								null, fw, bindingErrorHandler));
			}
		}

		if(b == null) {
			throw new IllegalArgumentException("Unable to resolve binding.");
		}
		
		bindings.put(widget, b);
		
		// bind
		b.bind();

		// populate the fields
		b.setRight();
	}

	@Override
	public final void unbind(IBindableWidget<?> widget) {
		final Binding b = bindings.get(widget);
		if(b != null) {
			b.unbind();
			b.getChildren().clear();
			bindings.remove(widget);
		}
	}

	@Override
	public void unset(IBindableWidget<?> widget) {
		unbind(widget);
		if(root == widget) {
			root = null;
		}
	}
}
