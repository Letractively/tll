/**
 * The Logic Lab
 * @author jpk Sep 14, 2007
 */
package com.tll.client.ui.field;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.ui.AbstractBoundWidget;
import com.tll.client.ui.IBoundWidget;
import com.tll.client.ui.IHasFormat;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.BooleanValidator;
import com.tll.client.validate.CharacterValidator;
import com.tll.client.validate.CompositeValidator;
import com.tll.client.validate.DateValidator;
import com.tll.client.validate.DecimalValidator;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.IntegerValidator;
import com.tll.client.validate.NotEmptyValidator;
import com.tll.client.validate.StringLengthValidator;
import com.tll.client.validate.ValidationException;
import com.tll.common.bind.IBindable;
import com.tll.common.model.MalformedPropPathException;
import com.tll.common.model.PropertyPathException;
import com.tll.common.util.ObjectUtil;
import com.tll.common.util.StringUtil;
import com.tll.model.schema.IPropertyMetadataProvider;
import com.tll.model.schema.PropertyMetadata;

/**
 * AbstractField - Base class for non-group {@link IField}s.
 * @param <B> the bound type
 * @param <V> native field type
 * @author jpk
 */
public abstract class AbstractField<B, V> extends AbstractBoundWidget<B, V, IBindable> implements IField<B, V>,
		Focusable, ClickHandler, ChangeHandler {

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
	 * The field name.
	 */
	private String name;

	/**
	 * The full property path intended to uniquely identify this field relative to
	 * a common root construct.
	 * <p>
	 * <em>NOTE: This is distinct from the field's <code>name</code> property.
	 */
	private String property;

	private boolean required = false;
	private boolean readOnly = false;
	private boolean enabled = true;

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
	 * The field validator(s).
	 */
	private CompositeValidator validator;

	/**
	 * The initial value needed to perform a reset operation.
	 */
	private V initialValue;

	/**
	 * Flag to indicate whether or not the initial value is set.
	 */
	private boolean initialValueSet;

	/**
	 * Constructor
	 * @param name The field name which should be unique relative to any sibling
	 *        fields under a common parent (field group).
	 * @param propName The property name associated with this field which should
	 *        be unique relative to a common ancestor (field group).
	 * @param labelText The optional field label text
	 * @param helpText The options field help text that will appear when the mouse
	 *        hovers.
	 * @throws IllegalArgumentException When no property propName is given
	 */
	public AbstractField(String name, String propName, String labelText, String helpText) {
		domId = 'f' + Integer.toString(++fieldCounter);
		setName(name);
		setPropertyName(propName);

		// set the label
		setLabelText(labelText);

		// set the help text
		setHelpText(helpText);

		// TODO is this style setting necessary?
		pnl.setStyleName(Styles.FIELD);

		initWidget(pnl);
	}

	/*
	 * We need to honor the IField contract and this method is declated soley to make it publicly visible.
	 */
	@Override
	public final Widget getWidget() {
		return this;
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
			fldLbl.addClickHandler(this);
		}
		fldLbl.setText(labelText == null ? "" : labelText);
	}

	/**
	 * @return the domId
	 */
	public final String getDomId() {
		return domId;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		if(StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("A field must have a name.");
		}
		this.name = name;
	}

	public final String getPropertyName() {
		if(StringUtil.isEmpty(name)) {
			throw new IllegalArgumentException("A field must have a property name.");
		}
		return property;
	}

	public final void setPropertyName(String propName) {
		this.property = propName;
	}

	public final void clear() {
		setValue(null);
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
	public final void setHelpText(String helpText) {
		if(!ObjectUtil.equals(this.helpText, helpText)) {
			this.helpText = helpText;
			if(isAttached()) draw();
		}
	}
	
	/**
	 * Obtains the editable form control Widget.
	 * @return The form control Widget
	 */
	protected abstract Focusable getEditable();

	/**
	 * Either marks or un-marks this field as dirty in the UI based on its current
	 * value and the set initial value.
	 */
	private void markDirty() {
		if(initialValueSet) {
			if(!ObjectUtil.equals(initialValue, getValue())) {
				addStyleName(Styles.DIRTY);
			}
			else {
				removeStyleName(Styles.DIRTY);
			}
		}
	}

	public final void addValidator(IValidator validator) {
		if(validator != null && validator != NotEmptyValidator.INSTANCE
				&& (validator instanceof StringLengthValidator == false)) {
			if(this.validator == null) {
				this.validator = new CompositeValidator();
			}
			this.validator.add(validator);
		}
	}

	public final void applyPropertyMetadata(IPropertyMetadataProvider provider) {
		// Log.debug("AbstractField.applyPropertyMetadata() for " + toString());
		final PropertyMetadata metadata = provider.getPropertyMetadata(getPropertyName());
		if(metadata == null) {
			Log.warn("No property metadata found for field: " + toString());
		}
		else {
			// requiredness
			setRequired(metadata.isRequired() && !metadata.isManaged());

			// maxlength
			if(this instanceof IHasMaxLength) {
				((IHasMaxLength) this).setMaxLen(metadata.getMaxLen());
			}

			// set the type coercion validator
			switch(metadata.getPropertyType()) {
				case BOOL:
					addValidator(BooleanValidator.INSTANCE);
					break;
				case CHAR:
					addValidator(CharacterValidator.INSTANCE);
					break;
				case DATE:
					if(this instanceof IHasFormat) {
						final GlobalFormat format = ((IHasFormat) this).getFormat();
						if(format != null && format.isDateFormat()) {
							addValidator(DateValidator.instance(format));
						}
					}
					break;
				case FLOAT:
				case DOUBLE:
					if(this instanceof IHasFormat) {
						final GlobalFormat format = ((IHasFormat) this).getFormat();
						if(format != null && format.isNumericFormat()) {
							addValidator(DecimalValidator.instance(format));
						}
					}
					break;
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
					throw new IllegalStateException("Unhandled property type: " + metadata.getPropertyType().name());
			}
		}
	}

	public final void removeValidator(IValidator validator) {
		if(validator != null && this.validator != null) {
			this.validator.remove(validator);
		}
	}

	public final void validate() throws ValidationException {
		validate(getValue());
	}

	public final Object validate(Object value) throws ValidationException {
		// check field requiredness
		if(isRequired()) {
			value = NotEmptyValidator.INSTANCE.validate(value);
		}

		// check max length
		if(this instanceof IHasMaxLength) {
			final int maxlen = ((IHasMaxLength) this).getMaxLen();
			if(maxlen != -1) {
				value = StringLengthValidator.validate(value, -1, maxlen);
			}
		}

		if(validator != null) {
			value = validator.validate(value);
		}

		return value;
	}

	private void markInvalid(boolean invalid) {
		if(invalid) {
			removeStyleName(Styles.DIRTY);
			addStyleName(Styles.INVALID);
		}
		else {
			removeStyleName(Styles.INVALID);
		}
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
			// ToStringConverter.INSTANCE.render(val);
			String sval = getText();
			sval = StringUtil.isEmpty(sval) ? dfltReadOnlyEmptyValue : sval;
			rof.setText(sval);
		}

		// set readonly/editable display
		if(rof != null) rof.setVisible(readOnly);
		formWidget.setVisible(!readOnly);

		// apply disabled property
		formWidget.getElement().setPropertyBoolean(Styles.DISABLED, !enabled);

		// resolve the containers
		final Widget fldContainer = container == null ? this : container;
		final Widget lblContainer = fldLbl == null ? null : labelContainer == null ? fldLbl : labelContainer;

		// apply enabled property to "containing" widget
		if(enabled) {
			fldContainer.removeStyleName(Styles.DISABLED);
			if(lblContainer != null) lblContainer.removeStyleName(Styles.DISABLED);
		}
		else {
			fldContainer.addStyleName(Styles.DISABLED);
			if(lblContainer != null) lblContainer.addStyleName(Styles.DISABLED);
		}

		if(!enabled || readOnly) {
			// remove all msgs, edit and validation styling
			//clearMsgs();
			removeStyleName(Styles.INVALID);
			removeStyleName(Styles.DIRTY);
		}
		else if(enabled && !readOnly) {
			// show/hide edit styling
			markDirty();
		}
	}

	//public final void addFocusHandler(FocusHandler handler) {
		// NOTE: we must be deterministic here as the editability may intermittently
		// change
		// if(!isReadOnly()) {
		//((HasFocusHandlers) getEditable()).addFocusHandler(handler);
		// }
	//}

	@Override
	public int getTabIndex() {
		return getEditable().getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		if(!readOnly) getEditable().setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		if(!readOnly) getEditable().setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		if(!readOnly) getEditable().setTabIndex(index);
	}

	public void onClick(ClickEvent event) {
		// toggle the display of any bound UI msgs for this field when the field
		// label is clicked
		//if(event.getSource() == fldLbl) toggleMsgs();
	}

	public void onChange(ChangeEvent event) {
		assert event.getSource() == getEditable();

		// dirty check
		markDirty();
	}

	public final Object getProperty(String propPath) throws PropertyPathException {
		if(!IBoundWidget.PROPERTY_VALUE.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		return getValue();
	}

	@SuppressWarnings("unchecked")
	public final void setProperty(String propPath, Object value) throws PropertyPathException, Exception {
		if(!IBoundWidget.PROPERTY_VALUE.equals(propPath)) {
			throw new MalformedPropPathException(propPath);
		}
		try {
			setValue((B) value);
		}
		catch(final RuntimeException e) {
			throw new Exception("Unable to set field " + this + " value", e);
		}
	}

	/**
	 * Sub-class impl for setting field value.
	 * @param value
	 */
	protected abstract void doSetValue(B value);

	public final void setValue(B value) {
		doSetValue(value);
		if(!initialValueSet) {
			initialValue = getValue();
			initialValueSet = true;
		}
	}

	/**
	 * Responsible for setting the field's value given the value in native type as
	 * well as triggering property change events if necessary.
	 * @param nativeValue
	 */
	protected abstract void setNativeValue(V nativeValue);

	public final void reset() {
		if(initialValueSet) {
			setNativeValue(initialValue);
			markInvalid(false);
			removeStyleName(Styles.DIRTY);
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		draw();
	}

	@Override
	protected void onUnload() {
		//clearMsgs();
		super.onUnload();
	}

	/**
	 * Fields are considered if their property names match.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		final AbstractField other = (AbstractField) obj;
		return (!property.equals(other.property));
	}

	@Override
	public final int hashCode() {
		return 31 + property.hashCode();
	}

	@Override
	public final String toString() {
		return name + " [ " + property + " ]";
	}
}