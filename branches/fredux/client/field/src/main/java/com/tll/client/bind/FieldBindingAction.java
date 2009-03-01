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
	 * Aggregation of the bound widgets and their corresponding bindings.
	 */
	private final Map<IBindableWidget<?>, Binding> bindings = new HashMap<IBindableWidget<?>, Binding>();

	/**
	 * Transfers field data to the model through the defined bindings.
	 */
	@Override
	public final void execute() {
		for(final Binding b : bindings.values()) {
			b.setLeft();
		}
	}

	@Override
	public void set(IBindableWidget<?> widget) {
		Log.debug("FieldBindingAction.set(): " + widget);
		if(bindings.containsKey(widget)) {
			throw new IllegalArgumentException();
		}
		bindings.put(widget, new Binding());
	}

	@Override
	public void bind(IBindableWidget<?> widget) {
		if(!bindings.containsKey(widget)) throw new IllegalStateException(); // not set!
		Log.debug("FieldBindingAction.bind(): " + widget);

		// get an mregistry
		final MsgPopupRegistry mregistry = new MsgPopupRegistry();

		// generate the binding for this widget
		final Binding b = bindings.get(widget);
		assert b != null;
		try {
			populateBinding(b, widget, mregistry);
		}
		catch(final PropertyPathException e) {
			throw new IllegalStateException("Unable to bind: " + e.getMessage(), e);
		}

		// bind..
		b.bind();
		b.setRight(); // populate the fields
	}

	@Override
	public void unbind(IBindableWidget<?> widget) {
		if(bindings.containsKey(widget)) {
			Log.debug("FieldBindingAction.unbind(): " + widget);

			// unbind the primary bindings..
			final Binding b = bindings.get(widget);
			b.unbind();
			b.getChildren().clear();

			bindings.remove(widget);
		}
	}

	/**
	 * Generates bindings for the given bindable widget.
	 * @param b the binding to populate
	 * @param widget the bindable widget
	 * @param mregistry the message registry
	 * @throws PropertyPathException
	 */
	private void populateBinding(Binding b, IBindableWidget<?> widget, MsgPopupRegistry mregistry)
			throws PropertyPathException {
		assert widget != null;
		if(widget instanceof IndexedFieldPanel) {
			addIndexedBinding(b, (IndexedFieldPanel<?>) widget);
		}
		else if(widget instanceof FieldPanel) {
			final IBindable model = widget.getModel();
			if(model == null) throw new IllegalStateException("No model set.");
			for(final IFieldWidget<?> fw : ((FieldPanel<?>) widget).getFieldGroup().getFieldWidgets(null)) {
				// TODO check for field binding elidgibility somehow!!
				addFieldBinding(b, model, fw, mregistry);
			}
		}
		else {
			throw new IllegalArgumentException("Unsupported bindable widget type: " + widget.getClass());
		}
		widget.setMsgPopupRegistry(mregistry);
	}

	private void addIndexedBinding(Binding binding, IndexedFieldPanel<?> indexedPanel) {
		Log.debug("Binding indexed field panel: " + indexedPanel);
		// add binding to the many value collection in the primary binding
		binding.getChildren().add(
				new Binding(indexedPanel.getModel(), indexedPanel.getIndexedPropertyName(), null, null, indexedPanel,
						IBindableWidget.PROPERTY_VALUE, null, null));
	}

	/**
	 * Adds an "atomic" field widget/model binding.
	 * @param binding
	 * @param model
	 * @param modelProperty
	 * @param field
	 * @param mregistry
	 */
	private void addFieldBinding(Binding binding, IBindable model, IFieldWidget<?> field, MsgPopupRegistry mregistry) {
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
