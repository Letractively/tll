/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;
import com.tll.client.model.PropertyPathException;
import com.tll.client.model.UnsetPropertyException;
import com.tll.client.ui.AbstractBoundWidget;
import com.tll.client.ui.IBoundWidget;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
 * <p>
 * <em><b>IMPT: </b>The composite wrapped widget is used for field rendering.  Consequently, it must be ensured that types of the two match.</em>
 * @param <W> The widget type employed for field rendering
 * @param <M> The model type
 * @author jpk
 */
public abstract class FieldPanel<W extends Widget, M extends IBindable> extends AbstractBoundWidget<M, FieldGroup, M>
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

	/**
	 * @return The field group for this field panel.
	 */
	public final FieldGroup getFieldGroup() {
		if(fields == null) {
			setFieldGroup(generateFieldGroup());
		}
		return fields;
	}

	/**
	 * Sets the field group.
	 * @param fields The required field group
	 */
	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) {
			throw new IllegalArgumentException("A field group must be specified");
		}
		if(this.fields != fields) {
			this.fields = fields;
			this.fields.setFeedbackWidget(this);
		}
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
		renderer.render(getWidget(), getFieldGroup());
	}

	@Override
	public void setModel(M model) {
		if(getModel() == null || model != getModel()) {
			super.setModel(model);

			// apply property metadata
			if(model instanceof IPropertyMetadataProvider) {
				getFieldGroup().applyPropertyMetadata((IPropertyMetadataProvider) model);
			}
		}
	}

	public FieldGroup getValue() {
		return getFieldGroup();
	}

	public void setValue(M value) {
		setModel(value);
	}

	/**
	 * Searches the member field group for the field whose property name matches
	 * that given. <br>
	 * NOTE: The field, if found, is returned in the form of an
	 * {@link AbstractField} so it may serve as an {@link IBoundWidget} when
	 * necessary.
	 * @param propPath The property path of the sought field.
	 * @return The non-<code>null</code> field
	 * @throws UnsetPropertyException When the field does not exist in the member
	 *         field group
	 */
	// TODO see if we can make this private
	public final AbstractField<?, ?> getField(String propPath) throws UnsetPropertyException {
		IField<?, ?> f = getFieldGroup().getField(propPath);
		if(f == null) {
			throw new UnsetPropertyException(propPath);
		}
		return (AbstractField<?, ?>) f;
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return getField(propPath).getProperty(propPath);
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		getField(propPath).setProperty(propPath, value);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(!drawn) {
			draw();
			drawn = true;
		}
	}
}
