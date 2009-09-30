/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.Binding;
import com.tll.client.bind.BindingException;
import com.tll.client.model.ModelChangeTracker;
import com.tll.client.validate.Error;
import com.tll.client.validate.ErrorClassifier;
import com.tll.client.validate.ErrorDisplay;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
 * @param <W> The widget type employed for field rendering
 * @author jpk
 */
public abstract class FieldPanel<W extends Widget> extends Composite implements IFieldBoundWidget {

	/**
	 * Styles (field.css)
	 * @author jpk
	 */
	public static final class Styles {

		/**
		 * Style indicating a UI artifact is a field title.
		 */
		public static final String FIELD_TITLE = "fldtitle";

		/**
		 * Style indicating a field panel.
		 */
		public static final String FIELD_PANEL = "fpnl";
	}

	/**
	 * The field group.
	 */
	private FieldGroup group;

	/**
	 * The model that is updated with the field data.
	 */
	private Model model;

	private boolean drawn;

	/**
	 * The sole binding.
	 */
	private final Binding binding = new Binding();

	/**
	 * The sole error handler instance for this binding.
	 */
	private IErrorHandler errorHandler;

	/**
	 * Tracks which properties have been altered in the model [optional].
	 */
	private ModelChangeTracker modelChangeTracker;

	/**
	 * Constructor
	 */
	public FieldPanel() {
		super();
	}

	/**
	 * @return The composite wrapped {@link Widget} the type of which
	 *         <em>must</em> be that of the render widget type.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final W getWidget() {
		return (W) super.getWidget();
	}

	@Override
	protected void initWidget(Widget widget) {
		widget.addStyleName(Styles.FIELD_PANEL);
		super.initWidget(widget);
	}

	/**
	 * Checks the current state of this field panel ensuring the field group is ok
	 * to be generated.
	 * @return true/false
	 */
	protected boolean canGenerateFieldGroup() {
		if(group == null) {
			if(getIndexedChildren() != null && model == null) {
				// this field panel is dependent on model data for initial display!
				return false;
			}
		}
		return true;
	}

	@Override
	public Model getChangedModel() {
		return (modelChangeTracker == null || model == null) ? null : modelChangeTracker.generateChangeModel(model);
	}

	@Override
	public final IErrorHandler getErrorHandler() {
		return errorHandler;
	}

	@Override
	public final void setErrorHandler(IErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	@Override
	public ModelChangeTracker getModelChangeTracker() {
		return modelChangeTracker;
	}

	@Override
	public void setModelChangeTracker(ModelChangeTracker modelChangeTracker) {
		this.modelChangeTracker = modelChangeTracker;
	}

	/**
	 * Generates the fields in the field group if they haven't been created yet.
	 * This guarantees a non-<code> return value.
	 * @throws IllegalStateException When the field group can't be generated based
	 *         on the current state of this panel.
	 */
	@Override
	public final FieldGroup getFieldGroup() throws IllegalStateException {
		if(group == null) {
			if(!canGenerateFieldGroup()) {
				throw new IllegalStateException();
			}
			Log.debug(this + " generating fields..");
			setFieldGroup(generateFieldGroup());
		}
		return group;
	}

	@Override
	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) throw new IllegalArgumentException("Null fields");
		if(this.group == fields) return;
		if(isBound()) throw new IllegalStateException("Binding already bound");
		this.group = fields;
		this.group.setWidget(this);

		// propagate the binding's error handler and model change tracker
		if(errorHandler != null) {
			Log.debug("Propagating error handler for: " + this);
			group.setErrorHandler(errorHandler);
		}
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
	 * @throws BindingException When the bindings are not created for whatever reason
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

		unbind();	// if not bound no-op

		// clear out the field values since we have different model data
		// NOTE: we don't want to lazily instantite the field group here
		if(group != null) {
			Log.debug("Clearing field values..");
			group.clearValue();
		}

		this.model = model;
		Log.debug("model set");



		if(model != null) {
			// apply property metadata and model new flag (sets incremental validation flag)
			// NOTE: first ensure the field group has been generated
			Log.debug("Applying prop metadata to fields..");
			getFieldGroup().applyPropertyMetadata(model, model.isNew());
		}

		if(model != null) {
			bind();
			updateFields();
		}
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		// default is null
		return null;
	}

	@Override
	public void reset() {
		if(group == null) return;
		final IIndexedFieldBoundWidget[] indexed = getIndexedChildren();
		if(indexed != null) {
			for(final IIndexedFieldBoundWidget i : indexed) {
				i.reset();
			}
		}
		group.reset();
	}

	@Override
	public void enable(boolean enable) {
		if(group == null) return;
		group.setEnabled(enable);
		final IIndexedFieldBoundWidget[] indexed = getIndexedChildren();
		if(indexed != null) {
			for(final IIndexedFieldBoundWidget i : indexed) {
				i.enable(enable);
			}
		}
	}

	/**
	 * Provides the field panel renderer (drawer).
	 * @return the renderer
	 */
	protected abstract IFieldRenderer<W> getRenderer();

	/**
	 * Generates the root {@link FieldGroup} this panel references via
	 * {@link #getFieldGroup()}. This method is only called when this panel's
	 * field group reference is <code>null</code>. Therefore, this method may be
	 * circumvented by manually calling {@link #setFieldGroup(FieldGroup)}.
	 * @return A new {@link FieldGroup} instance.
	 */
	protected abstract FieldGroup generateFieldGroup();

	/**
	 * Responsible for rendering the group in the ui. The default is to employ the
	 * provided renderer via {@link #getRenderer()}. Sub-classes may extend this
	 * method to circumvent this strategy thus avoiding the call to
	 * {@link #getRenderer()}.
	 */
	protected void draw() {
		final IFieldRenderer<W> renderer = getRenderer();
		if(renderer != null) {
			Log.debug(this + ": rendering..");
			renderer.render(getWidget(), getFieldGroup());
		}
	}

	@Override
	public final void updateModel() throws ValidationException, BindingException {
		if(!isBound()) throw new IllegalStateException("Not bound");
		if(errorHandler != null) errorHandler.clear();
		try {
			// validate
			getFieldGroup().validate();

			// update the model
			binding.setLeft();
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
		if(modelChangeTracker != null) {
			modelChangeTracker.setHandleChanges(false);
		}
		if(isBound()) binding.setRight();
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

	@Override
	protected void onAttach() {
		Log.debug("Attaching " + this + "..");
		super.onAttach();
	}

	@Override
	protected void onLoad() {
		Log.debug("Loading " + toString() + "..");
		super.onLoad();
		if(!drawn) {
			draw();
			drawn = true;
		}
	}

	@Override
	protected void onDetach() {
		Log.debug("Detatching " + toString() + "..");
		super.onDetach();
	}

	@Override
	public String toString() {
		return "FieldPanel[" + (group == null ? "-nofields-" : group.getName()) + "]";
	}
}
