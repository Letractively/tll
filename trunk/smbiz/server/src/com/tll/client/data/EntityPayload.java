/**
 * The Logic Lab
 * @author jpk
 * Sep 14, 2007
 */
package com.tll.client.data;

import java.util.HashMap;
import java.util.Map;

import com.tll.client.model.Model;
import com.tll.client.model.RefKey;

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

	private RefKey entityRef;

	/**
	 * Map of related one entity refs keyed by property name.
	 */
	private Map<String, RefKey> relatedOneRefs;

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

	public RefKey getEntityRef() {
		return entityRef;
	}

	public void setEntityRef(RefKey entityRef) {
		this.entityRef = entityRef;
	}

	public void setRelatedOneRef(String propName, RefKey ref) {
		if(relatedOneRefs == null) {
			relatedOneRefs = new HashMap<String, RefKey>();
		}
		relatedOneRefs.put(propName, ref);
	}

	public RefKey getRelatedOneRef(String propName) {
		return relatedOneRefs == null ? null : (RefKey) relatedOneRefs.get(propName);
	}

}
