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
import com.tll.common.bind.IBindable;
import com.tll.common.model.Model;

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
 * <p>
 * <em><b>IMPT: </b>The composite wrapped widget is used for field rendering.  Consequently, it must be ensured that types of the two match.</em>
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
	 * The optional binding.
	 */
	private FieldModelBinding binding;

	private boolean drawn;

	/**
	 * Constructor
	 */
	public FieldPanel() {
		super();
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

	@Override
	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) throw new IllegalArgumentException("Null fields");
		if(this.group != fields) {
			if(binding != null && binding.isSet()) throw new IllegalStateException("Already set binding");
			this.group = fields;
			this.group.setWidget(this);
		}
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

		final IBindable old = this.model;

		if(old != null && binding != null) {
			Log.debug(this + " unbinding..");
			binding.unbind();
		}

		getFieldGroup().clearValue();
		this.model = model;

		if(model != null) {
			// apply property metadata
			getFieldGroup().applyPropertyMetadata(model);
			// don't do incremental validation when the model is new
			getFieldGroup().validateIncrementally(!model.isNew());
		}

		if(binding != null) {
			if(model != null) {
				Log.debug(this + " binding..");
				binding.set(this);
				binding.bind();
			}
			else {
				Log.debug(this + " unbinding..");
				binding.unbind();
			}
		}
		//Log.debug("AbstractBindableWidget - firing 'model' prop change event..");
		//changeSupport.firePropertyChange(PropertyChangeType.MODEL.prop(), old, model);
	}

	@Override
	public final FieldModelBinding getBinding() {
		return binding;
	}

	@Override
	public void setBinding(FieldModelBinding binding) {
		if(binding == null) throw new IllegalArgumentException("Null binding");
		if(this.binding != null) throw new IllegalStateException("Binding already set");
		if(model != null) {
			binding.set(this);
			binding.bind();
		}
		this.binding = binding;
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
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		// default is null
		return null;
	}

	/**
	 * Provides the field panel renderer (drawer).
	 * @return the renderer
	 */
	protected abstract IFieldRenderer<W> getRenderer();

	/**
	 * Generates a fresh field group with group this panel will maintain. This
	 * method is only called when this panel's field group reference is null.
	 * Therefore, this method may be circumvented by manually calling
	 * {@link #setFieldGroup(FieldGroup)} prior to DOM attachment.
	 * @return The FieldGroup for this panel ensuring it is first populated.
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
		// if(binding != null) binding.set(this);
		super.onAttach();
		//Log.debug("Firing prop change 'attach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), false, true);
	}

	@Override
	protected void onLoad() {
		Log.debug("Loading " + toString() + "..");
		super.onLoad();
		// if(binding != null) binding.bind();
		if(!drawn) {
			draw();
			drawn = true;
		}
	}

	@Override
	protected void onDetach() {
		Log.debug("Detatching " + toString() + "..");
		super.onDetach();
		//Log.debug("Firing prop change 'detach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), true, false);
	}

	@Override
	public String toString() {
		return "FieldPanel[" + (group == null ? "-nofields-" : group.getName()) + "]";
	}
}
