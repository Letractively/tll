/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldBinding;
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;

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
		ensureFieldGroupPopulated();
		return fields;
	}

	/**
	 * Ensures the field group is populated.
	 */
	private void ensureFieldGroupPopulated() {
		if(fields.size() < 1) {
			populateFieldGroup(fields);
		}
	}

	/**
	 * Adds {@link IField}s to the member {@link FieldGroup}.
	 */
	protected abstract void populateFieldGroup(FieldGroup fields);

	/**
	 * Hook to add fields to the field group that are dependent on the model.
	 * @param model The model that corres. directly to this field panel in terms
	 *        of the model type.
	 * @param fields The field group
	 */
	protected void applyModel(Model model, FieldGroup fields) {
		// base impl no-op
	}

	/**
	 * Creates {@link FieldBinding}s from the given {@link Model}. This panel's
	 * {@link FieldGroup} is populated if not already.
	 * @param bindings The FieldBindingGroup that recieves the new field bindings
	 * @param model The model subject to binding
	 */
	public final void createFieldBindings(FieldBindingGroup bindings, Model model) {
		FieldGroup fg = getFieldGroup();
		applyModel(model, fg);
		populateFieldBindingGroup(bindings, null, fg, model);
	}

	/**
	 * Populates the given {@link FieldBindingGroup} with {@link FieldBinding}s
	 * based on the existing fields and the given {@link Model}.
	 * @param bindings The {@link FieldBindingGroup} to which new
	 *        {@link FieldBinding}s are added
	 * @param parentPropertyPath The parent property path that is pre-pended to
	 *        each field's property name which serves to resolve fields from the
	 *        given {@link FieldGroup}
	 * @param fields The {@link FieldGroup} containing the fields to be bound
	 * @param model The resolved model whose child properties map to this
	 *        {@link FieldPanel}.
	 */
	protected abstract void populateFieldBindingGroup(FieldBindingGroup bindings, String parentPropertyPath,
			FieldGroup fields, Model model);

	/**
	 * Draws the fields onto the UI canvas only if there are no child Widgets
	 * contained in this Panel.
	 */
	public final void draw() {
		if(panel.getWidgetCount() == 0) {
			draw(panel, getFieldGroup());
		}
	}

	/**
	 * Draws the fields onto the UI "canvas".
	 * @param canvas The "canvas" on which the fields are drawn.
	 * @param fields The field group
	 */
	protected abstract void draw(Panel canvas, FieldGroup fields);

	/**
	 * Removes any previously drawn fields from this Panel forcing a subsequent
	 * re-draw.
	 */
	public final void clear() {
		panel.clear();
	}

	/**
	 * Factory method for creating {@link FieldBinding}s.
	 * @param propName The non-property path field property name
	 * @param model The model
	 * @param parentPropertyPath The parent property path that resolves the target
	 *        field in the field group. May be <code>null</code>.
	 * @return The created {@link FieldBinding} or <code>null</code> if either the
	 *         field or the model property is unresolvable.
	 */
	protected final FieldBinding createFieldBinding(String propName, Model model, String parentPropertyPath) {
		final PropertyPath pPropName = new PropertyPath(propName);

		final PropertyPath path = new PropertyPath(parentPropertyPath);
		path.append(propName);

		IField f = fields.getField(path.toString());
		if(f == null) return null;

		IPropertyValue pv = model.getPropertyValue(pPropName);
		if(pv == null) return null;

		return new FieldBinding(f, pv);
	}

	@Override
	public final String toString() {
		return fields.toString();
	}
}
