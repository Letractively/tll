package com.tll.client.model;

import com.tll.client.event.ISourcesModelChangeEvents;
import com.tll.client.event.type.ModelChangeEvent.ModelChangeOp;

/**
 * IModelChangeHandler - Acts as a mediator and centralizes the handling of
 * model change events. Also handles model retrieval given a model ref.
 * @author jpk
 */
public interface IModelChangeHandler extends ISourcesModelChangeEvents {

	/**
	 * Handles the fetching of a model given a model ref.
	 * @param modelRef The reference of the model to fetch
	 */
	void handleModelFetch(RefKey modelRef);

	/**
	 * Handles the fetching of needed auxiliary data
	 * @return <code>true</code> only if aux data is needed and
	 *         <code>false</code> when no aux data is needed of all needed aux
	 *         datat is already cached on the client.
	 */
	boolean handleAuxDataFetch();

	/**
	 * Handles a model change cancellation firing a model change event only to
	 * indicate cancellation.
	 * @param canceledOp
	 * @param model The unchanged model.
	 */
	void handleModelChangeCancellation(ModelChangeOp canceledOp, Model model);

	/**
	 * Handles model persisting (adding and updating) firing an appropriate model
	 * change event.
	 * @param model The model to add
	 */
	void handleModelPersist(Model model);

	/**
	 * Commits a model delete firing a model change event to subscribed listeners
	 * upon a successful delete.
	 * @param modelRef The model to delete
	 */
	void handleModelDelete(RefKey modelRef);
}