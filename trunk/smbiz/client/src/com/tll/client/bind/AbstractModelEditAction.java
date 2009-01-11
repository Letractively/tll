/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.UnsetPropertyException;
import com.tll.client.ui.IBoundWidget;
import com.tll.client.ui.field.AbstractField;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.validate.ValidationFeedbackManager;

/**
 * AbstractModelEditAction - Common base class for all concrete model edit
 * action classes in the client app.
 * @param <M> the model type
 * @param <FP> the field panel type
 * @author jpk
 */
public abstract class AbstractModelEditAction<M extends IBindable, FP extends FieldPanel<?, M>> implements
		IBindingAction {

	/**
	 * The binding.
	 */
	protected final Binding binding = new Binding();

	@SuppressWarnings("unchecked")
	public <B extends IBindable> void setBindable(B bindable) {
		try {
			populateBinding((FP) bindable);
		}
		catch(ClassCastException e) {
			throw new IllegalArgumentException("The bindable must be a field panel");
		}
		catch(PropertyPathException e) {
			throw new IllegalStateException("Unable to set bindable", e);
		}
	}

	public void bind() {
		binding.bind();
		binding.setRight(); // populate the fields
	}

	public void unbind() {
		binding.unbind();
		binding.getChildren().clear();
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
		binding.getChildren().add(
				new Binding(model, indexedProperty, null, null, indexedFieldPanel, IBoundWidget.PROPERTY_VALUE, null, null));
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

		Set<IField<?>> fset = fieldPanel.getFieldGroup().getFields(parentPropPath);
		assert fset != null;
		if(fset.size() < 1) {
			throw new UnsetPropertyException(parentPropPath);
		}

		for(IField<?> f : fset) {
			binding.getChildren().add(
					new Binding(model, f.getPropertyName(), null, null, f, IBoundWidget.PROPERTY_VALUE, f,
							ValidationFeedbackManager.instance()));
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
		final AbstractField<?> f = fieldPanel.getField(modelProperty);
		final M model = fieldPanel.getModel();
		assert model != null;
		binding.getChildren().add(
				new Binding(model, modelProperty, null, null, f, IBoundWidget.PROPERTY_VALUE, f, ValidationFeedbackManager
						.instance()));
	}
}
