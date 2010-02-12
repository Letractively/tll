/**
 * The Logic Lab
 * @author jpk
 * @since Feb 11, 2010
 */
package com.tll.client.mvc;

import java.util.Iterator;

import com.tll.client.model.IModelChangeHandler;
import com.tll.client.model.ModelChangeEvent;
import com.tll.client.mvc.view.IModelAwareView;
import com.tll.client.mvc.view.IView;


/**
 * ModelChangeViewHandler
 * @author jpk
 */
public class ModelChangeViewHandler implements IModelChangeHandler {
	
	private static final ModelChangeViewHandler instance = new ModelChangeViewHandler();
	
	public static ModelChangeViewHandler get() {
		return instance;
	}

	private ModelChangeViewHandler() {
	}

	@Override
	public void onModelChangeEvent(ModelChangeEvent event) {
		// apply the model change to the currently cached views
		for(final Iterator<CView> itr = ViewManager.get().cache.queueIterator(); itr.hasNext();) {
			final IView<?> view = itr.next().vc.getView();
			if(view instanceof IModelAwareView<?>) {
				((IModelAwareView<?>) view).onModelChangeEvent(event);
			}
		}
	}
}
