/**
 * The Logic Lab
 * @author jpk Dec 6, 2007
 */
package com.tll.client.event;

import java.util.EventListener;

import com.tll.client.event.type.OptionEvent;
import com.tll.client.ui.OptionsPanel;

/**
 * IOptionListener - Notifies when an option is selected in an {@link OptionsPanel}.
 * @author jpk
 */
public interface IOptionListener extends EventListener {

	/**
	 * Fired when an option is selected.
	 * @param event The OptionEvent
	 */
	void onOptionSelected(OptionEvent event);

	/**
	 * Fired when the current option changes.
	 * @param event The OptionEvent
	 */
	void onCurrentOptionChanged(OptionEvent event);
}
