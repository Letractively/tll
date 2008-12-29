/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field;

import java.util.List;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.KeyboardListener;
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
	private FieldLabel fldLbl;

	/**
	 * The {@link Composite} widget containing the field's contents.
	 */
	private final FlowPanel pnl = new FlowPanel();

	/**
	 * The desired ancestor Widget reference for the field and the field label
	 * necessary respectively ensuring certain styling rules are properly applied
	 * since the field label is not necessarily a child of this Widget.
	 */
	private Widget container, labelContainer;

	/**
	 * Constructor
	 * @param propName The property name associated with this field
	 * @param labelText The optional field label text
	 * @param helpText The options field help text that will appear when the mouse
	 *        hovers.
	 * @throws IllegalArgumentException When no property propName is given
	 */
	public AbstractField(String propName, String labelText, String helpText) {
		domId = 'f' + Integer.toString(++fieldCounter);
		setPropertyName(propName);

		// set the label
		setLabelText(labelText);

		// set the help text
		setHelpText(helpText);

		// TODO is this style setting necessary?
		pnl.setStyleName(STYLE_FIELD);

		initWidget(pnl);
	}

	/*
	 * We need to honor the IField contract and this method is declated soley to make it publicly visible.
	 */
	@Override
	public final Widget getWidget() {
		return super.getWidget();
	}

	/**
	 * Sets the Widget to be the field's containing Widget. This is necessary to
	 * apply certain styling to the appropriate dom node.
	 * @param fieldContainer The desired containing Widget
	 */
	public final void setFieldContainer(Widget fieldContainer) {
		this.container = fieldContainer;
	}

	/**
	 * Sets the Widget to be the field label's containing Widget. This is
	 * necessary to apply certain styling to the appropriate dom node.
	 * @param labelContainer The desired Widget containing the label
	 */
	public final void setFieldLabelContainer(Widget labelContainer) {
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
	 * Set the field's associated label.
	 * @param labelText The label text. If <code>null</code>, the label will be
	 *        removed.
	 */
	public void setLabelText(String labelText) {
		if(fldLbl == null) {
			fldLbl = new FieldLabel();
			fldLbl.setFor(domId);
			fldLbl.addClickListener(this);
		}
		fldLbl.setText(labelText == null ? "" : labelText);
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

	public void setReadOnly(boolean readOnly) {
		// hide the field label required indicator if we are in read-only state
		if(fldLbl != null) fldLbl.setRequired(readOnly ? false : required);

		this.readOnly = readOnly;
		if(isAttached()) draw();
	}

	public final boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		// show/hide the field label required indicator
		if(fldLbl != null) {
			fldLbl.setRequired(readOnly ? false : required);
		}
		this.required = required;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(isAttached()) draw();
	}

	/**
	 * We override to handle setting the visibility for the label as well as the
	 * visibility to the field and label containers.
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if(fldLbl != null) {
			fldLbl.setVisible(visible);
		}

		if(container != null) {
			container.setVisible(visible);
		}
		if(labelContainer != null) {
			labelContainer.setVisible(visible);
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
	public void setHelpText(String helpText) {
		this.helpText = helpText;
		if(isAttached()) draw();
	}

	/**
	 * Obtains the editable form control Widget.
	 * @return The form control Widget
	 */
	protected abstract HasFocus getEditable();

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

	private void addMsgs(List<Msg> msgs) {
		MsgManager.instance().post(false, msgs, Position.BOTTOM, this, -1, false).show();
	}

	private void clearMsgs() {
		MsgManager.instance().clear(this, true);
	}

	private void toggleMsgs() {
		MsgManager.instance().toggle(this, true);
	}

	protected void draw() {

		Widget formWidget;

		// assemble?
		// NOTE: we *always* have the editable form control part of the DOM so as to
		// make sure change type events are handled etc.
		// the editable form control widget drives the field's behavior and is the
		// master
		formWidget = (Widget) getEditable();
		if(pnl.getWidgetCount() == 0) {
			formWidget.getElement().setPropertyString("id", domId);
			pnl.add(formWidget);
		}
		else if(formWidget != pnl.getWidget(0)) {
			pnl.insert(formWidget, 0);
		}
		assert formWidget != null;
		formWidget.setTitle(helpText);

		if(readOnly) {
			if(pnl.getWidgetCount() != 2) {
				assert rof == null;
				rof = new HTML();
				pnl.add(rof);
			}
			assert rof != null;
			// set help text
			rof.setTitle(helpText);

			// final Object val = getValue();
			// String sval = val == null ? null :
			// ToStringRenderer.INSTANCE.render(val);
			String sval = getText();
			sval = StringUtil.isEmpty(sval) ? dfltReadOnlyEmptyValue : sval;
			rof.setText(sval);
		}

		// set readonly/editable display
		if(rof != null) rof.setVisible(readOnly);
		formWidget.setVisible(!readOnly);

		// apply disabled property
		formWidget.getElement().setPropertyBoolean(Style.DISABLED, !enabled);

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
		getEditable().addKeyboardListener(listener);
		// }
	}

	public final void removeKeyboardListener(KeyboardListener listener) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable().removeKeyboardListener(listener);
		// }
	}

	public final void addFocusListener(FocusListener listener) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable().addFocusListener(listener);
		// }
	}

	public final void removeFocusListener(FocusListener listener) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		getEditable().removeFocusListener(listener);
		// }
	}

	public final int getTabIndex() {
		return readOnly ? -1 : getEditable().getTabIndex();
	}

	public final void setAccessKey(char key) {
		if(!readOnly) {
			getEditable().setTabIndex(key);
		}
	}

	public final void setFocus(boolean focused) {
		if(!readOnly) {
			getEditable().setFocus(focused);
		}
	}

	public final void setTabIndex(int index) {
		if(!readOnly) {
			getEditable().setTabIndex(index);
		}
	}

	public void onClick(Widget sender) {
		// toggle the display of any bound UI msgs for this field when the field
		// label is clicked
		if(sender == fldLbl) toggleMsgs();
	}

	public void onChange(Widget sender) {
		assert sender == getEditable();

		dirty = true;

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

	public final Object getProperty(String propPath) throws PropertyPathException {
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
