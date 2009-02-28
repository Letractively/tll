/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.bind;

import java.util.Set;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FieldValidationFeedback;
import com.tll.client.ui.field.IField;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.bind.IBindable;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.UnsetPropertyException;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * AbstractModelFieldBinding - Common base class for binding actions whose action
 * transfers field data to the model.
 * @author jpk
 */
public abstract class AbstractModelFieldBinding implements IModelFieldBinding {

	/**
	 * the model ref.
	 */
	protected IBindable model;

	/**
	 * The primary binding.
	 */
	protected final Binding binding = new Binding();
	
	/**
	 * The msg popup registry for this binding.
	 */
	protected final MsgPopupRegistry mregistry = new MsgPopupRegistry();

	/**
	 * Aggregation of indexed field panels referenced here for life-cycle
	 * requirements.
	 * <p>
	 * NOTE: We can't aggregate all bindings under one common parent due to the
	 * way indexed properties are handled.
	 */
	//protected final Set<IndexedFieldPanel<?, ?>> indexed = new HashSet<IndexedFieldPanel<?, ?>>();

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

	public void bind() {
		if(!bound) {
			Log.debug("AbstractModelFieldBinding.populateBinding()..");
			try {
				populateBinding();
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException("Unable to bind: " + e.getMessage(), e);
			}
			
			// propagate the mregistry..
			getRootFieldPanel().setMsgPopupRegistry(mregistry);
			
			// bind primary..
			binding.bind();
			binding.setRight(); // populate the fields

			bound = true;
		}
	}

	public void unbind() {
		if(bound) {
			Log.debug("AbstractModelFieldBinding.unbind()..");

			// clear the indexed..
			/*
			for(final IndexedFieldPanel<?, ?> ifp : indexed) {
				ifp.clear();
			}
			*/
			
			// unbind primary
			binding.unbind();
			binding.getChildren().clear();
			bound = false;
		}
	}

	@Override
	public final void setModel(IBindable model) {
		// don't spuriously re-apply the same model instance!
		if(this.model != null && model == this.model) {
			return;
		}
		Log.debug("AbstractModelFieldBinding.setModel() - START");

		if(this.model != null) {
			unbind();
		}

		this.model = model;

		//Log.debug("AbstractModelFieldBinding.setModel() - setting bindable..");
		//setBindable(this);

		//if(isAttached() && model != null) {
		if(model != null) {
			Log.debug("AbstractModelFieldBinding.setModel() - binding..");
			bind();
		}

		// changeSupport.firePropertyChange(PROPERTY_MODEL, old, model);
		Log.debug("AbstractModelFieldBinding.setModel() - END");
	}
	
	/**
	 * Responsible for filling the <code>binding</code> member property.
	 * @throws PropertyPathException
	 */
	protected abstract void populateBinding() throws PropertyPathException;

	/**
	 * Adds an indexed field binding.
	 * @param <I> the index field panel type
	 * @param indexedProperty
	 * @param indexedFieldPanel
	 */
	protected final <I extends FieldPanel<?>, M extends IBindable> void addIndexedFieldBinding(String indexedProperty,
			IndexedFieldPanel<I> indexedFieldPanel) {

		// add binding to the many value collection in the primary binding
		binding.getChildren().add(
				new Binding(model, indexedProperty, null, null, indexedFieldPanel, IBindableWidget.PROPERTY_VALUE, null, null));

		// retain indexed binding ref
		//indexed.add(indexedFieldPanel);
	}

	/**
	 * Adds multiple nested field bindings based on a given parent property path
	 * @param parentPropPath
	 * @throws UnsetPropertyException
	 */
	protected final void addNestedFieldBindings(String parentPropPath)
			throws UnsetPropertyException {
		final Set<IField<?>> fset = getRootFieldPanel().getFieldGroup().getFields(parentPropPath);
		assert fset != null;
		if(fset.size() < 1) {
			throw new UnsetPropertyException(parentPropPath);
		}

		for(final IField<?> f : fset) {
			addFieldBinding(f.getPropertyName(), f);
		}
	}

	/**
	 * Shorthand for adding a child field binding.
	 * @param modelProperty
	 * @throws UnsetPropertyException When the field can't be extracted from the
	 *         given {@link FieldPanel}.
	 */
	protected final void addFieldBinding(String modelProperty) throws UnsetPropertyException {
		addFieldBinding(modelProperty, getRootFieldPanel().getFieldGroup().getField(modelProperty));
	}
	
	/**
	 * Shorthand for adding common model/field bindings.
	 * @param name add model name binding?
	 * @param timestamp add date created/modified bindings?
	 * @throws UnsetPropertyException
	 */
	protected final void addCommonModelFieldBindings(boolean name, boolean timestamp) throws UnsetPropertyException {
		if(name) {
			addFieldBinding(Model.NAME_PROPERTY);
		}
		if(timestamp) {
			addFieldBinding(Model.DATE_CREATED_PROPERTY);
			addFieldBinding(Model.DATE_MODIFIED_PROPERTY);
		}
	}

	/**
	 * Single internal method for actual binding creation.
	 * @param model
	 * @param modelProperty
	 * @param field
	 */
	private void addFieldBinding(String modelProperty, IField<?> field) {
		Log.debug("Binding field: " + field + " to model [" + model + "]." + modelProperty);
		// apply property metadata
		if(model instanceof IPropertyMetadataProvider) {
			field.applyPropertyMetadata((IPropertyMetadataProvider) model);
		}
		binding.getChildren().add(
				new Binding(model, modelProperty, null, null, field, IBindableWidget.PROPERTY_VALUE, field,
						new FieldValidationFeedback(mregistry)));
	}
}
