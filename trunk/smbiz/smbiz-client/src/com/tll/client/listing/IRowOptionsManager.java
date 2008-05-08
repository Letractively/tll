/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.event.type.OptionEvent;
import com.tll.client.model.RefKey;
import com.tll.client.ui.Option;

/**
 * IRowOptionsManager - Indicates the ability to provide {@link Option}s for a
 * particular table row and handle row option selections.
 * @author jpk
 */
public interface IRowOptionsManager {

	/**
	 * @return <code>true</code> if the options are applicable to all rows. This
	 *         is an optimization to avoid having to fill/re-fill options when
	 *         they are always the same (static).
	 */
	boolean isStaticOptions();

	/**
	 * Provides {@link Option}s for use by a row specific popup Panel.
	 * @param rowRef The RefKey of the row for which {@link Option}s are sought
	 * @return Array of {@link Option}s.
	 */
	Option[] getOptions(RefKey rowRef);

	/**
	 * Handles option events for a single row.
	 * @param event The option event
	 * @param rowRef The ref to the row at which the option event occurred.
	 */
	void handleOptionEvent(OptionEvent event, RefKey rowRef);
}
