package com.tll.client.ui;

import com.google.gwt.user.client.ui.HasValue;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.common.bind.IBindable;

/**
 * IBindableWidget - Common base class for a UI widgets that are boundWidget.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 * @param <T> the value type
 */
public interface IBindableWidget<T> extends IBindable, HasValue<T> {

	/**
	 * Generic token indicating the name of the value property. Used when firing
	 * property change events.
	 */
	static final String PROPERTY_VALUE = "value";

	/**
	 * Generic token indicating DOM attachment. Used when firing property change
	 * events.
	 */
	// static final String PROPERTY_ATTACHED = "attached";
	
	/**
	 * Generic token indicating the name of the model property. Used when firing
	 * property change events.
	 */
	static final String PROPERTY_MODEL = "model";

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
}
