/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field;

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.HasMaxLength;
import com.tll.client.field.IField;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.CSS;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.util.StringUtil;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.NotEmptyValidator;
import com.tll.client.validate.StringLengthValidator;
import com.tll.client.validate.ValidationException;

/**
 * AbstractField - Input field abstraction.
 * @author jpk
 */
public abstract class AbstractField extends Composite implements IField, HasFocus, ClickListener, ChangeListener, FocusListener {

	/**
	 * Reflects the number of instantiated {@link AbstractField}s. This is
	 * required to ensure a unique DOM id which is used by the associated
	 * {@link FieldLabel}.
	 * <p>
	 * NOTE: The counter always increments and never decrements and is not
	 * reflective of the actual number of fields referenced in the app.
	 */
	private static int fieldCounter = 0;

	private static final String dfltReadOnlyEmptyValue = "-";

	/**
	 * The unique DOM element id of this field.
	 */
	private final String domId;

	/**
	 * The designated property name for this field. This is the form input name.
	 */
	private String propName;

	/**
	 * The field value. This property is necessary since a field has the ability
	 * to toggle between editable and read-only states and the value may be set
	 * before the field is initially drawn in the UI.
	 */
	private String value;

	/**
	 * The value the field is set to when {@link #reset()} is called.
	 */
	private String resetValue;

	private boolean required = false;
	private boolean readOnly = false;
	private boolean enabled = true;

	/**
	 * The read-only field Widget.
	 */
	private HTML rof;

	/**
	 * The optional field label that is <em>not</em> a child of this Widget. This
	 * class only *logically* owns the field label and <em>not</em> physically
	 * (dom-wise).
	 */
	private final FieldLabel fldLbl;

	/**
	 * The Composite wrapped widget containing only the editable field or
	 * read-only field Widget.
	 */
	private final SimplePanel pnl = new SimplePanel();

	/**
	 * The desired ancestor Widget reference for the field and the field label
	 * necessary respectively ensuring certain styling rules are properly applied
	 * since the field label is not necessarily a child of this Widget.
	 */
	private Widget container, labelContainer;

	/**
	 * The field validator(s).
	 */
	private IValidator validator;

	/**
	 * The cached validated value.
	 */
	private Object validatedValue;

	/**
	 * Constructor
	 * @param propName The required property name to associate w/ this field.
	 * @param lblTxt The field label text. If <code>null</code>, no field label is
	 *        created.
	 * @throws IllegalArgumentException When a <code>null</code> or zero-length
	 *         property name is given.
	 */
	public AbstractField(String propName, String lblTxt) {
		domId = 'f' + Integer.toString(++fieldCounter);
		setPropertyName(propName);

		// TODO is this style setting necessary?
		pnl.setStyleName(STYLE_FIELD);

		initWidget(pnl);

		// create field label if label text specified
		if(lblTxt != null) {
			fldLbl = new FieldLabel(lblTxt);
			fldLbl.setFor(domId);
			fldLbl.addClickListener(this);
		}
		else {
			fldLbl = null;
		}

		addValidator(new RequirednessValidator());
		addValidator(new MaxLengthValidator());
	}

	public final Widget getFieldWidget() {
		return this;
	}

	/**
	 * Sets the Widget to be the field's containing Widget. This is necessary to
	 * apply certain styling to the appropriate dom node.
	 * @param container The desired containing Widget
	 */
	public final void setContainer(Widget container) {
		this.container = container;
	}

	/**
	 * Sets the Widget to be the field label's containing Widget. This is
	 * necessary to apply certain styling to the appropriate dom node.
	 * @param labelContainer The desired Widget containing the label
	 */
	public final void setLabelContainer(Widget labelContainer) {
		this.labelContainer = labelContainer;
	}

	/**
	 * @return The {@link FieldLabel}. <br>
	 *         <em>NOTE: </em>The field label is <em>NOT</em> a child of this
	 *         composite Widget.
	 */
	public final FieldLabel getFieldLabel() {
		return fldLbl;
	}

	/**
	 * @return the domId
	 */
	public final String getDomId() {
		return domId;
	}

	public final String getPropertyName() {
		return propName;
	}

	public final void setPropertyName(String propName) {
		if(StringUtil.isEmpty(propName)) {
			throw new IllegalArgumentException("A field property name can't be empty.");
		}
		this.propName = propName;
	}

	public final boolean isReadOnly() {
		return readOnly;
	}

