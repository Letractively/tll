/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field;

import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.field.HasFormat;
import com.tll.client.field.HasMaxLength;
import com.tll.client.field.IField;
import com.tll.client.model.IPropertyBinding;
import com.tll.client.model.IPropertyValue;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.msg.Msg.MsgLevel;
import com.tll.client.ui.CSS;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.client.util.StringUtil;
import com.tll.client.util.Fmt.DateFormat;
import com.tll.client.util.Fmt.DecimalFormat;
import com.tll.client.validate.BooleanValidator;
import com.tll.client.validate.CharacterValidator;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.DecimalValidator;
import com.tll.client.validate.IValidationFeedback;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.IntegerValidator;
import com.tll.client.validate.NotEmptyValidator;
import com.tll.client.validate.StringLengthValidator;
import com.tll.client.validate.ValidationException;
import com.tll.model.schema.PropertyMetadata;

/**
 * AbstractField - Input field abstraction.
 * @author jpk
 */
public abstract class AbstractField extends Composite implements IField, HasFocus, ClickListener, FocusListener {

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

	private static final String styleChanged = "changed";
	private static final String styleError = MsgLevel.ERROR.getName().toLowerCase();
	private static final String styleWarn = MsgLevel.WARN.getName().toLowerCase();

	/**
	 * IntrinsicValidator - validates the field's intrinsic properties. When
	 * validating, this validator is always invoked first.
	 * @author jpk
	 */
	private class IntrinsicValidator implements IValidator {

		public Object validate(Object value) throws ValidationException {
			if(isRequired()) {
				NotEmptyValidator.INSTANCE.validate(value);
			}
			if(this instanceof HasMaxLength) {
				final int maxlen = ((HasMaxLength) this).getMaxLen();
				if(maxlen != -1) {
					StringLengthValidator.validate(value, -1, maxlen);
				}
			}
			return value;
		}

	}

	/**
	 * The unique DOM element id of this field.
	 */
	private final String domId;

	/**
	 * The field name synonymous w/ the property name when considered in a
	 * property path context. Can NOT be <code>null</code>.
	 */
	private String propName;

	/**
	 * The field value. This property is necessary when considering read-only
	 * state and needing the ability to set a field's value PRIOR to rendering.
	 */
	protected String value;

	/**
	 * The value the field is set at when {@link #reset()} is called.
	 */
	private String resetValue;

	private boolean required = false;
	private boolean readOnly = false;
	private boolean enabled = true;

	private HTML rof; // the read-only field

	/**
	 * The default value to display when the value is empty. Ok if
	 * <code>null<code>.
	 */
	private String defaultUiValue;

	/**
	 * The field edits.
	 */
	private final CompositeValidator validators = new CompositeValidator();

	/**
	 * The validated model value that is set only when the field value is altered.
	 */
	private Object modelValue;

	/**
	 * Internal flag for detecting if the field's value is changed.
	 */
	protected boolean changed;

	/**
	 * The optional FieldLabel created lazily.
	 */
	protected FieldLabel fldLbl;

	/**
	 * The composite wrapped widget that either the read-only or editable
	 * {@link Widget} is attached to at load time.
	 */
	private final SimplePanel pnl = new SimplePanel();

	/**
	 * The parents for the field control and field label control. Necessary refs
	 * to properly show/hide, enable/disable a field and its label.
	 */
	private Widget fieldParent, fieldLabelParent;

	/**
	 * Constructor
	 * @param propName The property name to associate w/ this field.
	 * @param lblTxt The field label text.
	 * @throws IllegalArgumentException When a <code>null</code> or zero-length
	 *         property name is given.
	 */
	public AbstractField(String propName, String lblTxt) {

		this.domId = 'f' + Integer.toString(++fieldCounter);

		// pnl.setStyleName(CSS_FIELD);
		initWidget(pnl);

		setPropertyName(propName);

		// create field label if label text specified
		if(lblTxt != null) {
			fldLbl = new FieldLabel(lblTxt);
			fldLbl.setFor(domId);
			fldLbl.addClickListener(this);
		}

		// do core field validation
		// IMPT: the core validator is first in the list of validators where the
		// last in the list
		// shall provide the transformed model value
		addValidator(new IntrinsicValidator());
	}

	public void setFieldParent(Widget fieldParent) {
		this.fieldParent = fieldParent;
	}

	public void setFieldLabelParent(Widget fieldLabelParent) {
		this.fieldLabelParent = fieldLabelParent;
	}

