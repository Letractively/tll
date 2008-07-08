/**
 * The Logic Lab
 * @author jkirton
 * Jul 7, 2008
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IFieldProvider;

/**
 * DelegatingFieldGroupPanel
 * @author jpk
 */
public class DelegatingFieldGroupPanel extends FieldGroupPanel {

	/**
	 * Provides new instances of the fields used by this Panel.
	 */
	private final IFieldProvider provider;

	/**
	 * The delegate for drawing the fields in the UI.
	 */
	private final IFieldRenderer renderer;

	/**
	 * Constructor
	 * @param displayName
	 * @param fields
	 * @param provider
	 * @param renderer
	 */
	public DelegatingFieldGroupPanel(String displayName, FieldGroup fields, IFieldProvider provider,
			IFieldRenderer renderer) {
		super(displayName, fields);
		this.provider = provider;
		this.renderer = renderer;
	}

	@Override
	protected void populateFieldGroup() {
		addFields(provider.getFields());
	}

	@Override
	protected final void draw(Panel canvas) {
		renderer.draw(canvas);
	}
}
