/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.mvc.view;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.common.model.Model;

/**
 * AbstractModelAwareView - Base view class for views that are {@link Model}
 * aware.
 * @author jpk
 * @param <I> the view initializer type
 */
public abstract class AbstractModelAwareView<I extends IViewInitializer> extends AbstractView<I> implements IModelAwareView<I> {

	/**
	 * Handles model change errors. Sub-classes should override as necessary.
	 * @param event The {@link ModelChangeEvent}
	 */
	protected void handleModelChangeError(ModelChangeEvent event) {
		// base impl no-op
	}

	/**
	 * Handles successful model changes. Sub-classes should override as necessary.
	 * @param event The {@link ModelChangeEvent}
	 */
	protected void handleModelChangeSuccess(ModelChangeEvent event) {
		// base impl no-op
	}

	/**
	 * Provides the child widgets that are model aware. These handlers are
	 * notified of model change events.
	 * @return The model change handlers in this view that should recieve model change events.
	 */
	protected IModelChangeHandler[] getModelChangeHandlers() {
		return null; // default
	}

	@Override
	public final void onModelChangeEvent(ModelChangeEvent event) {
		Log.debug("View ( " + toString() + " ) is handling model change event: " + event.toString() + "..");

		// global handling
		if(event.getStatus() != null && event.getStatus().hasErrors()) {
			// has errors
			handleModelChangeError(event);
		}
		else {
			// no errors
			handleModelChangeSuccess(event);
		}

		// dispatch to any child widget that handle model change events too
		final IModelChangeHandler[] handlers = getModelChangeHandlers();
		if(handlers != null) {
			for(final IModelChangeHandler handler : handlers) {
				handler.onModelChangeEvent(event);
			}
		}
	}
}
