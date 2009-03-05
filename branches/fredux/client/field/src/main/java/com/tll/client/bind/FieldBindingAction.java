/**
 * The Logic Lab
 * @author jpk Dec 29, 2008
 */
package com.tll.client.bind;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FieldValidationStyleHandler;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.validate.PanelValidationFeedback;
import com.tll.client.validate.PopupValidationFeedback;
import com.tll.client.validate.ValidationException;
import com.tll.client.validate.ValidationFeedbackDelegate;
import com.tll.common.bind.IBindable;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * FieldBindingAction - Action that binds fields to model properties enabling
 * bi-directional communication between the two.
 * @author jpk
 */
public final class FieldBindingAction implements IBindingAction<FieldGroup, FieldPanel<?>> {
	
	private final Binding binding;
	
	private final MsgPopupRegistry mregistry;

	private final ValidationFeedbackDelegate errorDelegate;
	
	private FieldPanel<?> root;

	/**
	 * Constructor
	 */
	public FieldBindingAction() {
		binding = new Binding();
		mregistry = new MsgPopupRegistry();
		final PopupValidationFeedback popupFeedback = new PopupValidationFeedback(mregistry);
		final PanelValidationFeedback panelFeedback = new PanelValidationFeedback(new GlobalMsgPanel());
		final FieldValidationStyleHandler fieldStyleHandler = new FieldValidationStyleHandler();
		errorDelegate = new ValidationFeedbackDelegate(popupFeedback, panelFeedback, fieldStyleHandler);
	}

	/**
	 * Transfers field data to the model through the defined bindings.
	 * @throws ValidationException When the operation fails
	 */
	@Override
	public final void execute() throws ValidationException {
		if(root == null) throw new IllegalStateException();
		root.getFieldGroup().validate();
		binding.setLeft();
	}

	@Override
	public void set(FieldPanel<?> widget) {
		Log.debug("FieldBindingAction.set(): " + widget);
		if(root == null) {
			this.root = widget;
		}
	}

	@Override
	public void bind(FieldPanel<?> widget) {
		if(root == null) throw new IllegalStateException();
		if(widget == root) {
			final IBindable model = widget.getModel();
			if(model == null) throw new IllegalStateException("No model set on the root field panel.");
			
			// create bindings for all provided field widgets in the root field panel..
			for(final IFieldWidget<?> fw : ((FieldPanel<?>) widget).getFieldGroup().getFieldWidgets(null)) {
				Log.debug("Binding fw: " + fw + " to model [" + model + "]." + fw.getPropertyName());
				// apply property metadata
				if(model instanceof IPropertyMetadataProvider) {
					fw.applyPropertyMetadata((IPropertyMetadataProvider) model);
				}
				binding.getChildren().add(
						new Binding(model, fw.getPropertyName(), null, null, null, fw, IBindableWidget.PROPERTY_VALUE, null, fw,
								errorDelegate));
			}
			
			// bind
			binding.bind();

			// populate the fields
			binding.setRight();
		}
		else if(widget instanceof IndexedFieldPanel) {
			if(((IndexedFieldPanel<?, ?>) widget).getParentFieldPanel() != root) {
				throw new IllegalStateException();
			}
			Log.debug("Binding indexed field panel: " + widget);
			// add binding to the many value collection in the primary binding
			final Binding b =
					new Binding(widget.getModel(), ((IndexedFieldPanel<?, ?>) widget).getIndexedPropertyName(), null, null, null,
							widget, IBindableWidget.PROPERTY_VALUE, null, null, null);
			b.bind();
			b.setRight();
			binding.getChildren().add(b);
		}
		widget.setValidationHandler(errorDelegate);
	}

	@Override
	public void unbind(FieldPanel<?> widget) {
		if(root == widget) {
			binding.unbind();
			binding.getChildren().clear();

			mregistry.clear();
		}
	}
}
