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

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
 * @author jpk
 * @param <M> The model type
 */
public abstract class FieldPanel<M extends IBindable> extends AbstractBoundWidget<M, M, M> {

	/**
	 * The collective group of all fields in this panel.
	 */
	private final FieldGroup fields;

	/**
	 * Constructor
	 * @param displayName The display name
	 */
	public FieldPanel(String displayName) {
		fields = new FieldGroup(displayName, this);
	}

	/**
	 * @return The FieldGroup for this panel ensuring it is first populated.
	 */
	public final FieldGroup getFieldGroup() {
		if(fields.size() < 1) {
			populateFieldGroup(fields);
		}
		return fields;
	}

	/**
	 * Adds {@link IField}s to the member {@link FieldGroup}.
	 */
	protected abstract void populateFieldGroup(FieldGroup fields);

	/**
	 * Draws the fields onto the given {@link Panel} and supporting {@link Widget}
	 * s.
	 */
	protected abstract void draw();

	public final M getValue() {
		return getModel();
	}

	public final void setValue(M value) {
		setModel(value);
	}

	private IBindable getBindableField(String propPath) throws PropertyPathException {
		IField f = getFieldGroup().getField(propPath);
		if(f == null) {
			throw new UnsetPropertyException(propPath);
		}
		return f;
	}

	public Object getProperty(String propPath) throws PropertyPathException {
		return getBindableField(propPath).getProperty(propPath);
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException {
		getBindableField(propPath).setProperty(propPath, value);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(fields.size() < 1) {
			populateFieldGroup(fields);
			draw();
		}
	}

	@Override
	public final String toString() {
		return fields.toString();
	}
}
