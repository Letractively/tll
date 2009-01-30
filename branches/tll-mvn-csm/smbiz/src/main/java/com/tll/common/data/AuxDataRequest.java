/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.IMarshalable;
import com.tll.model.EntityType;
import com.tll.refdata.RefDataType;

/**
 * AuxDataRequest - Way to request "auxiliary" data when issuing an RPC call.
 * @author jpk
 */
public class AuxDataRequest implements IMarshalable {

	public enum AuxDataType {
		REFDATA,
		ENTITY,
		ENTITY_PROTOTYPE;
	}

	private Set<RefDataType> refData;

	private Set<EntityType> entityTypes;

	private Set<EntityType> entityPrototypes;

	/**
	 * Request app ref data in the form of a string/string map.
	 * @param refDataType
	 */
	public void requestAppRefData(RefDataType refDataType) {
		if(refData == null) {
			refData = new HashSet<RefDataType>();
		}
		refData.add(refDataType);
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

	public void requestEntityPrototype(EntityType entityType) {
		if(entityPrototypes == null) {
			entityPrototypes = new HashSet<EntityType>();
		}
		entityPrototypes.add(entityType);
	}

	public Iterator<RefDataType> getRefDataRequests() {
		return refData == null ? null : refData.iterator();
	}

	public Iterator<EntityType> getEntityRequests() {
		return entityTypes == null ? null : entityTypes.iterator();
	}

	public Iterator<EntityType> getEntityPrototypeRequests() {
		return entityPrototypes == null ? null : entityPrototypes.iterator();
	}

	public int size() {
		return (refData == null ? 0 : refData.size()) + (entityTypes == null ? 0 : entityTypes.size())
				+ (entityPrototypes == null ? 0 : entityPrototypes.size());
	}
}
