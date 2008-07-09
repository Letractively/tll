/**
 * The Logic Lab
 * @author jkirton
 * Jul 7, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldGroup;

/**
 * DelegatingFieldGroupPanel
 * @author jpk
 */
public class DelegatingFieldGroupPanel extends FieldGroupPanel {

	/**
	 * The delegate for drawing the fields in the UI.
	 */
	private final IFieldRenderer renderer;

	/**
	 * The parent property path used to filter fields from the referenced
	 * FieldGroup.
	 */
	private String parentPropertyPath;

	/**
	 * Constructor
	 * @param displayName The display name
	 * @param parentFieldGroup
	 * @param renderer
	 */
	public DelegatingFieldGroupPanel(String displayName, FieldGroup parentFieldGroup, IFieldRenderer renderer) {
		super(displayName, parentFieldGroup);
		this.renderer = renderer;
	}

	/**
	 * Sets the parent property path which dictates (or filters) the queried
	 * fields from the referenced FieldGroup.
	 * @param parentPropertyPath The parent property path to set
	 */
	public void setParentPropertyPath(String parentPropertyPath) {
		if((parentPropertyPath == null && this.parentPropertyPath == null)
				|| (parentPropertyPath != null && parentPropertyPath.equals(this.parentPropertyPath))) return;
		this.parentPropertyPath = parentPropertyPath;
		clear(); // force a re-draw
	}

	@Override
	protected void populateFieldGroup() {
		// we assume the the group has already been populated!
		throw new UnsupportedOperationException();
	}

	@Override
	protected final void draw(Panel canvas) {
		renderer.draw(canvas, getFields(), parentPropertyPath);
	}
}
