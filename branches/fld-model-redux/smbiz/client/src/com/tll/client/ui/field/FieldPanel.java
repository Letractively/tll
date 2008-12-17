/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.FieldModelBinding;
import com.tll.client.field.IField;
import com.tll.client.model.Model;

/**
 * FieldPanel - Common base class for panels that display form data. Provision
 * for read-only is supported.
 * @author jpk
 */
public abstract class FieldPanel extends Composite {

	/**
	 * The Panel containing the drawn fields.
	 */
	private final FlowPanel panel = new FlowPanel();

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
		initWidget(panel);
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
	 * Applies the model to the fields of this panel providing an opportunity to
	 * create fields whose existence are dependent on interrogating the model.
	 * @param model The model to apply
	 */
	public void applyModel(Model model) {
		// base impl no-op
	}

	/**
	 * Creates the necessary field bindings for fields in this panel to fully
	 * support bi-diretional field/model data transfer.
	 * <p>
	 * This method is responsible for:
	 * <ol>
	 * <li>Adding
	 * <li>Adding field bindings to the given {@link FieldModelBinding} to fully
	 * support bi-directional field/model data transfer.
	 * </ol>
	 * @param model The model subject to binding for this field panel
	 * @param bindings The field bindings that receive newly created field
	 *        bindings
	 */
	public abstract void setFieldBindings(Model model, FieldModelBinding bindings);

	/**
	 * Draws or re-draws this field panel.
	 */
	public final void draw() {
		clear();
		drawInternal(panel);
	}

	/**
	 * Draws the fields onto the given {@link Panel} and supporting {@link Widget}
	 * s.
	 * @param canvas The "canvas" on which the fields are drawn.
	 */
	protected abstract void drawInternal(Panel canvas);

	/**
	 * Removes all child {@link Widget}s from this {@link FieldPanel}.
	 */
	public final void clear() {
		panel.clear();
	}

	@Override
	public final String toString() {
		return fields.toString();
	}
}
