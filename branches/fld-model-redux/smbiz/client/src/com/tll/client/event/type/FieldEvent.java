/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;

/**
 * FieldEvent
 * @author jpk
 */
public final class FieldEvent extends BaseEvent {

	public static enum FieldEventType {
		FIELDS_CREATED,
		BINDINGS_CREATED,
		BEFORE_BIND,
		AFTER_BIND,
		BEFORE_UNBIND,
		AFTER_UNBIND;
	}

	private final FieldEventType type;

	/**
	 * Constructor
	 */
	public FieldEvent(Widget source, FieldEventType type) {
		super(source);
		this.type = type;
	}

	public FieldEventType getType() {
		return type;
	}
}
