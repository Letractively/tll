/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.Binding;
import com.tll.client.bind.BindingException;
import com.tll.client.model.ModelChangeTracker;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.ErrorDisplay;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;

/**
 * Base field panel support field model binding.
 * @param <W> The widget type employed for field rendering
 * @author jpk
 */
public abstract class AbstractBindableFieldPanel<W extends Widget> extends AbstractFieldPanel<W> implements IFieldBoundWidget {

	/**
	 * The model that is updated with the field data.
	 */
	private Model model;

	/**
	 * The sole binding.
	 */
	private final Binding binding = new Binding();

	/**
	 * Tracks which properties have been altered in the model [optional].
	 */
	private ModelChangeTracker modelChangeTracker;

	/**
	 * Constructor
	 */
	public AbstractBindableFieldPanel() {
		super();
	}

	@Override
	public Model getChangedModel() {
		return (modelChangeTracker == null || model == null) ? null : modelChangeTracker.generateChangeModel(model);
	}

	@Override
	public ModelChangeTracker getModelChangeTracker() {
		return modelChangeTracker;
	}

	@Override
	public void setModelChangeTracker(ModelChangeTracker modelChangeTracker) {
		this.modelChangeTracker = modelChangeTracker;
	}

	@Override
	protected boolean canGenerateFieldGroup() {
		if(!super.canGenerateFieldGroup()) return false;
		if(group == null) {
			if(getIndexedChildren() != null && model == null) {
				// this field panel is dependent on model data for initial display!
				return false;
			}
		}
		return true;
	}

	@Override
	public final void setFieldGroup(FieldGroup fields) {
		super.setFieldGroup(fields);
		if(isBound()) throw new IllegalStateException("Binding already bound");
		final IFieldBoundWidget[] arr = getIndexedChildren();
		if(arr != null) {
			for(final IFieldBoundWidget c : arr) {
				c.setErrorHandler(errorHandler);
				c.setModelChangeTracker(modelChangeTracker);
			}
		}
	}

	@Override
	public final Model getModel() {
		return model;
	}

	/**
	 * Sub-classes may override to handle custom bindings.
	 * @return A newly created {@link IFieldBindingBuilder} impl.
	 */
	protected IFieldBindingBuilder getFieldBindingBuilder() {
		return new DefaultFieldBindingBuilder();
	}

	/**
	 * Creates a new {@link Binding} instance employing a
	 * {@link DefaultFieldBindingBuilder} instance.
	 * @throws BindingException When the bindings are not created for whatever
	 *         reason
	 */
	private void createBindings() throws BindingException {
		assert !isBound();
		Log.debug("Building binding instance..");
		final IFieldBindingBuilder fbb = getFieldBindingBuilder();
		fbb.set(this);
		fbb.createBindings(binding);
	}

	@Override
	public void setModel(Model model) {
		// don't spuriously re-apply the same model instance!
		if(this.model != null && model == this.model) {
			return;
		}
		Log.debug(this + " setting model: [" + model + "]..");

		unbind(); // if not bound no-op

		// clear out the field values since we have different model data
		// NOTE: we don't want to lazily instantite the field group here
		if(group != null) {
			Log.debug("Clearing field values..");
			group.clearValue();
		}

		this.model = model;
		Log.debug("model set");

		if(model != null) {
			bind();
			updateFields();

			// apply property metadata and model new flag (sets incremental validation flag)
			Log.debug("Applying prop metadata to fields..");
			getFieldGroup().applyPropertyMetadata(model, model.isNew());
		}
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		// default is null
		return null;
	}

	@Override
	public void reset() {
		final IIndexedFieldBoundWidget[] indexed = getIndexedChildren();
		if(indexed != null) {
			for(final IIndexedFieldBoundWidget i : indexed) {
				((AbstractFieldPanel<?>)i).reset();
			}
		}
		super.reset();
	}

	@Override
	public void enable(boolean enable) {
		super.enable(enable);
		final IIndexedFieldBoundWidget[] indexed = getIndexedChildren();
		if(indexed != null) {
			for(final IIndexedFieldBoundWidget i : indexed) {
				((AbstractFieldPanel<?>)i).enable(enable);
			}
		}
	}

	@Override
	public final void updateModel() throws NoChangesException, ValidationException, BindingException {
		if(!isBound()) throw new IllegalStateException("Not bound");
		if(errorHandler != null) errorHandler.clear();
		try {
			// validate
			getFieldGroup().validate();

			// update the model
			binding.setLeft();

			if(modelChangeTracker != null && modelChangeTracker.getNumChanges() < 1) {
				throw new NoChangesException();
			}
		}
		catch(final ValidationException e) {
			if(errorHandler != null) errorHandler.handleErrors(e.getErrors(), ErrorDisplay.ALL_FLAGS);
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
			if(errorHandler != null)
				errorHandler.handleError(new Error(ErrorClassifier.CLIENT, null, emsg), ErrorDisplay.ALL_FLAGS);
			throw e;
		}
	}

	@Override
	public void updateFields() throws BindingException {
		if(!isBound()) return;
		if(modelChangeTracker != null) {
			modelChangeTracker.setHandleChanges(false);
		}
		binding.setRight();
		if(modelChangeTracker != null) {
			modelChangeTracker.setHandleChanges(true);
		}
	}

	@Override
	public final void bind() throws BindingException {
		if(isBound()) return;
		if(model == null) {
			throw new IllegalStateException("Can't bind: no model set");
		}
		createBindings();

		Log.debug("Binding: " + this);
		binding.bind();
	}

	@Override
	public final void unbind() {
		if(isBound()) {
			Log.debug("Un-binding: " + this);

			// unbind indexed first
			final IIndexedFieldBoundWidget[] indexedWidgets = getIndexedChildren();
			if(indexedWidgets != null) {
				for(final IIndexedFieldBoundWidget iw : indexedWidgets) {
					Log.debug("Un-binding indexed: " + iw);
					iw.clearIndexed();
				}
			}

			// un-wire the bindings then clear them out
			binding.unbind();
			binding.getChildren().clear();
			if(modelChangeTracker != null) modelChangeTracker.clear();
		}
	}

	@Override
	public final boolean isBound() {
		return binding.getChildren().size() > 0;
	}
}
