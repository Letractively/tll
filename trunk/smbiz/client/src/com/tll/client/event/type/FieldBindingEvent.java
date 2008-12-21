/**
 * The Logic Lab
 * @author jpk
 * Dec 18, 2008
 */
package com.tll.client.event.type;

import com.tll.client.field.IFieldGroupModelBinding;

/**
 * FieldBindingEvent
 * @author jpk
 */
public final class FieldBindingEvent extends BaseEvent {

	public enum FieldBindingEventType {
		BEFORE_BIND,
		AFTER_BIND;
	}

	private final IFieldGroupModelBinding binding;

	private final FieldBindingEventType type;

	/**
	 * Constructor
	 * @param source
	 * @param binding
	 * @param type
	 */
	public FieldBindingEvent(Object source, IFieldGroupModelBinding binding, FieldBindingEventType type) {
		super(source);
		this.binding = binding;
		this.type = type;
	}

	/**
	 * @return the binding
	 */
	public IFieldGroupModelBinding getBinding() {
		return binding;
	}

	/**
	 * @return the type
	 */
	public FieldBindingEventType getType() {
		return type;
	}

}
