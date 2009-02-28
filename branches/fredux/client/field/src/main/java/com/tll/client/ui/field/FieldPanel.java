/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.AbstractBindableWidget;
import com.tll.client.ui.IBindableWidget;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.model.PropertyPathException;
import com.tll.common.model.UnsetPropertyException;

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
 * <p>
 * <em><b>IMPT: </b>The composite wrapped widget is used for field rendering.  Consequently, it must be ensured that types of the two match.</em>
 * @param <W> The widget type employed for field rendering
 * @author jpk
 */
public abstract class FieldPanel<W extends Widget> extends AbstractBindableWidget<FieldGroup>
		implements IFieldGroupProvider {

	/**
	 * The field group.
	 */
	private FieldGroup fields;

	/**
	 * The field renderer.
	 */
	private IFieldRenderer<W> renderer;

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
	public void setMsgPopupRegistry(MsgPopupRegistry mregistry) {
		this.mregistry = mregistry;
		// propagate to the fields
		fields.setMsgPopupRegistry(mregistry);
	}

	/**
	 * Sets the field renderer.
	 * @param renderer
	 */
	public final void setRenderer(IFieldRenderer<W> renderer) {
		this.renderer = renderer;
	}

	/**
	 * Generates a fresh field group with fields this panel will maintain. This
	 * method is only called when this panel's field group reference is null.
	 * Therefore, this method may be circumvented by manually calling
	 * {@link #setFieldGroup(FieldGroup)} prior to DOM attachment.
	 * @return The FieldGroup for this panel ensuring it is first populated.
	 */
	protected abstract FieldGroup generateFieldGroup();

	/**
	 * Draws the fields.
	 */
	private void draw() {
		if(renderer == null) {
			throw new IllegalStateException("No field renderer set");
		}
		Log.debug(toString() + ".draw()..");
		renderer.render(getWidget(), getFieldGroup());
	}

	/**
	 * Searches the member field group for the field whose property name matches
	 * that given. <br>
	 * NOTE: The field, if found, is returned in the form of an
	 * {@link IFieldWidget} so it may serve as an {@link IBindableWidget} when
	 * necessary.
	 * @param propPath The property path of the sought field.
	 * @return The non-<code>null</code> field
	 * @throws UnsetPropertyException When the field does not exist in the member
	 *         field group
	 */
	private IFieldWidget<?> getField(String propPath) throws UnsetPropertyException {
		final IField<?> f = getFieldGroup().getField(propPath);
		if(f == null) {
			throw new UnsetPropertyException(propPath);
		}
		return (IFieldWidget<?>) f;
	}

	public final Object getProperty(String propPath) throws PropertyPathException {
		return getField(propPath).getProperty(propPath);
	}

	public final void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		getField(propPath).setProperty(propPath, value);
	}
	
	public final FieldGroup getFieldGroup() {
		if(fields == null) {
			Log.debug(toString() + ".generateFieldGroup()..");
			setValue(generateFieldGroup());
		}
		return fields;
	}
	
	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) {
			throw new IllegalArgumentException("A field group must be specified");
		}
		if(this.fields != fields) {
			this.fields = fields;
			this.fields.setFeedbackWidget(this);
		}
	}

	@Override
	public final FieldGroup getValue() {
		return getFieldGroup();
	}

	@Override
	public final void setValue(FieldGroup value, boolean fireEvents) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void setValue(FieldGroup fields) {
		setFieldGroup(fields);
	}

	@Override
	public final HandlerRegistration addValueChangeHandler(ValueChangeHandler<FieldGroup> handler) {
		//return addHandler(handler, ValueChangeEvent.getType());
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(!drawn) {
			draw();
			drawn = true;
		}
	}

	@Override
	public String toString() {
		return "FieldPanel [ " + (fields == null ? "-nofields-" : fields.getName()) + " ]";
	}
}
