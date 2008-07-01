/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.model.Model;
import com.tll.client.util.GlobalFormat;

/**
 * FieldGroupPanel - Common base class for panels that display form data.
 * Provision for read-only is supported.
 * @author jpk
 */
public abstract class FieldGroupPanel extends Composite {

	/**
	 * The Panel containing the drawn fields.
	 */
	private final FlowPanel panel = new FlowPanel();

	/**
	 * The collective group of all fields in this panel.
	 */
	private final FieldGroup fields;

	/**
	 * Internal flag for tracking whether the fields have been populated or not.
	 */
	private boolean populated;

	/**
	 * Constructor
	 * @param displayName The display name
	 */
	public FieldGroupPanel(String displayName) {
		super();
		this.fields = new FieldGroup(displayName, this);
		initWidget(panel);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if(panel.getWidgetCount() == 0) {
			init();
			draw(panel);
		}
	}

	/**
	 * Ensures the field group is populated.
	 */
	private void init() {
		if(!populated) {
			populateFieldGroup();
			populated = true;
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
		init();
		applyModel(model);
	}

	/**
	 * Event hook called by the member FieldGroup just after model binding.
	 */
	public void onAfterBind() {
		// base impl no-op
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
		init();
		return fields;
	}

	/**
	 * Creates a new {@link TextField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param visibleLength
	 */
	protected static final TextField ftext(String propName, String lblTxt, int visibleLength) {
		return new TextField(propName, lblTxt, visibleLength);
	}

	/**
	 * Creates a new {@link TextField} instance with currency formatting.
	 * @param propName
	 * @param lblTxt
	 */
	protected static final TextField fcurrency(String propName, String lblTxt) {
		TextField fld = ftext(propName, lblTxt, 15);
		fld.setFormat(GlobalFormat.CURRENCY);
		return fld;
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param displayFormat
	 */
	protected static final DateField fdate(String propName, String lblTxt, GlobalFormat format) {
		return new DateField(propName, lblTxt, format);
	}

	/**
	 * Creates a Check box field that is designed to be bound to a boolean type
	 * using String-wise constants "true" and "false" to indicate the boolean
	 * value respectively.
	 * @param propName
	 * @param lblTxt
	 * @param checkedValue
	 * @param uncheckedValue
	 */
	protected static final CheckboxField fbool(String propName, String lblTxt) {
		return new CheckboxField(propName, lblTxt, "true", "false");
	}

	/**
	 * Creates a new {@link CheckboxField} instance that is designed to be bound
	 * to a String type.
	 * @param propName
	 * @param lblTxt
	 * @param checkedValue
	 * @param uncheckedValue
	 */
	protected static final CheckboxField fcheckbox(String propName, String lblTxt, String checkedValue,
			String uncheckedValue) {
		return new CheckboxField(propName, lblTxt, checkedValue, uncheckedValue);
	}

	/**
	 * Creates a new {@link TextAreaField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param numRows
	 * @param numCols
	 */
	protected static final TextAreaField ftextarea(String propName, String lblTxt, int numRows, int numCols) {
		return new TextAreaField(propName, lblTxt, numRows, numCols);
	}

	/**
	 * Creates a new {@link PasswordField} instance.
	 * @param propName
	 * @param lblTxt
	 */
	protected static final PasswordField fpassword(String propName, String lblTxt) {
		return new PasswordField(propName, lblTxt);
	}

	/**
	 * Creates a new {@link SelectField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 */
	protected static final SelectField fselect(String propName, String lblTxt, Map<String, String> dataMap) {
		return new SelectField(propName, lblTxt, dataMap);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 */
	protected static final SuggestField fsuggest(String propName, String lblTxt, Map<String, String> dataMap) {
		return new SuggestField(propName, lblTxt, dataMap);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 * @param renderHorizontal
	 */
	protected static final RadioGroupField fradiogroup(String propName, String lblTxt, Map<String, String> dataMap,
			boolean renderHorizontal) {
		return new RadioGroupField(propName, lblTxt, dataMap, renderHorizontal);
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	protected static final DateField[] createTimestampEntityFields() {
		DateField dateCreated = fdate(Model.DATE_CREATED_PROPERTY, "Created", GlobalFormat.DATE);
		DateField dateModified = fdate(Model.DATE_MODIFIED_PROPERTY, "Modified", GlobalFormat.DATE);
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		return new DateField[] {
			dateCreated,
			dateModified };
	}

	/**
	 * Creates an entity name text field.
	 * @return The created entity name field
	 */
	protected static final TextField createNameEntityField() {
		return ftext(Model.NAME_PROPERTY, "Name", 30);
	}

	@Override
	public final String toString() {
		return fields.toString();
	}
}
