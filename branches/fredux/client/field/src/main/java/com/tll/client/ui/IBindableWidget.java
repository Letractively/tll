package com.tll.client.ui;

import com.google.gwt.user.client.ui.HasValue;
import com.tll.client.bind.IBindingAction;
import com.tll.client.convert.IConverter;
import com.tll.client.validate.IValidationFeedback;
import com.tll.common.bind.IBindable;

/**
 * IBindableWidget - Extension of {@link IBindable} relevant to ui widgets
 * capable of participating in a binding.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 * @param <V> the value type
 */
public interface IBindableWidget<V> extends IBindable, HasValue<V> {
	
	/**
	 * Generic token indicating the name of the value property.
	 */
	static final String PROPERTY_VALUE = "value";

	/**
	 * @return Optional converter used to type coerce un-typed inbound values.
	 */
	IConverter<V, Object> getConverter();

	/**
	 * @return The bound model
	 */
	IBindable getModel();

	/**
	 * Sets the model to be bound to this bindable widget.
	 * @param model the bindable model
	 */
	void setModel(IBindable model);

	/**
	 * @return The action for this bindable widget.
	 */
	@SuppressWarnings("unchecked")
	IBindingAction getAction();

	/**
	 * Sets the action.
	 * @param action
	 */
	@SuppressWarnings("unchecked")
	void setAction(IBindingAction action);
	
	/**
	 * @return The validation feedback handler.
	 */
	IValidationFeedback getValidationHandler();

	/**
	 * Sets the validation feedback handler.
	 * @param validationHandler
	 */
	void setValidationHandler(IValidationFeedback validationHandler);
}
