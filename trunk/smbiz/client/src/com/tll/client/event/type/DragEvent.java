/**
 * The Logic Lab
 * @author jpk Jan 13, 2008
 */
package com.tll.client.event.type;

import com.google.gwt.user.client.ui.Widget;

/**
 * DragEvent - Event object containing information about UI artifact dragging.
 * @author jpk
 */
public class DragEvent extends BaseEvent {

	public final int deltaX, deltaY;

	/**
	 * Constructor
	 * @param source
	 */
	public DragEvent(Widget source) {
		super(source);
		this.deltaX = -1;
		this.deltaY = -1;
	}

	/**
	 * Constructor
	 * @param source
	 * @param deltaX
	 * @param deltaY
	 */
	public DragEvent(Widget source, int deltaX, int deltaY) {
		super(source);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
}
