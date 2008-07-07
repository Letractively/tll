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
	 * Constructor
	 * @param displayName
	 * @param fields
	 * @param renderer
	 */
	public DelegatingFieldGroupPanel(String displayName, FieldGroup fields, IFieldRenderer renderer) {
		super(displayName, fields);
		this.renderer = renderer;
	}

	@Override
	protected final void draw(Panel canvas) {
		renderer.draw(canvas);
	}
}
