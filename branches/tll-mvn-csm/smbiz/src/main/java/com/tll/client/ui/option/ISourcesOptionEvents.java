/**
 * The Logic Lab
 * @author jpk Jan 27, 2008
 */
package com.tll.client.ui.option;

import java.util.ArrayList;


/**
 * ISourcesOptionEvents
 * @author jpk
 */
public interface ISourcesOptionEvents {

	/**
	 * Adds a listener interface to receive option related events.
	 * @param listener the listener interface to add
	 */
	void addOptionListener(IOptionListener listener);

	/**
	 * Removes a previously added listener interface.
	 * @param listener the listener interface to remove
	 */
	void removeOptionListener(IOptionListener listener);

	/**
	 * OptionListenerCollection
	 * @author jpk
	 */
	@SuppressWarnings("serial")
	public static final class OptionListenerCollection extends ArrayList<IOptionListener> {

		public void fireOnSelected(OptionEvent event) {
			for(IOptionListener listener : this) {
				listener.onOptionSelected(event);
			}
		}

		public void fireOnCurrentChanged(OptionEvent event) {
			for(IOptionListener listener : this) {
				listener.onCurrentOptionChanged(event);
			}
		}
	}
}