	public final void setReadOnly(boolean readOnly) {
		// if we go from editable to read-only mode, carry the value over
		if(isAttached() && !this.readOnly) {
			value = getEditableValue();
		}

		// hide the field label required indicator if we are in read-only state
		if(fldLbl != null) {
			fldLbl.setRequired(readOnly ? false : required);
		}

		this.readOnly = readOnly;
	}

	public final boolean isRequired() {
		return required;
	}

	public final void setRequired(boolean required) {
		// show/hide the field label required indicator
		if(fldLbl != null) {
			fldLbl.setRequired(readOnly ? false : required);
		}

		this.required = required;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public final boolean isVisible() {
		return container == null ? super.isVisible() : container.isVisible();
	}

	/**
	 * We override to handle setting the visibility for the label as well as the
	 * visibility to the containing Widget.
	 */
	@Override
	public final void setVisible(boolean visible) {
		if(container != null) {
			container.setVisible(visible);
		}
		else {
			super.setVisible(visible);
		}
		if(fldLbl != null) {
			if(labelContainer != null) {
				labelContainer.setVisible(visible);
			}
			else {
				fldLbl.setVisible(visible);
			}
		}
	}

	/**
	 * Obtains the editable Widget and optionally sets its value.
	 * @param value The value to to be populated into the form control. If
	 *        <code>null</code>, the UI form value shall <em>not</em> be altered.
	 * @return The editable UI field (form) control.
	 */
	protected abstract HasFocus getEditable(String value);

	/**
	 * The method grabs the current form element value in a consistent manner.
	 * Since the various form controls inherently do <em>not</em> have single
	 * common method to do so, this method exists.
	 */
	protected abstract String getEditableValue();

	public final String getValue() {
		if(!readOnly && isAttached()) {
			value = getEditableValue();
		}
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

	public final String getResetValue() {
		return resetValue;
	}

	public final void setResetValue(String resetValue) {
		this.resetValue = resetValue;
	}

	public final boolean isDirty() {
		final String cv = getValue();
		return ((cv == null && resetValue == null) || cv != null && cv.equals(resetValue));
	}

	public void dirtyCheck() {
		if(isDirty()) {
			addStyleName(STYLE_DIRTY);
		}
		else {
			removeStyleName(STYLE_DIRTY);
		}
	}

	/**
	 * Adds a field validator to this binding.
	 * @param validator The validator to add
	 */
	public final void addValidator(IValidator validator) {
		assert validator != null;
		if(this.validator == null) {
			this.validator = validator;
		}
		else if(this.validator instanceof CompositeValidator) {
			((CompositeValidator) this.validator).add(validator);
		}
		else {
			CompositeValidator cv = new CompositeValidator();
			cv.add(this.validator);
			cv.add(validator);
			this.validator = cv;
		}
	}

	/**
	 * RequirednessValidator - Validator for checking a field's requireness.
	 * @author jpk
	 */
	protected final class RequirednessValidator implements IValidator {

		public Object validate(Object value) throws ValidationException {
			if(isRequired()) {
				value = NotEmptyValidator.INSTANCE.validate(value);
			}
			return value;
		}

	}

	/**
	 * MaxLengthValidator - Validator for checking a field value's maximum
	 * allowable length.
	 * @author jpk
	 */
	protected final class MaxLengthValidator implements IValidator {

		public Object validate(Object value) throws ValidationException {
			if(AbstractField.this instanceof HasMaxLength) {
				final int maxlen = ((HasMaxLength) AbstractField.this).getMaxLen();
				if(maxlen != -1) {
					value = StringLengthValidator.validate(value, -1, maxlen);
				}
			}
			return value;
		}

	}

	public final void validate() throws ValidationException {
		// we start with the field's current value
		Object value = getValue();

		List<Msg> errorMsgs = null;

		try {
			if(validator != null) {
				value = validator.validate(value);
			}
		}
		catch(ValidationException ve) {
			errorMsgs = ve.getValidationMessages();
			throw ve;
		}
		finally {
			markInvalid(errorMsgs != null, errorMsgs);
		}

		this.validatedValue = value;
	}

	public final Object getValidatedValue() {
		return validatedValue;
	}

	public void markInvalid(boolean invalid, List<Msg> msgs) {
		if(invalid) {
			addStyleName(STYLE_INVALID);
			if(msgs != null) {
				addMsgs(msgs);
			}
		}
		else {
			clearMsgs();
			removeStyleName(STYLE_INVALID);
		}
	}

	public final void reset() {
		setValue(resetValue);
		validatedValue = null;
		clearMsgs();
		removeStyleName(STYLE_DIRTY);
		removeStyleName(STYLE_INVALID);
		// draw(); // don't draw here
	}

	/**
	 * @return Valid HTML String displayed in the UI when this field is read-only.
	 *         Sub-classes should override for special behavior. <br>
	 *         E.g.: abbreviating the value for those fields that implement
	 *         {@link HasMaxLength}.
	 */
	protected String getReadOnlyHtml() {
		return (value == null || value.length() == 0) ? dfltReadOnlyEmptyValue : value;
	}

	private void addMsgs(List<Msg> msgs) {
		MsgManager.instance().post(false, msgs, Position.BOTTOM, this, -1, false).show();
	}

	private void clearMsgs() {
		MsgManager.instance().clear(this, true);
	}

	private void toggleMsgs() {
		MsgManager.instance().toggle(this, true);
	}

	public void draw() {
		Widget fw;
		if(readOnly) {
			if(rof == null) {
				rof = new HTML();
			}
			rof.setHTML(getReadOnlyHtml());
			fw = rof;
		}
		else {
			fw = (Widget) getEditable(value);
			// apply disabled property to form control directly
			fw.getElement().setPropertyBoolean(CSS.DISABLED, !enabled);
		}

		// set the field widget and its dom id property
		if(pnl.getWidget() != fw) {
			fw.getElement().setPropertyString("id", domId);
			pnl.setWidget(fw);
		}

		// resolve the containers
		final Widget fldContainer = container == null ? this : container;
		final Widget lblContainer = fldLbl == null ? null : labelContainer == null ? fldLbl : labelContainer;

		// apply enabled property to "containing" widget
		if(enabled) {
			fldContainer.removeStyleName(CSS.DISABLED);
			if(lblContainer != null) lblContainer.removeStyleName(CSS.DISABLED);
		}
		else {
			fldContainer.addStyleName(CSS.DISABLED);
			if(lblContainer != null) lblContainer.addStyleName(CSS.DISABLED);
		}

		if(!enabled || readOnly) {
			// remove all msgs, edit and validation styling
			clearMsgs();
			removeStyleName(STYLE_INVALID);
			removeStyleName(STYLE_DIRTY);
		}
		else if(enabled && !readOnly) {
			// show/hide edit styling
			if(isDirty()) {
				addStyleName(STYLE_DIRTY);
			}
			else {
				removeStyleName(STYLE_DIRTY);
			}
		}
	}

	public void onClick(Widget sender) {
		// toggle the display of any bound UI msgs for this field when the field
		// label is clicked
		if(sender == fldLbl) toggleMsgs();
	}

	public final void addKeyboardListener(KeyboardListener listener) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable(null).addKeyboardListener(listener);
		// }
	}

	public final void removeKeyboardListener(KeyboardListener listener) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable(null).removeKeyboardListener(listener);
		// }
	}

