/**
 * The Logic Lab
 * @author jpk Dec 30, 2007
 */
package com.tll.client.ui;

import java.util.ArrayList;


/**
 * ISourcesDragEvents - A widget that implements this interface sources the events defined by the
 * {@link IDragListener} interface.
 * @author jpk
 */
public interface ISourcesDragEvents {

	/**
	 * Adds a listener interface to receive drag events.
	 * @param listener the listener interface to add
	 */
	void addDragListener(IDragListener listener);

	/**
	 * Removes a previously added listener interface.
	 * @param listener the listener interface to remove
	 */
	void removeDragListener(IDragListener listener);

	/**
	 * DragListenerCollection
	 * @author jpk
	 */
	public static final class DragListenerCollection extends ArrayList<IDragListener> {

		/**
		 * Fires a drag start event to all listeners.
		 * @param event The event
		 */
		public void fireDragStart(DragEvent event) {
			for(IDragListener listener : this) {
				listener.onDragStart(event);
			}
		}

		/**
		 * Fires a dragging event to all listeners.
		 * @param event The event
		 */
		public void fireDragging(DragEvent event) {
			for(IDragListener listener : this) {
				listener.onDragging(event);
			}
		}

		/**
		 * Fires a drag end event to all listeners.
		 * @param event The drag event
		 */
		public void fireDragEnd(DragEvent event) {
			for(IDragListener listener : this) {
				listener.onDragEnd(event);
			}
		}
	}
}