	/**
	 * @return The {@link FieldLabel}. <em>NOTE: </em>The field label is
	 *         <em>NOT</em> a child of this composite Widget.
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

	private boolean isDrawn() {
		return pnl.getWidget() != null;
	}

	private void redraw() {
		pnl.clear();
		if(readOnly) {
			if(rof == null) {
				rof = new HTML();
			}
			String val = getReadOnlyHtml();
			if(val != null) rof.setHTML(val);
			pnl.add(rof);
		}
		else {
			pnl.add((Widget) getEditable(null));
		}
		pnl.getWidget().getElement().setPropertyString("id", domId);
	}

	public final boolean isReadOnly() {
		return readOnly;
	}

	public final void setReadOnly(boolean readOnly) {
		if(readOnly != this.readOnly) {
			this.readOnly = readOnly;
			if(readOnly && defaultUiValue == null) {
				defaultUiValue = dfltReadOnlyEmptyValue;
			}
			if(fldLbl != null) {
				fldLbl.setRequired(readOnly ? false : required);
			}
			redraw();
			if(!readOnly) {
				Widget w = (Widget) getEditable(null);
				w.getElement().setPropertyBoolean("disabled", !enabled);
			}
		}
	}

	public final boolean isRequired() {
		return required;
	}

	public final void setRequired(boolean required) {
		if(this.required != required) {
			this.required = required;
			if(fldLbl != null) {
				fldLbl.setRequired(readOnly ? false : required);
			}
		}
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		if(enabled != this.enabled) {
			this.enabled = enabled;

			clearValidationStyling();

			// resolve the parents
			Widget parent = fieldParent == null ? this : fieldParent;
			Widget labelParent = fldLbl == null ? null : fieldLabelParent == null ? fldLbl : fieldLabelParent;

			if(enabled) {
				parent.removeStyleName(CSS.DISABLED);
				if(labelParent != null) labelParent.removeStyleName(CSS.DISABLED);
			}
			else {
				parent.addStyleName(CSS.DISABLED);
				if(labelParent != null) labelParent.addStyleName(CSS.DISABLED);
			}

			if(!readOnly) {
				Widget w = (Widget) getEditable(null);
				w.getElement().setPropertyBoolean("disabled", !enabled);
			}
		}
	}

	@Override
	public final boolean isVisible() {
		if(fieldParent != null) return fieldParent.isVisible();
		return super.isVisible();
	}

	/**
	 * We override to handle setting the visibility for the label as well and also
	 * to apply visibility to the directed parent Widget.
	 */
	@Override
	public final void setVisible(boolean visible) {
		if(fieldParent != null) {
			fieldParent.setVisible(visible);
		}
		else {
			super.setVisible(visible);
		}
		if(fldLbl != null) {
			if(fieldLabelParent != null) {
				fieldLabelParent.setVisible(visible);
			}
			else {
				fldLbl.setVisible(visible);
			}
		}
	}

	/**
	 * Obtains the editable Widget and optionally sets its value.
	 * @param value The value to to be populated into the form control. If
	 *        <code>null</code>, the UI form value shall <em>not</em> be
	 *        altered.
	 * @return The ui field control.
	 */
	protected abstract HasFocus getEditable(String value);

	/**
	 * The method grabs the current form element value and is employed when this
	 * field is editable and a call to {@link #getValue()} is invoked.
	 * @return The editable form element value.
	 */
	protected abstract String getEditableValue();

	public final String getValue() {
		if(!readOnly && isAttached()) {
			value = getEditableValue();
		}
		return value;
	}

	public final void setValue(String value) {
		value = value == null ? (defaultUiValue == null ? "" : defaultUiValue) : value;
		this.value = value;
		if(resetValue == null) resetValue = value;

		if(isAttached()) {
			if(readOnly) {
				assert rof != null;
				rof.setText(value);
			}
			else {
				getEditable(value);
			}
		}
	}

	/**
	 * @return Valid HTML String displayed in the UI when this field is read-only.
	 *         Sub-classes should override for special bahavior. E.g.:
	 *         abbreviating the value for those fields that implement
	 *         {@link HasMaxLength}.
	 */
	protected String getReadOnlyHtml() {
		return (value == null || value.length() == 0) ? defaultUiValue : value;
	}

	private void clearValidationStyling() {
		MsgManager.instance.clear(this, false);
		removeStyleName(styleError);
		removeStyleName(styleWarn);
	}

	private void clearEditStyling() {
		removeStyleName(styleChanged);
	}

	public final void reset() {
		clearValidationStyling();
		clearEditStyling();
		setValue(resetValue);
	}

	public void clear() {
		clearValidationStyling();
		clearEditStyling();
		setValue(null);
	}

