/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field;

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
import com.tll.client.bind.IBindable;
import com.tll.client.bind.IBindingAction;
import com.tll.client.model.MalformedPropPathException;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;
import com.tll.client.msg.Msg;
import com.tll.client.msg.MsgManager;
import com.tll.client.ui.AbstractBoundWidget;
import com.tll.client.ui.TimedPositionedPopup.Position;
import com.tll.client.util.StringUtil;
import com.tll.client.validate.NotEmptyValidator;
import com.tll.client.validate.StringLengthValidator;
import com.tll.client.validate.ValidationException;

/**
 * AbstractField - Input field abstraction.
 * @author jpk
 */

public abstract class AbstractField<V> extends AbstractBoundWidget<Object, V, IBindingAction<IBindable>, Model> implements IField, HasFocus, ClickListener, ChangeListener {

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
	 * The designated property name for this field. This is used as the the form
	 * input name.
	 */
	private String propName;

	/**
	 * The field value. This property is necessary since a field has the ability
	 * to toggle between editable and read-only states and the value may be set
	 * before the field is initially drawn in the UI.
	 */
	private String fvalue;

	private boolean required = false;
	private boolean readOnly = false;
	private boolean enabled = true;

	/**
	 * Is this field's value different that the initial "reset" value?
	 */
	private boolean dirty;

	/**
	 * Text that appears when the mouse hovers over the field.
	 */
	private String helpText;

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
	 * Constructor
	 * @param propName The unique field propName.
	 * @param lblTxt The field label text. If <code>null</code>, no field label is
	 *        created.
	 * @throws IllegalArgumentException When a <code>null</code> or zero-length
	 *         property propName is given.
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

