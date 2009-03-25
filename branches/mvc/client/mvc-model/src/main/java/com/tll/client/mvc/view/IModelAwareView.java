/**
 * The Logic Lab
 * @author jpk
 * @since Mar 23, 2009
 */
package com.tll.client.mvc.view;

import com.tll.client.model.IModelChangeHandler;

/**
 * IModelAwareView - A view that can handle model change events.
 * @author jpk
 * @param <R>
 */
public interface IModelAwareView<R extends IViewKeyProvider> extends IView<R>, IModelChangeHandler {

}
