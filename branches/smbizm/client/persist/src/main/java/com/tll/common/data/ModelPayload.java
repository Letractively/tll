/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.common.data;

import java.util.HashMap;
import java.util.Map;

import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;

/**
 * ModelPayload - To transport model and any supporting aux data to the client.
 * @author jpk
 */
public final class ModelPayload extends AuxDataPayload {

	/**
	 * The model.
	 */
	private Model model;

	private ModelKey ref;

	/**
	 * Map of related one entity refs keyed by property name.
	 */
	private Map<String, ModelKey> relatedOneRefs;

	/**
	 * Constructor
	 */
	public ModelPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 */
	public ModelPayload(Status status) {
		super(status);
	}

	/**
	 * Constructor
	 * @param status
	 * @param entity
	 */
	public ModelPayload(Status status, Model entity) {
		super(status);
		this.model = entity;
	}

	public Model getEntity() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public ModelKey getRef() {
		return ref;
	}

	public void setRef(ModelKey ref) {
		this.ref = ref;
	}

	public void setRelatedOneRef(String propName, ModelKey ref) {
		if(relatedOneRefs == null) {
			relatedOneRefs = new HashMap<String, ModelKey>();
		}
		relatedOneRefs.put(propName, ref);
	}

	public ModelKey getRelatedOneRef(String propName) {
		return relatedOneRefs == null ? null : (ModelKey) relatedOneRefs.get(propName);
	}

}
