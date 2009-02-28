/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FieldValidationFeedback;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.bind.IBindable;
import com.tll.common.model.PropertyPathException;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * FieldBindingAction - Action that binds fields to model properties enabling
 * bi-directional communication between the two.
 * @author jpk
 */
public final class FieldBindingAction implements IBindingAction {

	/**
	 * The primary binding.
	 */
	protected final Binding binding = new Binding();

	/**
	 * Aggregation of the bound widgets for this model/field binding instance.
	 */
	//private final Set<IBindableWidget<?>> boundWidgets = new HashSet<IBindableWidget<?>>();

	/**
	 * Flag to indicate whether or not we are bound.
	 */
	private boolean bound;

	/**
	 * Transfers field data to the model through the defined bindings.
	 */
	@Override
	public final void execute() {
		// populate the model from field data..
		if(!bound) throw new IllegalStateException();
		binding.setLeft();
	}

	@Override
	public void set(IBindableWidget<?> widget) {
		/*
		if(!boundWidgets.add(widget)) {
			throw new IllegalArgumentException();
		}
		*/
		// propagate an mregistry instance..
		widget.setMsgPopupRegistry(new MsgPopupRegistry());
	}

	@Override
	public void bind(IBindableWidget<?> widget) {
		if(!bound) {
			Log.debug("FieldBindingAction.populateBinding()..");

			// populate binding..
			try {
				populateBinding(widget);
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException("Unable to bind: " + e.getMessage(), e);
			}
			
			// bind primary..
			binding.bind();
			binding.setRight(); // populate the fields

			bound = true;
		}
	}

	@Override
	public void unbind(IBindableWidget<?> widget) {
		if(bound) {
			Log.debug("FieldBindingAction.unbind()..");

			// unbind the primary bindings..
			binding.unbind();
			binding.getChildren().clear();

			// clear out the bound widgets..
			//boundWidgets.clear();
			
			bound = false;
		}
	}

	/**
	 * Adds bindings for a single bindable widget.
	 * @param widget the bindable widget being bound
	 * @throws PropertyPathException
	 */
	private void populateBinding(IBindableWidget<?> widget) throws PropertyPathException {
		assert widget != null;
		final MsgPopupRegistry mregistry = widget.getMsgPopupRegistry();
		final IBindable model = widget.getModel();
		if(widget instanceof IndexedFieldPanel) {
			addIndexedBinding((IndexedFieldPanel<?>) widget);
		}
		else if(widget instanceof FieldPanel) {
			for(final IFieldWidget<?> fw : ((FieldPanel<?>) widget).getFieldGroup().getFieldWidgets(null)) {
				// TODO check for field binding elidgibility somehow!!
				addFieldBinding(model, fw, mregistry);
			}
		}
		else {
			throw new IllegalArgumentException("Unsupported bindable widget type: " + widget.getClass());
		}

		// retain binding ref
		//boundWidgets.add(widget);
	}
	
	private void addIndexedBinding(IndexedFieldPanel<?> indexedPanel) {
		// add binding to the many value collection in the primary binding
		binding.getChildren().add(
				new Binding(indexedPanel.getModel(), indexedPanel.getIndexedPropertyName(), null, null, indexedPanel,
						IBindableWidget.PROPERTY_VALUE, null, null));

	}

	/**
	 * Adds an "atomic" field widget/model binding.
	 * @param model
	 * @param modelProperty
	 * @param field
	 * @param mregistry
	 */
	private void addFieldBinding(IBindable model, IFieldWidget<?> field, MsgPopupRegistry mregistry) {
		Log.debug("Binding field: " + field + " to model [" + model + "]." + field.getPropertyName());
		// apply property metadata
		if(model instanceof IPropertyMetadataProvider) {
			field.applyPropertyMetadata((IPropertyMetadataProvider) model);
		}
		binding.getChildren().add(
				new Binding(model, field.getPropertyName(), null, null, field, IBindableWidget.PROPERTY_VALUE, field,
						new FieldValidationFeedback(mregistry)));
	}
}
