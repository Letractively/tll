/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.data.rpc;


/**
 * StatusEventDispatcher - Dispatches {@link StatusEvent}s to its subscribed
 * {@link IStatusListener}s.
 * @author jpk
 */
public final class StatusEventDispatcher implements ISourcesStatusEvents {

	private static StatusEventDispatcher instance;

	public static StatusEventDispatcher instance() {
		if(instance == null) {
			instance = new StatusEventDispatcher();
		}
		return instance;
	}

	private final StatusListenerCollection listeners = new StatusListenerCollection();

	/**
	 * Constructor
	 */
	private StatusEventDispatcher() {
		super();
	}

	public void addStatusListener(IStatusListener listener) {
		listeners.add(listener);
	}

	public void removeStatusListener(IStatusListener listener) {
		listeners.remove(listener);
	}

	public void fireStatusEvent(StatusEvent event) {
		listeners.fireStatusEvent(event);
	}
}
