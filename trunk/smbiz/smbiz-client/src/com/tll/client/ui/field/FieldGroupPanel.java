/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.model.IPropertyValue;
import com.tll.client.model.Model;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.ValidationException;

/**
 * FieldGroupPanel - Common base class for panels that display form data.
 * Provision for read-only is supported.
 * @author jpk
 */
public abstract class FieldGroupPanel extends FlowPanel /*implements IPropertyNameProvider*/{

	/**
	 * Needed to ensure unique pending property names and to hide this aspect from
	 * clients.
	 */
	private static int pendingPropertyCounter = 0;

	/**
	 * Provides a "pending" indexed property name. Pending implies that the
	 * indexed property does <em>NOT</em> yet exist and the semantics of a
	 * pending property name serves to indicate this fact.
	 * @return A unique property name indicating "pending".
	 */
	public static String getPendingPropertyName() {
		return "{pending" + pendingPropertyCounter++ + '}';
	}

	/**
	 * Is the given property name a pending property? Used in tandem with
	 * {@link #getPendingPropertyName()}.
	 * @param propName The property name to check
	 * @return true/false
	 * @see #getPendingPropertyName()
	 */
	public static boolean isPendingProperty(String propName) {
		return propName != null && propName.startsWith("{pending");
	}

	/**
	 * The collective group of all fields in this panel.
	 */
	protected final FieldGroup fields;

	/**
	 * Flag indicating whether the fields have been instantiated and the UI
	 * widgets have been set.
	 */
	private boolean configured = false;

	/**
	 * Constructor
	 * @param propName The property name
	 * @param displayName The display name
	 */
	public FieldGroupPanel(String propName, String displayName) {
		super();
		this.fields = new FieldGroup(propName, displayName, this);
	}

	/**
	 * Add needed aux data to the given aux data request for showing this field
	 * group panel.
	 * @param auxDataRequest
	 */
	protected abstract void neededAuxData(AuxDataRequest auxDataRequest);

	/**
	 * The <em>local</em> (or relative) property name bound the FieldGroup
	 * referenced by this FieldGroupPanel.
	 * <p>
	 * NOTE: This does <em>not</em> include the parent property path.
	 */
	/*
	public final String getPropertyName() {
		return fields.getPropertyName();
	}

	public final void setPropertyName(String propName) {
		fields.setPropertyName(propName);
	}
	*/

	/**
	 * Overrides the default behavior of this panel being the ref Widget. This is
	 * useful when we have Widgets that cloak (hide) fields since the ref Widget
	 * is employed for posting validation messages in the ui.
	 * @param refWidget The Widget to set as the ref Widget.
	 */
	public final void setRefWidget(Widget refWidget) {
		fields.setRefWidget(refWidget);
	}

	public final FieldGroup getFields() {
		return fields;
	}

	protected final boolean isConfigured() {
		return configured;
	}

	/**
	 * Populates the field group and sets the UI widgets. Performed once.
	 * <p>
	 * <em>IMPT:</em> This method should be called resursively for child field
	 * group panels.
	 * <p>
	 * NOTE: panels needing aux data should access the {@link AuxDataCache}
	 * singleton.
	 */
	protected abstract void configure();

	public final boolean isReadOnly() {
		return fields.isReadOnly();
	}

	public final void setReadOnly(boolean readOnly) {
		fields.setReadOnly(readOnly);
	}

	public final boolean isEnabled() {
		return fields.isEnabled();
	}

	public final void setEnabled(boolean enabled) {
		fields.setEnabled(enabled);
	}

	/**
	 * Renders the fields.
	 */
	public final void render() {
		assert configured : "The field group panel must be configured before rendering is allowed";
		fields.render();
	}

	/**
	 * Resets the field values to their original values.
	 */
	public final void reset() {
		assert configured : "The field group panel must be configured before resetting is allowed";
		fields.reset();
	}

