/**
 * The Logic Lab
 * @author jpk
 * Mar 23, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.model.RefKey;

/**
 * RowOptionEvent - Option events used for identifying table rows.
 * @author jpk
 */
public class RowOptionEvent extends OptionEvent {

	/**
	 * The index of the row that sourced the event.
	 */
	public final int rowIndex;

	/**
	 * The ref key for the target table row.
	 */
	public final RefKey rowRef;

	/**
	 * Constructor
	 * @param source
	 * @param optionText
	 * @param rowIndex
	 * @param rowRef
	 */
	public RowOptionEvent(Widget source, String optionText, int rowIndex, RefKey rowRef) {
		super(source, optionText);
		this.rowIndex = rowIndex;
		this.rowRef = rowRef;
	}

}
