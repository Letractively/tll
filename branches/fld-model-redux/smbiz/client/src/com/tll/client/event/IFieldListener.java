/**
 * The Logic Lab
 * @author jpk
 * Jul 4, 2008
 */
package com.tll.client.event;

import com.tll.client.event.type.FieldEvent;

/**
 * IFieldListener
 * @author jpk
 */
public interface IFieldListener {

	/**
	 * Fired when a field related event occurrs.
	 * @param event The event
	 */
	void onFieldEvent(FieldEvent event);
}
