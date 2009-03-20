/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindingAction;
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
	 * The field group.
	 */
	private FieldGroup group;

	/**
	 * The optional model.
	 */
	private Model model;

	/**
	 * The optional action.
	 */
	private IBindingAction action;

	private boolean drawn;

	/**
	 * Constructor
	 */
	public FieldPanel() {
		super();
	}

	public final FieldGroup getFieldGroup() {
		if(group == null) {
			Log.debug(toString() + ".generateFieldGroup()..");
			setFieldGroup(generateFieldGroup());
		}
		return group;
	}

	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) {
			throw new IllegalArgumentException("A field group must be specified");
		}
		if(this.group != fields) {
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
		Log.debug("AbstractBindableWidget.setModel() - START");

		final IBindable old = this.model;

		if(old != null) {
			Log.debug("AbstractBindableWidget - unbinding existing action..");
			action.unset(this);
		}

		this.model = model;

		// apply property metadata
		getFieldGroup().applyPropertyMetadata(model);

		if(action != null) {
			action.set(this);
			if(isAttached() && (model != null)) {
				Log.debug("AbstractBindableWidget - re-binding existing action..");
				action.bind(this);
			}
		}
		//Log.debug("AbstractBindableWidget - firing 'model' prop change event..");
		//changeSupport.firePropertyChange(PropertyChangeType.MODEL.prop(), old, model);
	}

	@Override
	public final IBindingAction getAction() {
		return action;
	}

	@Override
	public final void setAction(IBindingAction action) {
		this.action = action;
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
			Log.debug(toString() + ": rendering fields..");
			renderer.render(getWidget(), getFieldGroup());
		}
	}

	@Override
	protected void onAttach() {
		Log.debug("Attaching " + this + "..");
		if(action != null) {
			Log.debug("Setting action [" + action + "] on [" + this + "]..");
			action.set(this);
		}
		super.onAttach();
		//Log.debug("Firing prop change 'attach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), false, true);
	}

	@Override
	protected void onLoad() {
		Log.debug("Loading " + toString() + "..");
		super.onLoad();
		if(action != null) {
			Log.debug("Binding action [" + action + "] on [" + this + "]..");
			action.bind(this);
		}
		if(!drawn) {
			draw();
			drawn = true;
		}
	}

	@Override
	protected void onDetach() {
		Log.debug("Detatching " + toString() + "..");
		super.onDetach();
		if(action != null) {
			Log.debug("Unbinding action [" + action + "] from [" + this + "]..");
			action.unbind(this);
		}
		//Log.debug("Firing prop change 'detach' event for " + toString() + "..");
		//changeSupport.firePropertyChange(PropertyChangeType.ATTACHED.prop(), true, false);
	}

	@Override
	public String toString() {
		return "FieldPanel[" + (group == null ? "-nofields-" : group.getName()) + "]";
	}
}
