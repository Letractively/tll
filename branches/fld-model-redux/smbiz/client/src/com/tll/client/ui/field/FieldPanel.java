/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.field.FieldBindingGroup;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.model.Model;

/**
 * FieldPanel - Common base class for panels that display form data.
 * Provision for read-only is supported.
 * @author jpk
 */
public abstract class FieldPanel extends Composite /*implements IFieldBindingListener*/{

	/**
	 * The Panel containing the drawn fields.
	 */
	private final FlowPanel panel = new FlowPanel();

	/**
	 * The collective group of all fields in this panel.
	 */
	private FieldGroup fields;

	/**
	 * The field bindings.
	 */
	private FieldBindingGroup bindings;

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
	 * Ensures the field group is populated.
	 */
	private void ensureFieldGroupPopulated() {
		if(fields.size() < 1) {
			populateFieldGroup();
		}
	}

	/**
	 * Draws the fields onto the UI canvas only if there are no child Widgets
	 * contained in this Panel.
	 */
	public final void draw() {
		if(panel.getWidgetCount() == 0) {
			ensureFieldGroupPopulated();
			draw(panel);
		}
	}

	/**
	 * Draws the fields onto the UI "canvas".
	 * @param canvas The "canvas" on which the fields are drawn.
	 */
	protected abstract void draw(Panel canvas);

	/**
	 * Adds {@link IField}s to the member {@link FieldGroup}.
	 */
	protected abstract void populateFieldGroup();

	/**
	 * Opportunity to embellish the UI and/or the member FieldGroup based on the
	 * underlying data providing Model. This is necessary, for example, when this
	 * Panel handles related many sub-Models.
	 * @param model The underlying Model that will be bound and updated.
	 */
	protected void applyModel(Model model) {
		// base impl no-op
	}

	/**
	 * Creates the data transfer points between {@link IField}s and the underlying
	 * {@link Model}.
	 * @param bindings The group to which the created bindings are added
	 * @param model The model from which properties are extracted and bound to the
	 *        desired fields
	 */
	protected void createFieldBindings(FieldBindingGroup bindings, Model model) {
		// base impl no-op
	}

	public final void bindModel(Model model) {

		// ensure fields are created
		ensureFieldGroupPopulated();

		// ensure field bindings created
		if(bindings == null) {
			bindings = new FieldBindingGroup();
			createFieldBindings(bindings, model);
		}

	}

	public void unbindModel() {
		if(bindings != null) {
			bindings.unbind();
		}
	}

	/**
	 * Event hook called by the member FieldGroup just before model binding.
	 * @param model The model about to be bound
	 */
	/*
	public final void onBeforeBind(Model model) {
		ensurePopulated();
		applyModel(model);
	}
	*/

	/**
	 * Event hook called by the member FieldGroup just after model binding. May be
	 * overridden.
	 */
	/*
	public void onAfterBind() {
		draw();
	}
	*/

	protected final void removeField(IField field) {
		fields.removeField(field);
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
	 * Removes any previously drawn fields from this Panel forcing a subsequent
	 * re-draw.
	 */
	public final void clear() {
		panel.clear();
	}

	protected final void addField(IField field) {
		addField(null, field);
	}

	protected final void addFields(Iterable<IField> fields) {
		addFields(null, fields);
	}

	protected final void addFields(IField[] fields) {
		addFields(null, fields);
	}

	protected final void addField(String parentPropPath, IField field) {
		fields.addField(parentPropPath, field);
	}

	protected final void addFields(String parentPropPath, Iterable<IField> fields) {
		this.fields.addFields(parentPropPath, fields);
	}

	protected final void addFields(String parentPropPath, IField[] fields) {
		this.fields.addFields(parentPropPath, fields);
	}

	@Override
	public final String toString() {
		return fields.toString();
	}
}
