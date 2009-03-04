/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.AbstractBindableWidget;
import com.tll.client.ui.msg.MsgPopupRegistry;

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
		if(fields != null) fields.setMsgPopupRegistry(mregistry);
	}

	/**
	 * Provides the field panel renderer (drawer).
	 * @return the renderer
	 */
	protected abstract IFieldRenderer<W> getRenderer();

	/**
	 * Generates a fresh field group with fields this panel will maintain. This
	 * method is only called when this panel's field group reference is null.
	 * Therefore, this method may be circumvented by manually calling
	 * {@link #setFieldGroup(FieldGroup)} prior to DOM attachment.
	 * @return The FieldGroup for this panel ensuring it is first populated.
	 */
	protected abstract FieldGroup generateFieldGroup();

	/**
	 * Responsible for rendering the fields in the ui. The default is to employ
	 * the provided renderer via {@link #getRenderer()}. Sub-classes may extend
	 * this method to circumvent this strategy thus avoiding the call to
	 * {@link #getRenderer()}.
	 */
	protected void draw() {
		Log.debug(toString() + ".draw()..");
		getRenderer().render(getWidget(), getFieldGroup());
	}

	public final FieldGroup getFieldGroup() {
		if(fields == null) {
			Log.debug(toString() + ".generateFieldGroup()..");
			setFieldGroup(generateFieldGroup());
		}
		return fields;
	}
	
	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) {
			throw new IllegalArgumentException("A field group must be specified");
		}
		if(this.fields != fields) {
			this.fields = fields;
			this.fields.setWidget(this);
		}
	}

	@Override
	public final FieldGroup getValue() {
		return getFieldGroup();
	}

	@Override
	public final void setValue(FieldGroup fields) {
		setFieldGroup(fields);
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
