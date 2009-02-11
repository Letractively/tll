/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.common.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.IMarshalable;
import com.tll.common.model.IEntityType;
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

	private Set<IEntityType> entityTypes;

	private Set<IEntityType> entityPrototypes;

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

	public Iterator<RefDataType> getRefDataRequests() {
		return refData == null ? null : refData.iterator();
	}

	public Iterator<IEntityType> getEntityRequests() {
		return entityTypes == null ? null : entityTypes.iterator();
	}

	public Iterator<IEntityType> getEntityPrototypeRequests() {
		return entityPrototypes == null ? null : entityPrototypes.iterator();
	}

	public int size() {
		return (refData == null ? 0 : refData.size()) + (entityTypes == null ? 0 : entityTypes.size())
				+ (entityPrototypes == null ? 0 : entityPrototypes.size());
	}
}
