/**
 * The Logic Lab
 * @author jpk
 * @since Sep 27, 2009
 */
package com.tll.client.ui.field;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.bind.Binding;
import com.tll.client.bind.BindingException;
import com.tll.client.convert.IConverter;
import com.tll.client.model.ModelChangeTracker;
import com.tll.client.ui.IBindableWidget;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.model.Model;

/**
 * DefaultFieldBindingBuilder - Default {@link IFieldBindingBuilder}
 * implementation.
 * <p>
 * This class may be extended to realize support custom bindings.
 * @author jpk
 */
public class DefaultFieldBindingBuilder implements IFieldBindingBuilder {

	private IFieldBoundWidget widget;

	/**
	 * Sets the field bound widget for which a {@link Binding} instance is
	 * created.
	 * @param widget
	 */
	@Override
	public void set(IFieldBoundWidget widget) {
		this.widget = widget;
	}

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
	 * @throws IllegalStateException When binding creation can't be started
	 *         because not all required members have been set
	 * @throws BindingException When the binding creation fails
	 */
	@SuppressWarnings("unchecked")
	static Binding createBinding(Model model, IConverter<Object, Object> modelConverter, String modelProp,
			IPropertyChangeListener modelChangeListener, IFieldWidget<?> fw, IConverter<Object, Object> fieldConverter,
			IPropertyChangeListener fieldChangeListener) throws BindingException {
		BindableFieldWidget<?> bfw = new BindableFieldWidget<Object>((IFieldWidget<Object>) fw);
		final Binding b =
			new Binding(model, modelProp, modelConverter, null, null, bfw, IBindableWidget.PROPERTY_VALUE, fieldConverter,
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
	static Binding createBindings(FieldGroup group, Model model, ModelChangeTracker modelChangeTracker)
	throws BindingException {
		Log.debug("Binding field group: " + group + " to model [" + model + "]..");
		final Binding b = new Binding();
		// create bindings for all provided field widgets in the root field group
		for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
			// clear the current value to re-mark the field's initial value
			fw.clearValue();
			try {
				Log.debug("Binding field: " + fw + " to model prop [" + fw.getPropertyName() + "]..");
				final Binding cb = createBinding(model, null, fw.getPropertyName(), modelChangeTracker, fw, null, null);
				b.getChildren().add(cb);
			}
			catch(final Exception e) {
				Log.warn("Skipping field binding for property: " + fw.getPropertyName() + " due to " + e.getMessage());
			}
		}
		return b;
	}

	/**
	 * Creates an atomic binding between a model property and a field widget.
	 * <p>
	 * May be overridden.
	 * @param fw The field widget to bind
	 * @return The created {@link Binding}
	 * @throws BindingException Upon error creating the binding
	 */
	protected Binding createFieldBinding(IFieldWidget<?> fw) throws BindingException {
		return createBinding(widget.getModel(), null, fw.getPropertyName(), widget.getModelChangeTracker(), fw, null, null);
	}

	/**
	 * Hook to add additional bindings <em>after</em> the main binding creation
	 * routine completes.
	 * @param binding The created binding ref
	 */
	protected void postCreateBindings(Binding binding) {
		// default no-op
	}

	@Override
	public final void createBindings(Binding binding) throws BindingException {
		ensureSet();
		Log.debug("Creating bindings for: " + widget);

		final FieldGroup group = widget.getFieldGroup();
		final List<Binding> bchildren = binding.getChildren();
		Binding b;

		for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
			// clear the current value to re-mark the field's initial value
			fw.clearValue();
			try {
				Log.debug("Binding field: " + fw + " to model prop [" + fw.getPropertyName() + "]..");
				b = createFieldBinding(fw);
				bchildren.add(b);
			}
			catch(final BindingException e) {
				Log.warn("Skipping field binding for property: " + fw.getPropertyName() + " due to " + e.getMessage());
			}
		}

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

		// hook to add/tweak
		postCreateBindings(binding);
	}

	/**
	 * Creates a "manual" default binding. This is a provision to allow for
	 * multiple bindings beyond the "stock" bindings that are created
	 * automatically. This binding must first be set.
	 * @param modelProperty the property path that identifies the model property
	 *        to bind
	 * @param fieldName The name of the {@link IFieldWidget} to bind which must be
	 *        a child of the root field group
	 * @return the created binding
	 */
	protected Binding createBinding(String modelProperty, String fieldName) {
		ensureSet();
		return createBinding(widget.getModel(), null, modelProperty, widget.getModelChangeTracker(),
				resolveFieldWidget(fieldName), null, null);
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
	protected final Binding createBinding(String modelProperty, IConverter<Object, Object> modelConverter, String fieldName,
			IConverter<Object, Object> fieldConverter) throws BindingException {
		ensureSet();
		return createBinding(widget.getModel(), modelConverter, modelProperty, widget.getModelChangeTracker(),
				resolveFieldWidget(fieldName), fieldConverter, null);
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

	private void ensureSet() throws IllegalStateException {
		if(widget == null) throw new IllegalStateException("No widget set");
	}
}
