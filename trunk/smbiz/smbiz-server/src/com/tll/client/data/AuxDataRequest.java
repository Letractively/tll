/**
 * The Logic Lab
 * @author jpk Nov 6, 2007
 */
package com.tll.client.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tll.client.IMarshalable;

/**
 * AuxDataRequest - Way to request "auxiliary" data when issuing an RPC call.
 * @author jpk
 */
public class AuxDataRequest implements IMarshalable {

	public static final int TYPE_ENUM = 1;
	public static final int TYPE_REF_DATA = 2;
	public static final int TYPE_ENTITY = 3;

	public static final String enumTypePrefix = Integer.toString(TYPE_ENUM);
	public static final String refDataTypePrefix = Integer.toString(TYPE_REF_DATA);
	public static final String entityTypePrefix = Integer.toString(TYPE_ENTITY);

	/**
	 * Holds the requests.
	 */
	private Set<String> set = new HashSet<String>();

	/**
	 * Request an app enum in the form of a string/string map.
	 * @param enumClassName
	 */
	public void requestEnum(String enumClassName) {
		set.add(getKey(TYPE_ENUM, enumClassName));
	}

	/**
	 * Request app ref data in the form of a string/string map.
	 * @param terseName
	 */
	public void requestAppRefData(String terseName) {
		set.add(getKey(TYPE_REF_DATA, terseName));
	}

	/**
	 * Request a listing of a particular entity type.
	 * @param entityTypeName The enum EntityType name.
	 */
	public void requestEntityList(String entityTypeName) {
		set.add(getKey(TYPE_ENTITY, entityTypeName));
	}

	private String getKey(int type, String typeKey) {
		return Integer.toString(type) + typeKey;
	}

	public Iterator<String> requestIterator() {
		return set.iterator();
	}

	public int size() {
		return set.size();
	}

	public String descriptor() {
		return "Auxiliary Data Request";
	}

}
