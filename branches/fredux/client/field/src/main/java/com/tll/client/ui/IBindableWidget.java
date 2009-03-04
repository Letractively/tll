package com.tll.client.ui;

import com.google.gwt.user.client.ui.HasValue;
import com.tll.client.bind.IBindingAction;
import com.tll.client.convert.IConverter;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.bind.IBindable;

/**
 * IBindableWidget - Extension of {@link IBindable} relevant to ui widgets
 * capable of participating in a binding.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 * @param <T> the value type
 */
public interface IBindableWidget<T> extends IBindable, HasValue<T> {
	
	/**
	 * Generic token indicating the name of the value property.
	 */
	static final String PROPERTY_VALUE = "value";

	/**
	 * PropertyChangeType - Defines the supported property change event types for
	 * bindable widget. <br>
	 * These types are relevant in a ui context.
	 * @author jpk
	 */
	public static enum PropertyChangeType {
		/**
		 * The value of the bindable widget has changed.
		 */
		VALUE("value"),
		/**
		 * The bindable widget is being attached.
		 */
		ATTACHED("attached"),
		/**
		 * the model property of the bindable widget has changed.
		 */
		MODEL("model");

		/**
		 * The property change token name.
		 */
		private final String property;

		/**
		 * Constructor
		 * @param property
		 */
		private PropertyChangeType(String property) {
			this.property = property;
		}

		/**
		 * @return The token used when firing property change events.
		 */
		public String prop() {
			return property;
		}
	}

	/**
	 * @return The employed message popup registry.
	 */
	MsgPopupRegistry getMsgPopupRegistry();

	/**
	 * Sets the message popup registry enabling popups to me managed as a
	 * flyweight.
	 * @param mregistry the message popup registry
	 */
	void setMsgPopupRegistry(MsgPopupRegistry mregistry);

	/**
	 * @return Optional converter used to type coerce un-typed inbound values.
	 */
	IConverter<T, Object> getConverter();

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
}
