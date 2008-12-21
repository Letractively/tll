/**
 * The Logic Lab
 * @author jpk
 * Mar 23, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.ISourcesOptionEvents;
import com.tll.client.ui.Option;

/**
 * OptionEvent - Event type for {@link ISourcesOptionEvents}
 * @author jpk
 */
public class OptionEvent extends BaseEvent {

	/**
	 * The text of the {@link Option} that was clicked.
	 */
	public final String optionText;

	/**
	 * Constructor
	 * @param source
	 * @param optionText The Option text
	 */
	public OptionEvent(Widget source, String optionText) {
		super(source);
		this.optionText = optionText;
	}

}
