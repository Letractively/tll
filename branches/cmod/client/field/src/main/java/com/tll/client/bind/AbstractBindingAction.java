/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

import java.util.HashSet;
import java.util.Set;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.IBoundWidget;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FieldValidationFeedback;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.common.bind.IBindable;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.UnsetPropertyException;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * AbstractBindingAction - Common base class for binding actions whose action
 * transfers field data to the model.
 * @param <M> the model type
 * @param <FP> the field panel type
 * @author jpk
 */
public abstract class AbstractBindingAction<M extends IBindable, FP extends FieldPanel<?, M>> implements
		IBindingAction {

	/**
	 * The bindable (field panel) this binding action acts on.
	 */
	FP fieldPanel;

	/**
	 * The primary binding.
	 */
	private final Binding binding = new Binding();

	/**
	 * Aggregation of indexed field panels referenced here for life-cycle
	 * requirements.
	 * <p>
	 * NOTE: We can't aggregate all bindings under one common parent due to the
	 * way indexed properties are handled.
	 */
	private final Set<IndexedFieldPanel<?, ?>> indexed = new HashSet<IndexedFieldPanel<?, ?>>();

	private boolean bound;

	@SuppressWarnings("unchecked")
	public <B extends IBindable> void setBindable(B bindable) {
		if(bound) {
			throw new IllegalStateException("Model edit binding is already bound");
		}
		else if(bindable instanceof FieldPanel == false) {
			throw new IllegalArgumentException("The bindable must be a field panel");
		}
		this.fieldPanel = (FP) bindable;

		if(fieldPanel.getModel() != null) {
			try {
				Log.debug("AbstractBindingAction.populateBinding()..");
				populateBinding(fieldPanel);
			}
			catch(final ClassCastException e) {
				throw new IllegalArgumentException("The bindable must be a field panel");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException("Unable to set bindable", e);
			}
		}
	}
	
	/**
	 * Transfers field data to the model through the defined bindings.
	 */
	@Override
	public final void execute() {
		// populate the model from field data..
		if(!bound) throw new IllegalStateException();
		binding.setLeft();
	}

	public void bind() {
		if(!bound) {
			Log.debug("AbstractBindingAction.bind()..");

			// bind primary..
			binding.bind();
			binding.setRight(); // populate the fields

			bound = true;
		}
	}

	public void unbind() {
		if(bound) {
			Log.debug("AbstractBindingAction.unbind()..");

			// clear the indexed..
			for(final IndexedFieldPanel<?, ?> ifp : indexed) {
				ifp.clear();
			}

			// unbind primary
			binding.unbind();
			binding.getChildren().clear();
			bound = false;
		}
	}

	/**
	 * Responsible for filling the <code>binding</code> member property.
	 * @param bindable
	 * @throws PropertyPathException
	 */
	protected abstract void populateBinding(FP bindable) throws PropertyPathException;

	/**
	 * Adds an indexed field binding.
	 * @param <I> the index field panel type
	 * @param model
	 * @param indexedProperty
	 * @param indexedFieldPanel
	 */
	protected final <I extends FieldPanel<? extends Widget, M>> void addIndexedFieldBinding(M model,
			String indexedProperty, IndexedFieldPanel<I, M> indexedFieldPanel) {

		// add binding to the many value collection in the primary binding
		binding.getChildren().add(
				new Binding(model, indexedProperty, null, null, indexedFieldPanel, IBoundWidget.PROPERTY_VALUE, null, null));

		// retain indexed binding ref
		indexed.add(indexedFieldPanel);
	}

	/**
	 * Adds multiple nested field bindings based on a given parent property path
	 * @param fieldPanel
	 * @param parentPropPath
	 * @throws UnsetPropertyException
	 */
	protected final void addNestedFieldBindings(FP fieldPanel, String parentPropPath) throws UnsetPropertyException {

		final M model = fieldPanel.getModel();
		assert model != null;

		final Set<IField<?, ?>> fset = fieldPanel.getFieldGroup().getFields(parentPropPath);
		assert fset != null;
		if(fset.size() < 1) {
			throw new UnsetPropertyException(parentPropPath);
		}

		for(final IField<?, ?> f : fset) {
			addFieldBinding(model, f.getPropertyName(), f);
		}
	}

	/**
	 * Shorthand for adding a child field binding.
	 * @param fieldPanel
	 * @param modelProperty
	 * @throws UnsetPropertyException When the field can't be extracted from the
	 *         given {@link FieldPanel}.
	 */
	protected final void addFieldBinding(FP fieldPanel, String modelProperty) throws UnsetPropertyException {
		addFieldBinding(fieldPanel.getModel(), modelProperty, fieldPanel.getField(modelProperty));
	}

	/**
	 * Single internal method for actual binding creation.
	 * @param model
	 * @param modelProperty
	 * @param field
	 */
	private void addFieldBinding(M model, String modelProperty, IField<?, ?> field) {
		Log.debug("Binding field: " + field + " to model [" + model + "]." + modelProperty);
		// apply property metadata
		if(model instanceof IPropertyMetadataProvider) {
			field.applyPropertyMetadata((IPropertyMetadataProvider) model);
		}
		binding.getChildren().add(
				new Binding(model, modelProperty, null, null, field, IBoundWidget.PROPERTY_VALUE, field,
						new FieldValidationFeedback()));
	}
}
