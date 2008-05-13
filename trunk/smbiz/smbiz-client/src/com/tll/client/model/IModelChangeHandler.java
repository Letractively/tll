package com.tll.client.model;

import com.tll.client.event.ISourcesModelChangeEvents;
import com.tll.model.EntityType;

/**
 * IModelChangeHandler - Acts as a mediator and centralizes the handling of
 * model change events. Also handles model retrieval given a model ref.
 * @author jpk
 */
public interface IModelChangeHandler extends ISourcesModelChangeEvents {

	/**
	 * Handles the fetching of needed auxiliary data. A subsequent model change
	 * event is anticipated if aux data is found needed (<code>true</code> is
	 * returned).
	 * @return <code>true</code> only if aux data is actually needed and
	 *         <code>false</code> when no aux data is needed. I.e.: it is
	 *         already cached on the client.
	 */
	boolean handleAuxDataFetch();

	/**
	 * Handles the fetching of a prototypical model given an entity type. A
	 * subsequent model change event is anticipated. Used preliminarily for adding
	 * entities. A subsequent model change event is anticipated if aux data is
	 * found needed (<code>true</code> is returned).
	 * @param entityType The entity type of the model entity to fetch
	 * @return <code>true</code> if the model prototype must be fetched from the
	 *         server and <code>false</code> when the prototype is already
	 *         cached on the client.
	 */
	boolean handleModelPrototypeFetch(EntityType entityType);

	/**
	 * Handles the fetching of a model given a model ref. A subsequent model
	 * change event is anticipated.
	 * @param modelRef The reference of the model to fetch
	 */
	void handleModelLoad(RefKey modelRef);

	/**
	 * Handles model persisting (adding and updating) firing an appropriate model
	 * change event. A subsequent model change event is anticipated.
	 * @param model The model to persist
	 */
	void handleModelPersist(Model model);

	/**
	 * Commits a model delete firing a model change event to subscribed listeners
	 * upon a successful delete.
	 * @param modelRef The model to delete
	 */
	void handleModelDelete(RefKey modelRef);
}