		// set the default renderer
		// setRenderer(ToStringRenderer.INSTANCE);
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
			throw new IllegalArgumentException("A field propName can't be empty.");
		}
		this.propName = propName;
	}

	public final boolean isReadOnly() {
		return readOnly;
	}

	public final void setReadOnly(boolean readOnly) {
		// hide the field label required indicator if we are in read-only state
		if(fldLbl != null) fldLbl.setRequired(readOnly ? false : required);

		this.readOnly = readOnly;
		if(isAttached()) draw();
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
		if(isAttached()) draw();
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
	 * @return the helpText
	 */
	public final String getHelpText() {
		return helpText;
	}

	/**
	 * @param helpText the helpText to set
	 */
	public final void setHelpText(String helpText) {
		this.helpText = helpText;
		if(isAttached()) draw();
	}

	/**
	 * Obtains the editable Widget and optionally sets its value.
	 * @param value The value to to be populated into the form control. If
	 *        <code>null</code>, the UI form value shall <em>not</em> be altered.
	 * @return The editable UI field (form) control.
	 */
	protected abstract HasFocus getEditable(String value);

	/**
	 * The method grabs the current form element value in a consistent manner in
	 * String form. Since the various form controls inherently do <em>not</em>
	 * have single common method to do so, this method exists.
	 */
	protected abstract String getEditableValue();

	protected final String getFieldValue() {
		// this isn't necessary as the onChange event now handles it
		// if(!readOnly && isAttached()) {
		// value = getEditableValue();
		// }
		return fvalue;
	}

	protected final void setFieldValue(String value) {
		this.fvalue = value;
		if(isAttached()) draw();
	}

	private void dirtyCheck() {
		if(dirty) {
			addStyleName(STYLE_DIRTY);
		}
		else {
			removeStyleName(STYLE_DIRTY);
		}
	}

	/*
	protected final void addValidator(IValidator validator) {
		if(validator == null || validator == NotEmptyValidator.INSTANCE || validator instanceof StringLengthValidator) {
			throw new IllegalArgumentException("Invalid field validator");
		}
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
	
	protected final void removeValidator(IValidator validator) {
		if(validator == null || this.validator == null) return;
		if(this.validator == validator) {
			this.validator = null;
		}
		else if(this.validator instanceof CompositeValidator) {
			CompositeValidator cv = (CompositeValidator) this.validator;
			cv.remove(validator);
		}
	}
	*/

	public final void validate() throws ValidationException {
		validate(getValue());
	}

	public Object validate(Object value) throws ValidationException {
		// check field requiredness
		if(isRequired()) {
			value = NotEmptyValidator.INSTANCE.validate(value);
		}

		// check max length
		if(this instanceof HasMaxLength) {
			final int maxlen = ((HasMaxLength) this).getMaxLen();
			if(maxlen != -1) {
				value = StringLengthValidator.validate(value, -1, maxlen);
			}
		}

		/*
		List<Msg> errorMsgs = null;
		
		try {
			// check field requiredness
			if(isRequired()) {
				value = NotEmptyValidator.INSTANCE.validate(value);
			}

			// check max length
			if(this instanceof HasMaxLength) {
				final int maxlen = ((HasMaxLength) this).getMaxLen();
				if(maxlen != -1) {
					value = StringLengthValidator.validate(value, -1, maxlen);
				}
			}
		}
		catch(ValidationException ve) {
			errorMsgs = ve.getValidationMessages();
		}

		try {
			if(validator != null) {
				value = validator.validate(value);
			}
		}
		catch(ValidationException ve) {
			if(errorMsgs == null) {
				errorMsgs = ve.getValidationMessages();
			}
			else {
				errorMsgs.addAll(ve.getValidationMessages());
			}
			throw ve;
		}
		finally {
			clearMsgs();
			markInvalid(errorMsgs != null, errorMsgs);
		}

		this.validatedValue = value;
		*/

		return value;
	}

	public final void markInvalid(boolean invalid, List<Msg> msgs) {
		if(invalid) {
			removeStyleName(STYLE_DIRTY);
			addStyleName(STYLE_INVALID);
			if(msgs != null) {
				addMsgs(msgs);
			}
		}
		else {
			removeStyleName(STYLE_INVALID);
		}
	}

	public final void reset() {

		// reset the value
		// TODO verify this
		getAction().execute(this);

		dirty = false;

		// handle styling
		clearMsgs();
		removeStyleName(STYLE_DIRTY);
		removeStyleName(STYLE_INVALID);

		if(isAttached()) draw();
	}

	/**
	 * @return Valid HTML String displayed in the UI when this field is read-only.
	 *         Sub-classes should override for special behavior. <br>
	 *         E.g.: abbreviating the value for those fields that implement
	 *         {@link HasMaxLength}.
	 */
	protected String getReadOnlyHtml() {
		return (fvalue == null || fvalue.length() == 0) ? dfltReadOnlyEmptyValue : fvalue;
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

	private void draw() {
		Widget fw;
		if(readOnly) {
			if(rof == null) {
				rof = new HTML();
				// set help text
				rof.setTitle(helpText);
			}
			rof.setHTML(getReadOnlyHtml());
			fw = rof;
		}
		else {
			fw = (Widget) getEditable(fvalue);
			// set help text
			fw.setTitle(helpText);
			// apply disabled property to form control directly
			fw.getElement().setPropertyBoolean(Style.DISABLED, !enabled);
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
			fldContainer.removeStyleName(Style.DISABLED);
			if(lblContainer != null) lblContainer.removeStyleName(Style.DISABLED);
		}
		else {
			fldContainer.addStyleName(Style.DISABLED);
			if(lblContainer != null) lblContainer.addStyleName(Style.DISABLED);
		}

		if(!enabled || readOnly) {
			// remove all msgs, edit and validation styling
			clearMsgs();
			removeStyleName(STYLE_INVALID);
			removeStyleName(STYLE_DIRTY);
		}
		else if(enabled && !readOnly) {
			// show/hide edit styling
			dirtyCheck();
		}
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
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable(null).addFocusListener(listener);
		// }
	}

	public final void removeFocusListener(FocusListener listener) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable(null).removeFocusListener(listener);
		// }
	}

	public final int getTabIndex() {
		return readOnly ? -1 : getEditable(null).getTabIndex();
	}

	public final void setAccessKey(char key) {
		if(!readOnly) {
			getEditable(null).setTabIndex(key);
		}
	}

	public final void setFocus(boolean focused) {
		if(!readOnly) {
			getEditable(null).setFocus(focused);
		}
	}

	public final void setTabIndex(int index) {
		if(!readOnly) {
			getEditable(null).setTabIndex(index);
		}
	}

	public void onClick(Widget sender) {
		// toggle the display of any bound UI msgs for this field when the field
		// label is clicked
		if(sender == fldLbl) toggleMsgs();
	}

	public void onChange(Widget sender) {
		assert sender == getEditable(null);

		dirty = true;

		// update the value
		this.fvalue = getEditableValue();

		// valid check
		try {
			validate();
			// we are valid so do dirty check
			dirtyCheck();
		}
		catch(ValidationException e) {
			// no-op
		}
	}

	public final V getProperty(String propPath) throws PropertyPathException {
		if(!this.propName.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		return getValue();
	}

	public void setProperty(String propPath, Object value) throws PropertyPathException {
		if(!this.propName.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		setValue(value);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		draw();
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
