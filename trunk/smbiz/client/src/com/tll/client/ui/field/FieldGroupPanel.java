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
import com.tll.client.field.IField;
import com.tll.client.field.IFieldBindingListener;
import com.tll.client.model.Model;

/**
 * FieldGroupPanel - Common base class for panels that display form data.
 * Provision for read-only is supported.
 * @author jpk
 */
public abstract class FieldGroupPanel extends Composite implements IFieldBindingListener {

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
	public FieldGroupPanel(String displayName) {
		this(displayName, null);
	}

	/**
	 * Constructor - Use when an already populated FieldGroup is to be applied to
	 * this Panel.
	 * @param displayName The display name
	 * @param fields The FieldGroup
	 */
	public FieldGroupPanel(String displayName, FieldGroup fields) {
		this.fields = fields == null ? new FieldGroup(displayName, this, this) : fields;
		initWidget(panel);
	}

	/**
	 * Ensures the field group is populated.
	 */
	private void ensurePopulated() {
		if(fields.size() < 1) {
			populateFieldGroup();
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
	 * Event hook called by the member FieldGroup just before model binding.
	 * @param model The model about to be bound
	 */
	public final void onBeforeBind(Model model) {
		ensurePopulated();
		applyModel(model);
	}

	/**
	 * Event hook called by the member FieldGroup just after model binding. May be
	 * overridden.
	 */
	public void onAfterBind() {
		if(panel.getWidgetCount() == 0) {
			ensurePopulated();
			draw(panel);
		}
	}

	protected final void addField(IField field) {
		addField(null, field);
	}

	protected final void addFields(IField[] fields) {
		addFields(null, fields);
	}

	protected final void addField(String parentPropPath, IField field) {
		fields.addField(parentPropPath, field);
	}

	protected final void addFields(String parentPropPath, IField[] fields) {
		this.fields.addFields(parentPropPath, fields);
	}

	protected final void removeField(IField field) {
		fields.removeField(field);
	}

	/**
	 * Overrides the default behavior of this panel being the feedback Widget.
	 * This is useful when we have Widgets that cloak (hide) fields since the
	 * feedback Widget is employed for posting validation messages in the ui.
	 * @param feedbackWidget The Widget to set as the feedback Widget.
	 */
	public final void setFeedbackWidget(Widget feedbackWidget) {
		fields.setFeedbackWidget(feedbackWidget);
	}

	/**
	 * @return The FieldGroup for this panel ensuring it is first populated.
	 */
	public final FieldGroup getFields() {
		ensurePopulated();
		return fields;
	}

	@Override
	public final String toString() {
		return fields.toString();
	}
}