	public final void addFocusListener(FocusListener listener) {
		// NOTE: we *always* add a focus listener to ensure proper field binding
		// behavior
		// TODO can we optimize this?
		// if(!isReadOnly()) {
		getEditable(null).addFocusListener(listener);
		// }
	}

	public final void removeFocusListener(FocusListener listener) {
		// NOTE: we *always* remove a focus listener to ensure proper field binding
		// behavior
		// TODO can we optimize this?
		// if(!isReadOnly()) {
		getEditable(null).removeFocusListener(listener);
		// }
	}

	public final int getTabIndex() {
		return isReadOnly() ? -1 : getEditable(null).getTabIndex();
	}

	public final void setAccessKey(char key) {
		if(!isReadOnly()) {
			getEditable(null).setTabIndex(key);
		}
	}

	public final void setFocus(boolean focused) {
		if(!isReadOnly()) {
			getEditable(null).setFocus(focused);
		}
	}

	public final void setTabIndex(int index) {
		if(!isReadOnly()) {
			getEditable(null).setTabIndex(index);
		}
	}

	public void onChange(Widget sender) {
		assert sender == this;
		// dirty check
		dirtyCheck();
	}

	public void onFocus(Widget sender) {
		// no-op
	}

	public void onLostFocus(Widget sender) {
		// valid check
		try {
			validate();
		}
		catch(ValidationException e) {
			// no-op
		}
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		clearMsgs();
	}

	@Override
	public final String toString() {
		return propName;
	}
}
