/**
 * The Logic Lab
 * @author jpk
 * Dec 15, 2007
 */
package com.tll.client.ui;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasFocus;


/**
 * FocusCommand
 * @author jpk
 */
public class FocusCommand implements Command {
	protected final HasFocus w;
	protected final boolean focus;
	
	/**
	 * Constructor
	 * @param w
	 */
	public FocusCommand(HasFocus w, boolean focus) {
		super();
		this.w = w;
		this.focus = focus;
	}

	public void execute() {
		w.setFocus(focus);
	}

}
