/**
 * The Logic Lab
 * @author jpk Nov 3, 2007
 */
package com.tll.client.data;

import java.util.HashSet;
import java.util.Set;

import com.tll.client.IMarshalable;

/**
 * EntityOptions - Provision for specifying persistence options (eager loading
 * of related entities etc.)
 * @author jpk
 */
public final class EntityOptions implements IMarshalable {

	/**
	 * Collection of entity types as {@link String}s
	 */
	private final Set<String> relatedRequests = new HashSet<String>();

	/**
	 * Collection of entity types as {@link String}s
	 */
	private final Set<String> relatedRefRequests = new HashSet<String>();

	/**
	 * Instructs the server to provide the related entity type ref for a CRUD load
	 * op.
	 * @param entityType
	 */
	public void requestRelatedRef(String entityType) {
		relatedRefRequests.add(entityType);
	}

	/**
	 * Invoked server-side to determine whether or not the related entity type
	 * should be provided for a CRUD load op.
	 * @param entityType
	 */
	public boolean isRelatedRefRequested(String entityType) {
		return relatedRefRequests.contains(entityType);
	}

	/**
	 * Instructs the server to handle the related entity type for CRUD ops.
	 * @param entityType
	 */
	public void requestRelated(String entityType) {
		relatedRequests.add(entityType);
	}

	/**
	 * Invoked server-side to determine whether or not the related entity type
	 * should be handled for CRUD ops.
	 * @param entityType
	 */
	public boolean isRelatedRequested(String entityType) {
		return relatedRequests.contains(entityType);
	}

	public String descriptor() {
		return "Entity CRUD options";
	}

}
