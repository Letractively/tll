package com.tll.common.bind;

import java.util.EventListener;

/**
 * EventListenerProxy
 * @author jpk
 */
public abstract class EventListenerProxy implements EventListener {

	private final EventListener listener;

	public EventListenerProxy(final EventListener listener) {
		this.listener = listener;
	}

	public EventListener getListener() {
		return listener;
	}
}
