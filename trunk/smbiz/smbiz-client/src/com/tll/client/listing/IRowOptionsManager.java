/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.model.RefKey;
import com.tll.client.ui.Option;

/**
 * IRowOptionsManager - Indicates the ability to provide {@link Option}s for a
 * particular table row and handle row option selections.
 * @author jpk
 */
public interface IRowOptionsManager {

	/**
	 * Provides {@link Option}s for use by a row specific popup Panel.
	 * @param rowIndex The row index of the targeted row
	 * @param rowRef The RefKey of the row for which {@link Option}s are sought
	 * @return Array of {@link Option}s.
	 */
	Option[] getOptions(int rowIndex, RefKey rowRef);

	/**
	 * Handles option events for a single row.
	 * @param optionText The text of the selected option
	 * @param rowIndex The row index of associated with the selected option
	 * @param rowRef The ref of the row at which the option selection event
	 *        occurred
	 */
	void handleOptionSelection(String optionText, int rowIndex, RefKey rowRef);
}
