/**
 * 
 */
package com.tll.client.listing;

import com.tll.client.model.RefKey;
import com.tll.client.ui.Option;

/**
 * IRowOptionsProvider - Indicates the ability to provide {@link Option}s for a
 * particular table row.
 * @author jpk
 */
public interface IRowOptionsProvider {

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
}
