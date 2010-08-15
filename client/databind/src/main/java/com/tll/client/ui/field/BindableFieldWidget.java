/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2010
 */
package com.tll.client.ui.field;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.BindableWidgetAdapter;
import com.tll.client.validate.IErrorHandler;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.common.bind.IPropertyChangeListener;
import com.tll.common.model.PropertyPathException;

/**
 * Adapts an {@link IFieldWidget} to a databinding aware field widget by
 * composition.
 * @param <V> field value type
 * @author jpk
 */
public final class BindableFieldWidget<V> implements IBindableFieldWidget<V> {

	private final IFieldWidget<V> fieldWidget;

	private final BindableWidgetAdapter<V> adapter;

	/**
	 * Constructor
	 * @param fieldWidget
	 */
	public BindableFieldWidget(IFieldWidget<V> fieldWidget) {
		super();
		if(fieldWidget == null) throw new NullPointerException();
		this.fieldWidget = fieldWidget;
		adapter = new BindableWidgetAdapter<V>(this);
	}

	@Override
	public IEditable<?> getEditable() {
		return fieldWidget.getEditable();
	}

	@Override
	public FieldLabel getFieldLabel() {
		return fieldWidget.getFieldLabel();
	}

	@Override
	public String getLabelText() {
		return fieldWidget.getLabelText();
	}

	@Override
	public void setFieldContainer(Widget fieldContainer) {
		fieldWidget.setFieldContainer(fieldContainer);
	}

	@Override
	public void setFieldLabelContainer(Widget fieldLabelContainer) {
		fieldWidget.setFieldLabelContainer(fieldLabelContainer);
	}

	@Override
	public void setLabelText(String text) {
		fieldWidget.setLabelText(text);
	}

	@Override
	public void setPropertyName(String propName) {
		fieldWidget.setPropertyName(propName);
	}

	@Override
	public void addValidator(IValidator validator) throws IllegalArgumentException {
		fieldWidget.addValidator(validator);
	}

	@Override
	public void clearValue() {
		fieldWidget.clearValue();
	}

	@Override
	public boolean isEnabled() {
		return fieldWidget.isEnabled();
	}

	@Override
	public boolean isReadOnly() {
		return fieldWidget.isReadOnly();
	}

	@Override
	public boolean isRequired() {
		return fieldWidget.isRequired();
	}

	@Override
	public boolean isVisible() {
		return fieldWidget.isVisible();
	}

	@Override
	public void removeValidator(Class<? extends IValidator> type) {
		fieldWidget.removeValidator(type);
	}

	@Override
	public void reset() {
		fieldWidget.reset();
	}

	@Override
	public void setEnabled(boolean enabled) {
		fieldWidget.setEnabled(enabled);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		fieldWidget.setReadOnly(readOnly);
	}

	@Override
	public void setRequired(boolean required) {
		fieldWidget.setRequired(required);
	}

	@Override
	public void setVisible(boolean visible) {
		fieldWidget.setVisible(visible);
	}

	@Override
	public void validate() throws ValidationException {
		fieldWidget.validate();
	}

	@Override
	public void validateIncrementally(boolean validate) {
		fieldWidget.validateIncrementally(validate);
	}

	@Override
	public String getName() {
		return fieldWidget.getName();
	}

	@Override
	public void setName(String name) {
		fieldWidget.setName(name);
	}

	@Override
	public String descriptor() {
		return fieldWidget.descriptor();
	}

	@Override
	public Widget getWidget() {
		return fieldWidget.getWidget();
	}

	@Override
	public IErrorHandler getErrorHandler() {
		return fieldWidget.getErrorHandler();
	}

	@Override
	public void setErrorHandler(IErrorHandler errorHandler) {
		fieldWidget.setErrorHandler(errorHandler);
	}

	@Override
	public String getPropertyName() {
		return fieldWidget.getPropertyName();
	}

	@Override
	public V getValue() {
		return fieldWidget.getValue();
	}

	@Override
	public void setValue(V value) {
		fieldWidget.setValue(value);
	}

	@Override
	public void setValue(V arg0, boolean arg1) {
		fieldWidget.setValue(arg0, arg1);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<V> arg0) {
		return addValueChangeHandler(arg0);
	}

	@Override
	public void fireEvent(GwtEvent<?> arg0) {
		fieldWidget.fireEvent(arg0);
	}

	@Override
	public String getText() {
		return fieldWidget.getText();
	}

	@Override
	public void setText(String arg0) {
		fieldWidget.setText(arg0);
	}

	@Override
	public String getHelpText() {
		return fieldWidget.getHelpText();
	}

	@Override
	public void setHelpText(String helpText) {
		fieldWidget.setHelpText(helpText);
	}

	@Override
	public Object validate(Object value) throws ValidationException {
		return fieldWidget.validate(value);
	}

	@Override
	public void onValueChange(ValueChangeEvent<V> arg0) {
		fieldWidget.onValueChange(arg0);
	}

	@Override
	public int getTabIndex() {
		return fieldWidget.getTabIndex();
	}

	@Override
	public void setAccessKey(char arg0) {
		fieldWidget.setAccessKey(arg0);
	}

	@Override
	public void setFocus(boolean arg0) {
		fieldWidget.setFocus(arg0);
	}

	@Override
	public void setTabIndex(int arg0) {
		fieldWidget.setTabIndex(arg0);
	}

	@Override
	public void onBlur(BlurEvent arg0) {
		fieldWidget.onBlur(arg0);
	}

	@Override
	public void onFocus(FocusEvent arg0) {
		fieldWidget.onFocus(arg0);
	}

	@Override
	public final Object getProperty(String propPath) throws PropertyPathException {
		return adapter.getProperty(propPath);
	}

	@Override
	public final void setProperty(String propPath, Object value) throws PropertyPathException, IllegalArgumentException {
		adapter.setProperty(propPath, value);
	}

	@Override
	public final void addPropertyChangeListener(IPropertyChangeListener listener) {
		adapter.addPropertyChangeListener(listener);
	}

	@Override
	public final void addPropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		adapter.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public final void removePropertyChangeListener(IPropertyChangeListener listener) {
		adapter.removePropertyChangeListener(listener);
	}

	@Override
	public final void removePropertyChangeListener(String propertyName, IPropertyChangeListener listener) {
		adapter.removePropertyChangeListener(propertyName, listener);
	}

	@Override
	public final IConverter<V, Object> getConverter() {
		return fieldWidget.getConverter();
	}

	@Override
	public final void setConverter(IConverter<V, Object> converter) {
		fieldWidget.setConverter(converter);
	}
}