	/**
	 * Binds the model to the field group referenced by the field group panel.
	 * @param model The model to bind
	 */
	public final void bind(Model model) {
		if(!configured) {
			configure();
			configured = true;
		}
		onBeforeBind(model);
		fields.bindModel(model);
		onAfterBind(model);
	}

	/**
	 * Validates the field group and if no validation errors sets the
	 * {@link IPropertyValue}s in the given {@link Model} with those held in the
	 * child {@link IField}s of this panel's {@link FieldGroup}.
	 * <p>
	 * onBeforeUpdateModel is called immediately after successful validation.
	 * <p>
	 * onAfterUpdateModel is called immediately after the model is successfully
	 * updated.
	 * @param model The {@link Model} to be populated.
	 * @return <code>true</code> when the model update was successful with at
	 *         least one change made to the model or <code>false</code> when no
	 *         changes were made.
	 * @throws ValidationException When there are validation errors.
	 */
	public final boolean updateModel(Model model) throws ValidationException {
		fields.validate();
		onBeforeUpdateModel(model);
		if(fields.updateModel(model)) {
			onAfterUpdateModel(model);
			return true;
		}
		return false;
	}

	/**
	 * Provision for field panels to "prep" themselves just before the entity is
	 * applied.
	 * @param model
	 */
	protected void onBeforeBind(Model model) {
		// base impl - no-op
	}

	/**
	 * Provision for field panels to "prep" themselves just AFTER the entity is
	 * applied.
	 * @param model
	 */
	protected void onAfterBind(Model model) {
		// base impl - no-op
	}

	/**
	 * The method is called just before the model is updated.
	 * @param model The model about to be updated.
	 */
	protected void onBeforeUpdateModel(Model model) {
		// base impl - no-op
	}

	/**
	 * The method is called immediately after the model is updated.
	 * @param model The updated model.
	 */
	protected void onAfterUpdateModel(Model model) {
		// base impl - no-op
	}

	/**
	 * Creates a new {@link TextField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param visibleLength
	 */
	protected final TextField ftext(String propName, String lblTxt, int lblMode, int visibleLength) {
		return new TextField(propName, lblTxt, lblMode, visibleLength);
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param displayFormat
	 */
	protected final DateField fdate(String propName, String lblTxt, int lblMode, GlobalFormat format) {
		return new DateField(propName, lblTxt, lblMode, format);
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
	protected final CheckboxField fbool(String propName, String lblTxt) {
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
	protected final CheckboxField fcheckbox(String propName, String lblTxt, String checkedValue, String uncheckedValue) {
		return new CheckboxField(propName, lblTxt, checkedValue, uncheckedValue);
	}

	/**
	 * Creates a new {@link TextAreaField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param numRows
	 * @param numCols
	 */
	protected final TextAreaField ftextarea(String propName, String lblTxt, int lblMode, int numRows, int numCols) {
		return new TextAreaField(propName, lblTxt, lblMode, numRows, numCols);
	}

	/**
	 * Creates a new {@link PasswordField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 */
	protected final PasswordField fpassword(String propName, String lblTxt, int lblMode) {
		return new PasswordField(propName, lblTxt, lblMode);
	}

	/**
	 * Creates a new {@link SelectField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param dataMap
	 */
	protected final SelectField fselect(String propName, String lblTxt, int lblMode, Map<String, String> dataMap) {
		return new SelectField(propName, lblTxt, lblMode, dataMap);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param dataMap
	 */
	protected final SuggestField fsuggest(String propName, String lblTxt, int lblMode, Map<String, String> dataMap) {
		return new SuggestField(propName, lblTxt, lblMode, dataMap);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param lblMode
	 * @param dataMap
	 * @param renderHorizontal
	 */
	protected final RadioGroupField fradiogroup(String propName, String lblTxt, int lblMode, Map<String, String> dataMap,
			boolean renderHorizontal) {
		return new RadioGroupField(propName, lblTxt, lblMode, dataMap, renderHorizontal);
	}

	@Override
	public final String toString() {
		return fields.getPropertyName();
	}
}
