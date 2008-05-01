/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.client.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.client.IMarshalable;
import com.tll.model.EntityType;

/**
 * AuxDataRequest - Way to request "auxiliary" data when issuing an RPC call.
 * @author jpk
 */
public class AuxDataRequest implements IMarshalable {

	public enum AuxDataType {
		REFDATA,
		ENTITY;
	}

	private Set<String> refData;

	private Set<EntityType> entityTypes;

	public String descriptor() {
		return "Auxiliary Data Request";
	}

	/**
	 * Request app ref data in the form of a string/string map.
	 * @param terseName
	 */
	public void requestAppRefData(String terseName) {
		if(refData == null) {
			refData = new HashSet<String>();
		}
		refData.add(terseName);
	}

	/**
	 * Request a listing of a particular entity type.
	 * @param entityType The entity type
	 */
	public void requestEntityList(EntityType entityType) {
		if(entityTypes == null) {
			entityTypes = new HashSet<EntityType>();
		}
		entityTypes.add(entityType);
	}

	public Iterator<String> getRefDataRequests() {
		return refData == null ? null : refData.iterator();
	}

	public Iterator<EntityType> getEntityRequests() {
		return entityTypes == null ? null : entityTypes.iterator();
	}

	public int size() {
		return (refData == null ? 0 : refData.size()) + (entityTypes == null ? 0 : entityTypes.size());
	}
}
