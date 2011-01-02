/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.IDescriptorProvider;
import com.tll.IMarshalable;

/**
 * Way to request model data when issuing an RPC call.
 * @author jpk
 */
public final class ModelDataRequest implements IDescriptorProvider, IMarshalable {

	private Set<String> entityTypes;

	private Set<String> entityPrototypes;

	@Override
	public String descriptor() {
		return "Aux Data Request";
	}

	/**
	 * Request a listing of a particular entity type.
	 * @param entityType The entity type
	 */
	public void requestEntityList(String entityType) {
		if(entityTypes == null) {
			entityTypes = new HashSet<String>();
		}
		entityTypes.add(entityType);
	}

	public void requestEntityPrototype(String entityType) {
		if(entityPrototypes == null) {
			entityPrototypes = new HashSet<String>();
		}
		entityPrototypes.add(entityType);
	}

	public Iterator<String> getEntityRequests() {
		return entityTypes == null ? null : entityTypes.iterator();
	}

	public Iterator<String> getEntityPrototypeRequests() {
		return entityPrototypes == null ? null : entityPrototypes.iterator();
	}

	public int size() {
		return (entityTypes == null ? 0 : entityTypes.size()) + (entityPrototypes == null ? 0 : entityPrototypes.size());
	}
}
