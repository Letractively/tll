/**
 * The Logic Lab
 * @author jpk Nov 5, 2007
 */
package com.tll.client.ui.field;

import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
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
public abstract class FieldGroupPanel extends Composite {

	/**
	 * The wrapped canvas panel Widget. An {@link IFieldPanelComposer} wil
	 * generate and provide this Widget.
	 */
	protected Widget canvas;

	/**
	 * The collective group of all fields in this panel.
	 */
	protected final FieldGroup fields;

	/**
	 * Constructor
	 * @param displayName The display name
	 */
	public FieldGroupPanel(String displayName) {
		super();
		this.fields = new FieldGroup(displayName, this);
		// initWidget(panel);
	}

	@Override
	protected void onAttach() {
		init();
		assert canvas != null;
		initWidget(canvas);
		super.onAttach();
	}

	/**
	 * Add needed aux data to the given aux data request for showing this field
	 * group panel.
	 * @param auxDataRequest
	 */
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		// default is nothing
	}

	/**
	 * Overrides the default behavior of this panel being the ref Widget. This is
	 * useful when we have Widgets that cloak (hide) fields since the ref Widget
	 * is employed for posting validation messages in the ui.
	 * @param refWidget The Widget to set as the ref Widget.
	 */
	public final void setRefWidget(Widget refWidget) {
		fields.setRefWidget(refWidget);
	}

	/**
	 * @return The owned FieldGroup for this panel.
	 */
	public final FieldGroup getFields() {
		return fields;
	}

	/**
	 * Responsible for:
	 * <ul>
	 * <li>Populating {@link #fields}
	 * <li>Creating and populating a {@link Panel} containing the owned
	 * {@link IField}s
	 * </ul>
	 * @return The canvas Widget that serves as the wrapped Widget for this
	 *         Composite Widget.
	 */
	protected abstract Widget doInit();

	public final void init() {
		if(canvas == null) {
			canvas = doInit();
		}
	}

	/**
	 * Binds the model to the field group referenced by the field group panel.
	 * @param model The model to bind
	 */
	public final void bind(Model model) {
		// TODO determine if we need to call init here as we are now calling it in
		// onAttach().
		init();
		onBeforeBind(model);
		fields.bindModel(model.getBinding(null));
		onAfterBind(model);
	}

	/**
	 * The validation hook. Sub-classes may override but should most always call
	 * the super to ensure the fields are validated!
	 * @throws ValidationException
	 */
	public void validate() throws ValidationException {
		fields.validate();
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
		validate();
		onBeforeUpdateModel(model);
		if(fields.updateModel(model.getBinding(null)) /*|| fields.isPending()*/) {
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
	 * The method is called just before the model is updated *after* validation.
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
	 * @param visibleLength
	 */
	protected static final TextField ftext(String propName, String lblTxt, int visibleLength) {
		return new TextField(propName, lblTxt, visibleLength);
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
		return fields.getPropertyName();
	}
}