	public final void bindModel(IPropertyBinding binding) {
		if(binding instanceof IPropertyValue == false)
			throw new IllegalArgumentException("Non-group fields may only bind to property values.");
		final IPropertyValue pv = (IPropertyValue) binding;

		GlobalFormat format = (this instanceof HasFormat) ? ((HasFormat) this).getFormat() : null;

		// set property meta data related field properties
		PropertyMetadata metadata = pv.getMetadata();
		if(metadata != null) {

			setRequired(metadata.isRequired() && !metadata.isManaged());
			if(this instanceof HasMaxLength) {
				((HasMaxLength) this).setMaxLen(metadata.getMaxLen());
			}

			// critical: set the type coercion validator
			switch(metadata.getPropertyType()) {
				case BOOL:
					addValidator(BooleanValidator.INSTANCE);
					break;
				case CHAR:
					addValidator(CharacterValidator.INSTANCE);
					break;
				case DATE: {
					DateFormat dateFormat = format == null ? DateFormat.DATE : Fmt.getDateFormat(format);
					assert dateFormat != null;
					switch(dateFormat) {
						case DATE:
							addValidator(DateValidator.DATE_VALIDATOR);
							break;
						case TIME:
							addValidator(DateValidator.TIME_VALIDATOR);
							break;
						default:
						case TIMESTAMP:
							addValidator(DateValidator.TIMESTAMP_VALIDATOR);
							break;
					}
					break;
				}
				case FLOAT:
				case DOUBLE: {
					DecimalFormat decimalFormat = format == null ? DecimalFormat.DECIMAL : Fmt.getDecimalFormat(format);
					assert decimalFormat != null;
					switch(decimalFormat) {
						case CURRENCY:
							addValidator(DecimalValidator.CURRENCY_VALIDATOR);
							break;
						case PERCENT:
							addValidator(DecimalValidator.PERCENT_VALIDATOR);
							break;
						case DECIMAL:
							addValidator(DecimalValidator.DECIMAL_VALIDATOR);
							break;
					}
					break;
				}
				case INT:
				case LONG:
					addValidator(IntegerValidator.INSTANCE);
					break;

				case STRING_MAP:
					// TODO handle string map type coercion ?
					break;

				case ENUM:
				case STRING:
					// no type coercion validator needed
					break;

				default:
					throw new IllegalStateException("Unhandled model property type: " + metadata.getPropertyType().name());
			}
		}

		// stringize value of the model property value
		String strval = Fmt.format(pv.getValue(), format);

		// assign the reset value
		resetValue = strval;

		// clear out styling and set the current value to the just assigned reset
		// value
		reset();
	}

	public boolean updateModel(IPropertyBinding binding) {
		if(binding instanceof IPropertyValue == false)
			throw new IllegalArgumentException("Non-group fields may only update model property values.");
		final IPropertyValue pv = (IPropertyValue) binding;
		if(modelValue != null) {
			// NOTE: there is potential for the setProp call to throw an
			// excecption
			// but this souldn't happen if the model has been "properly" bound and the
			// validators property set
			pv.setValue(modelValue);
			return true;
		}
		return false;
	}

	public final void handleValidationFeedback(IValidationFeedback feedback) {
		List<Msg> msgs = feedback.getValidationMessages();
		if(msgs == null) return;
		boolean error = false, warn = false;
		for(Msg msg : msgs) {
			if(msg.getLevel().isError())
				error = true;
			else if(msg.getLevel() == MsgLevel.WARN) warn = true;
		}
		if(error)
			addStyleName(styleError);
		else if(warn) addStyleName(styleWarn);
		MsgManager.instance.post(false, msgs, Position.BOTTOM, this, -1, true).show();
	}

	public final IValidator getValidators() {
		return validators;
	}

	public final void addValidator(IValidator validator) {
		validators.add(validator);
	}

	public void onClick(Widget sender) {
		if(sender == fldLbl) MsgManager.instance.toggle(this, false);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		clearValidationStyling();
		if(!isDrawn()) redraw();
	}

	public final void addKeyboardListener(KeyboardListener listener) {
		if(!isReadOnly()) {
			getEditable(null).addKeyboardListener(listener);
		}
	}

	public final void removeKeyboardListener(KeyboardListener listener) {
		if(!isReadOnly()) {
			getEditable(null).removeKeyboardListener(listener);
		}
	}

	public final void addFocusListener(FocusListener listener) {
		if(!isReadOnly()) {
			getEditable(null).addFocusListener(listener);
		}
	}

	public final void removeFocusListener(FocusListener listener) {
		if(!isReadOnly()) {
			getEditable(null).removeFocusListener(listener);
		}
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

	public final void onFocus(Widget sender) {
	}

	/**
	 * This is when we perform actual field validation and retain the validated
	 * model value for later application when
	 * {@link #updateModel(IPropertyBinding)} is called.
	 */
	public final void onLostFocus(Widget sender) {
		final String currentValue = getValue();
		assert currentValue != null;

		// check if changed
		if(currentValue.equals(resetValue)) {
			clearValidationStyling();
			removeStyleName(styleChanged);
			modelValue = null;
		}
		else {
			try {
				modelValue = validators.validate(currentValue);
				addStyleName(styleChanged);
				clearValidationStyling();
			}
			catch(ValidationException ve) {
				removeStyleName(styleChanged);
				handleValidationFeedback(ve);
			}
		}

		changed = false;
	}

	@Override
	public final String toString() {
		return propName;
	}
}
