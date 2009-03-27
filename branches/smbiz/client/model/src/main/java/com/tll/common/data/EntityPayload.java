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
 * EntityPayload - To transport a single entity instance and any supporting data
 * to the client.
 * @author jpk
 */
public final class EntityPayload extends AuxDataPayload {

	/**
	 * The entity model.
	 */
	private Model entity;

	private ModelKey entityRef;

	/**
	 * Map of related one entity refs keyed by property name.
	 */
	private Map<String, ModelKey> relatedOneRefs;

	/**
	 * Constructor
	 */
	public EntityPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 */
	public EntityPayload(Status status) {
		super(status);
	}

	/**
	 * Constructor
	 * @param status
	 * @param entity
	 */
	public EntityPayload(Status status, Model entity) {
		super(status);
		this.entity = entity;
	}

	public Model getEntity() {
		return entity;
	}

	public void setEntity(Model entityGroup) {
		this.entity = entityGroup;
	}

	public ModelKey getEntityRef() {
		return entityRef;
	}

	public void setEntityRef(ModelKey entityRef) {
		this.entityRef = entityRef;
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
