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
	private FieldGroup fields;

	/**
	 * Constructor
	 * @param displayName The display name
	 */
	public FieldPanel(String displayName) {
		this(displayName, null);
	}

	/**
	 * Constructor - Use when an already populated FieldGroup is to be applied to
	 * this Panel.
	 * @param displayName The display name
	 * @param fields The FieldGroup
	 */
	public FieldPanel(String displayName, FieldGroup fields) {
		setFieldGroup(fields == null ? new FieldGroup(displayName, this) : fields);
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
	 * Replaces the underlying {@link FieldGroup}.
	 * @param fields The field group which can't be <code>null</code>
	 */
	public final void setFieldGroup(FieldGroup fields) {
		if(fields == null) throw new IllegalArgumentException();
		this.fields = fields;
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
	 * Creates a populated FieldBindingGroup based on this panel's
	 * {@link FieldGroup} and the given {@link Model}.
	 * @param model The model subject to binding
	 */
	public final FieldBindingGroup createFieldBindings(Model model) {
		FieldGroup fg = getFieldGroup();
		applyModel(model, fg);
		FieldBindingGroup bindings = new FieldBindingGroup();
		populateFieldBindingGroup(bindings, null, fg, model);
		return bindings;
	}

	/**
	 * Populates the given {@link FieldBindingGroup} with field bindings based on
	 * the fields and the model.
	 * @param bindings The field binding group
	 * @param parentPropertyPath The parent property path that is pre-pended to
	 *        each field's property name which resolves the field from the field
	 *        group
	 * @param fields The field group
	 * @param model The resolved model whose child properties map directly to this
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
