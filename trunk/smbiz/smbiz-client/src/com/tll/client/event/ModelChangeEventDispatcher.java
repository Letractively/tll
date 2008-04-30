/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.client.event;

import com.tll.client.event.type.ModelChangeEvent;

/**
 * ModelChangeEventDispatcher - The sole Object responsible for dispatching
 * model change events within the client application.
 * @author jpk
 */
public class ModelChangeEventDispatcher implements ISourcesModelChangeEvents {

	private static ModelChangeEventDispatcher instance;

	public static ModelChangeEventDispatcher instance() {
		if(instance == null) {
			instance = new ModelChangeEventDispatcher();
		}
		return instance;
	}

	private final ModelChangeListenerCollection listeners = new ModelChangeListenerCollection();

	/**
	 * Constructor
	 */
	private ModelChangeEventDispatcher() {
		super();
	}

	public void addModelChangeListener(IModelChangeListener listener) {
		listeners.add(listener);
	}

	public void removeModelChangeListener(IModelChangeListener listener) {
		listeners.remove(listener);
	}

	public void fireOnModelChange(ModelChangeEvent event) {
		listeners.fireOnModelChange(event);
	}
}
