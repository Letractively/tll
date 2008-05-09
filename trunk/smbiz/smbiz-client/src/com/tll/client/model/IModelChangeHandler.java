package com.tll.client.model;

import com.tll.client.event.ISourcesModelChangeEvents;
import com.tll.client.event.type.ModelChangeEvent.ModelChangeOp;

/**
 * IModelChangeHandler - Acts as a mediator and centralizes the handling of
 * model change events.
 * @author jpk
 */
public interface IModelChangeHandler extends ISourcesModelChangeEvents {

	/**
	 * Handles a model change cancellation.
	 * @param canceledOp
	 * @param model The unchanged model.
	 */
	void handleModelChangeCancellation(ModelChangeOp canceledOp, Model model);

	/**
	 * Commits a model addition firing a model change event to subscribed
	 * listeners upon a successful commit.
	 * @param model The model to add
	 */
	void handleModelAdd(Model model);

	/**
	 * Commits a model update firing a model change event to subscribed listeners
	 * upon a successful commit.
	 * @param model The model to update
	 */
	void handleModelUpdate(Model model);

	/**
	 * Commits a model delete firing a model change event to subscribed listeners
	 * upon a successful delete.
	 * @param modelRef The model to delete
	 */
	void handleModelDelete(RefKey modelRef);
}