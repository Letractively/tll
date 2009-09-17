/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.FieldModelBinding;
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
	 * The optional model.
	 */
	private Model model;

	/**
	 * The binding instance.
	 */
	private FieldModelBinding binding;

	private boolean drawn;

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

	@Override
	public final FieldGroup getFieldGroup() {
		if(group == null) {
			Log.debug(this + " generating fields..");
			setFieldGroup(generateFieldGroup());
		}
		return group;
	}

	protected final void setFieldGroup(FieldGroup fields) {
		if(fields == null) throw new IllegalArgumentException("Null fields");
		if(this.group == fields) return;
		if(binding != null && binding.isBound()) throw new IllegalStateException("Binding already bound");
		this.group = fields;
		this.group.setWidget(this);
	}

	@Override
	public final Model getModel() {
		return model;
	}

	@Override
	public void setModel(Model model) {
		// don't spuriously re-apply the same model instance!
		if(this.model != null && model == this.model) {
			return;
		}
		Log.debug(this + " setting model..");

		if(binding != null && binding.isBound()) {
			binding.unbind();
		}

		// clear out the field values since we have different model data
		if(group != null) {
			group.clearValue();
		}

		this.model = model;

		if(model != null) {
			// apply property metadata and model new flag (sets incremental validation flag)
			getFieldGroup().applyPropertyMetadata(model, model.isNew());
		}

		if(model != null) {
			// use getBinding() to ensure the bindings are first created
			getBinding().bind();
		}
		else if(binding != null) {
			binding.unbind();
		}
	}

	@Override
	public final FieldModelBinding getBinding() {
		if(binding == null) {
			binding = createBinding();
			binding.set(this);
		}
		return binding;
	}

	/**
	 * @return A new binding instance able to bind member fields to model data.
	 *         <p>
	 *         This may be overridden to provide a custom binding type.
	 */
	protected FieldModelBinding createBinding() {
		return new FieldModelBinding();
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		// default is null
		return null;
	}

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
	 * Generates the root {@link FieldGroup} this panel references via {@link #getFieldGroup()}. This
	 * method is only called when this panel's field group reference is <code>null</code>.
	 * Therefore, this method may be circumvented by manually calling
	 * {@link #setFieldGroup(FieldGroup)}.
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
