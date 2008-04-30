/**
 * The Logic Lab
 * @author jpk Dec 6, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.RowOptionEvent;

/**
 * IRowOptionListener - Notifies when a table row context option is selected.
 * @author jpk
 */
public interface IRowOptionListener extends EventListener {

	/**
	 * Fired when an option is selected for a particluar table listing row.
	 * @param event The RowOptionEvent
	 */
	void onRowOptionSelected(RowOptionEvent event);
}
