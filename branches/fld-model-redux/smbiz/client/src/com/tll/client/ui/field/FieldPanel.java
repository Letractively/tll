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

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
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
	 * @param modelPropertyPath The property path that resolves the target model
	 *        under the binding def's root model.
	 */
	public void applyModel(FieldModelBinding bindingDef, String modelPropertyPath) {
		// base impl no-op
	}

	/**
	 * Sets the working field/model binding "definition".
	 * @param bindingDef The binding definition
	 * @param modelPropertyPath The property path that resolves the target model
	 *        under the binding def's root model.
	 */
	public final void setBindingDefinition(FieldModelBinding bindingDef, String modelPropertyPath) {
		addFieldBindings(bindingDef, modelPropertyPath);
	}

	/**
	 * Adds the non-relational field bindings for the fields managed by this
	 * {@link FieldPanel}.
	 * <p>
	 * This method is reponsible for calling this method for all its child
	 * {@link FieldPanel}s and thus must resolve the appropriate nested model from
	 * the root model.
	 * @param modelPropertyPath The property path that resolves the [nested] model
	 *        contained under the binding def's root model. model or a nested
	 *        model under the root model.
	 */
	public abstract void addFieldBindings(FieldModelBinding bindingDef, String modelPropertyPath);

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
