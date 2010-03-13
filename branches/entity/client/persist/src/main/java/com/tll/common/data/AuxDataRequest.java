/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.common.model.IEntityType;

/**
 * AuxDataRequest - Way to request "auxiliary" data when issuing an RPC call.
 * @author jpk
 */
public final class AuxDataRequest implements IModelRelatedRequest {

	private Set<IEntityType> entityTypes;

	private Set<IEntityType> entityPrototypes;

	@Override
	public String descriptor() {
		return "Aux Data Request";
	}

	/**
	 * Request a listing of a particular entity type.
	 * @param entityType The entity type
	 */
	public void requestEntityList(IEntityType entityType) {
		if(entityTypes == null) {
			entityTypes = new HashSet<IEntityType>();
		}
		entityTypes.add(entityType);
	}

	public void requestEntityPrototype(IEntityType entityType) {
		if(entityPrototypes == null) {
			entityPrototypes = new HashSet<IEntityType>();
		}
		entityPrototypes.add(entityType);
	}

	public Iterator<IEntityType> getEntityRequests() {
		return entityTypes == null ? null : entityTypes.iterator();
	}

	public Iterator<IEntityType> getEntityPrototypeRequests() {
		return entityPrototypes == null ? null : entityPrototypes.iterator();
	}

	public int size() {
		return (entityTypes == null ? 0 : entityTypes.size()) + (entityPrototypes == null ? 0 : entityPrototypes.size());
	}
}
