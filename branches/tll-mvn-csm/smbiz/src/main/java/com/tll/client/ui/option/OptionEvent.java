/**
 * The Logic Lab
 * @author jpk
 * Mar 23, 2008
 */
package com.tll.client.ui.option;

import java.util.EventObject;

import com.google.gwt.user.client.ui.Widget;

/**
 * OptionEvent - Event type for {@link ISourcesOptionEvents}
 * @author jpk
 */
public class OptionEvent extends EventObject {

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
