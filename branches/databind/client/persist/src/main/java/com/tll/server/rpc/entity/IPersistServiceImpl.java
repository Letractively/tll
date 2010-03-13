package com.tll.server.rpc.entity;

import com.tll.common.data.ModelPayload;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.search.ISearch;

/**
 * IPersistServiceImpl - Server side model data crud support not required to be
 * aligned with server-side entity boundaries. Rather, this is a determination
 * made by {@link IPersistServiceImplResolver}.
 * @author jpk
 */
public interface IPersistServiceImpl {

	/**
	 * Implementation specific load routine. A {@link Model} instance is expected
	 * to be put into the given payload.
	 * @param search the search criteria
	 * @param payload the payload that is filled
	 */
	void load(ISearch search, ModelPayload payload);

	/**
	 * Implementation specific persist routine. A {@link Model} instance
	 * reflecting the newly persisted data is expected to be put into the given
	 * payload.
	 * @param model the model data to persist
	 * @param payload the payload that is filled
	 */
	void persist(Model model, ModelPayload payload);

	/**
	 * Implementation specific purge routine given a model instance.
	 * @param model the model data to purge
	 * @param payload the payload that is filled
	 */
	void purge(Model model, ModelPayload payload);

	/**
	 * Implementation specific purge routine given a model key.
	 * @param ref the model ref indicating what to purge
	 * @param payload the payload that is filled
	 */
	void purge(ModelKey ref, ModelPayload payload);
}