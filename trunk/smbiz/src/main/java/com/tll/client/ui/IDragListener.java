/**
 * The Logic Lab
 * @author jpk Dec 30, 2007
 */
package com.tll.client.ui;

import java.util.EventListener;


/**
 * IDragListener
 * @author jpk
 */
public interface IDragListener extends EventListener {

	/**
	 * Fired when a widget begins to be dragged.
	 * @param event The drag event
	 */
	void onDragStart(DragEvent event);

	/**
	 * Fired when a widget is dragged.
	 * @param event The drag event
	 */
	void onDragging(DragEvent event);

	/**
	 * Fired when dragging ceases. This trigger is necessary as there may be drag gestures that are
	 * too fast for the browser to process and here is where we get a change to "clean up" in
	 * particular finalizing the position of relevant widgets.
	 * @param event The drag event
	 */
	void onDragEnd(DragEvent event);
}
