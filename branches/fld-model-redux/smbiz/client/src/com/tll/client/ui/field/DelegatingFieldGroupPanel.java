/**
 * The Logic Lab
 * @author jkirton
 * Jul 7, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldGroup;

/**
 * DelegatingFieldGroupPanel - Delegates drawing of the field Panel to an
 * {@link IFieldRenderer}.
 * @author jpk
 */
public class DelegatingFieldGroupPanel extends FieldPanel {

	/**
	 * The delegate for drawing the fields in the UI.
	 */
	private final IFieldRenderer renderer;

	/**
	 * The parent property path of the target fields to be drawn. May be
	 * <code>null</code>.
	 */
	private String parentPropertyPath;

	/**
	 * Constructor
	 * @param displayName The display name
	 * @param fieldGroup
	 * @param renderer
	 */
	public DelegatingFieldGroupPanel(String displayName, FieldGroup fieldGroup, IFieldRenderer renderer) {
		super(displayName, fieldGroup);
		this.renderer = renderer;
	}

	public void setParentPropertyPath(String parentPropertyPath) {
		this.parentPropertyPath = parentPropertyPath;
	}

	@Override
	protected void populateFieldGroup() {
		// we assume the group has already been populated!
		throw new UnsupportedOperationException();
	}

	@Override
	protected final void draw(Panel canvas) {
		renderer.draw(canvas, getFieldGroup(), parentPropertyPath);
	}
}
