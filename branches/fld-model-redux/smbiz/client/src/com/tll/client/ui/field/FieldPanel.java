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
import com.tll.client.field.FieldModelBinding;
import com.tll.client.field.IField;
import com.tll.client.model.Model;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.EmailAddressValidator;

/**
 * FieldPanel - Common base class for {@link Panel}s that display {@link IField}
 * s.
 * @author jpk
 */
public abstract class FieldPanel extends Composite {

	/**
	 * Creates a new {@link TextField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param visibleLength
	 */
	public static final TextField ftext(String propName, String lblTxt, int visibleLength) {
		return new TextField(propName, lblTxt, visibleLength);
	}

	/**
	 * Creates a new {@link TextField} instance with email address validation.
	 * @param propName
	 * @param lblTxt
	 * @param visibleLength
	 */
	public static final TextField femail(String propName, String lblTxt, int visibleLength) {
		TextField f = ftext(propName, lblTxt, visibleLength);
		f.addValidator(EmailAddressValidator.INSTANCE);
		return f;
	}

	/**
	 * Creates a new {@link TextField} instance with currency formatting.
	 * @param propName
	 * @param lblTxt
	 */
	public static final TextField fcurrency(String propName, String lblTxt) {
		TextField fld = ftext(propName, lblTxt, 15);
		fld.setFormat(GlobalFormat.CURRENCY);
		return fld;
	}

	/**
	 * Creates new {@link DateField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param format
	 */
	public static final DateField fdate(String propName, String lblTxt, GlobalFormat format) {
		return new DateField(propName, lblTxt, format);
	}

	/**
	 * Creates a Check box field that is designed to be bound to a boolean type
	 * using String-wise constants "true" and "false" to indicate the boolean
	 * value respectively.
	 * @param propName
	 * @param lblTxt
	 */
	public static final CheckboxField fbool(String propName, String lblTxt) {
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
	public static final CheckboxField fcheckbox(String propName, String lblTxt, String checkedValue, String uncheckedValue) {
		return new CheckboxField(propName, lblTxt, checkedValue, uncheckedValue);
	}

	/**
	 * Creates a new {@link TextAreaField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param numRows
	 * @param numCols
	 */
	public static final TextAreaField ftextarea(String propName, String lblTxt, int numRows, int numCols) {
		return new TextAreaField(propName, lblTxt, numRows, numCols);
	}

	/**
	 * Creates a new {@link PasswordField} instance.
	 * @param propName
	 * @param lblTxt
	 */
	public static final PasswordField fpassword(String propName, String lblTxt) {
		return new PasswordField(propName, lblTxt);
	}

	/**
	 * Creates a new {@link SelectField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 */
	public static final SelectField fselect(String propName, String lblTxt, Map<String, String> dataMap) {
		return new SelectField(propName, lblTxt, dataMap);
	}

	/**
	 * Creates a new {@link SuggestField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 */
	public static final SuggestField fsuggest(String propName, String lblTxt, Map<String, String> dataMap) {
		return new SuggestField(propName, lblTxt, dataMap);
	}

	/**
	 * Creates a new {@link RadioGroupField} instance.
	 * @param propName
	 * @param lblTxt
	 * @param dataMap
	 * @param renderHorizontal
	 */
	public static final RadioGroupField fradiogroup(String propName, String lblTxt, Map<String, String> dataMap,
			boolean renderHorizontal) {
		return new RadioGroupField(propName, lblTxt, dataMap, renderHorizontal);
	}

	/**
	 * Creates entity date created and date modified read only fields returning
	 * them in an array where the first element is the date created field.
	 * @return DateField array
	 */
	public static final DateField[] createTimestampEntityFields() {
		DateField dateCreated = fdate(Model.DATE_CREATED_PROPERTY, "Created", GlobalFormat.DATE);
		DateField dateModified = fdate(Model.DATE_MODIFIED_PROPERTY, "Modified", GlobalFormat.DATE);
		dateCreated.setReadOnly(true);
		dateModified.setReadOnly(true);
		return new DateField[] {
			dateCreated, dateModified };
	}

	/**
	 * Creates an entity name text field.
	 * @return The created entity name field
	 */
	public static final TextField createNameEntityField() {
		return ftext(Model.NAME_PROPERTY, "Name", 30);
	}

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
