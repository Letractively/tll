/**
 * The Logic Lab
 * @author jpk Sep 3, 2007
 */
package com.tll.client.mvc.view;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.ViewManager;

/**
 * AbstractModelAwareView - Base view class for all defined views in the app.
 * @author jpk
 * @param <I> the view initializer type
 */
public abstract class AbstractModelAwareView<I extends IViewInitializer> extends AbstractView<I> implements
		IModelAwareView<I> {

	/**
	 * Constructor
	 */
	public AbstractModelAwareView() {
	}

	/**
	 * Must be implemented by concrete views.
	 * @param event
	 * @return true/false
	 */
	protected abstract boolean shouldHandleModelChangeEvent(ModelChangeEvent event);

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

	public final void onModelChangeEvent(ModelChangeEvent event) {
		if(shouldHandleModelChangeEvent(event)) {
			Log.debug("View ( " + toString() + " ) is handling model change event: " + event.toString() + "..");
			if(event.getStatus().hasErrors()) {
				// has errors
				handleModelChangeError(event);
			}
			else {
				// no errors
				handleModelChangeSuccess(event);
			}
		}
		// now dispatch to the other cached views
		final IView<?>[] cachedViews = ViewManager.get().getCachedViews();
		for(final IView<?> cv : cachedViews) {
			if(cv != this) cv.getViewWidget().fireEvent(event);
		}
	}
}
