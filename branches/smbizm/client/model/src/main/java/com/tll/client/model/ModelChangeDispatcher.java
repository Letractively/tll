/**
 * The Logic Lab
 * @author jpk
 * @since Apr 25, 2009
 */
package com.tll.client.model;

import java.util.ArrayList;

import com.google.gwt.event.shared.GwtEvent;

/**
 * ModelChangeDispatcher - Centralizes the handling of model change events on
 * the client side.
 * @author jpk
 */
public class ModelChangeDispatcher implements IHasModelChangeHandlers {

	/**
	 * ModelChangeCollection
	 * @author jpk
	 */
	@SuppressWarnings("serial")
	static final class ModelChangeCollection extends ArrayList<IModelChangeHandler> {

		void fire(ModelChangeEvent event) {
			for(final IModelChangeHandler h : this) {
				h.onModelChangeEvent(event);
			}
		}
	}

	private static ModelChangeDispatcher instance;

	public static ModelChangeDispatcher get() {
		if(instance == null) {
			instance = new ModelChangeDispatcher();
		}
		return instance;
	}

	/**
	 * Constructor
	 */
	private ModelChangeDispatcher() {
		super();
	}

	final ModelChangeCollection modelHandlers = new ModelChangeCollection();

	@Override
	public void addModelChangeHandler(IModelChangeHandler handler) {
		modelHandlers.add(handler);
	}

	@Override
	public void removeModelChangeHandler(IModelChangeHandler handler) {
		modelHandlers.remove(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		if(event.getAssociatedType() == ModelChangeEvent.TYPE) {
			modelHandlers.fire((ModelChangeEvent) event);
		}
	}

}
