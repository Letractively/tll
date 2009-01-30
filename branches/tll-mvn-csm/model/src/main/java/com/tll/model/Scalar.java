package com.tll.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Scalar - IScalar implementation
 * @author jpk
 */
public final class Scalar implements IScalar {

	private final Class<? extends IEntity> refType;
	private final Map<String, Object> map;

	/**
	 * Constructor
	 * @param refType May NOT be <code>null</code>
	 * @param tuple May be <code>null</code>. If NOT <code>null</code>, the
	 *        aliases array MUST be of equal length.
	 * @param aliases The aliases (column names) array expected to be of equal
	 *        size as compared to the <code>tuple</code> array.
	 */
	public Scalar(Class<? extends IEntity> refType, Object[] tuple, String[] aliases) {
		super();
		assert refType != null : "A ref type must be specified for scalars";
		this.refType = refType;
		if(tuple != null) {
			assert aliases != null && aliases.length == tuple.length : "The tuble and aliases array sizes don't match";
			this.map = new HashMap<String, Object>(tuple.length);
			for(int i = 0; i < tuple.length; i++) {
				map.put(aliases[i], tuple[i]);
			}
		}
		else {
			this.map = new HashMap<String, Object>(0);
		}
	}

	/**
	 * Constructor
	 * @param refType May NOT be <code>null</code>
	 * @param map The scalar map
	 */
	public Scalar(Class<? extends IEntity> refType, Map<String, Object> map) {
		super();
		assert refType != null : "A ref type must be specified for scalars";
		this.refType = refType;
		if(map == null) {
			this.map = new HashMap<String, Object>(0);
		}
		else {
			this.map = map;
		}
	}

	public Class<? extends IEntity> getRefType() {
		return refType;
	}

	public Map<String, Object> getTupleMap() {
		return map;
	}

	public String getPropertyPath(String propertyName) {
		// a scalar is expected to only have 1-level deep properties in an OGNL
		// (property path) sense
		// so the property name should be stripped of any prefixing property
		// path
		assert propertyName != null;
		String[] props = propertyName.split("\\.");
		return "tupleMap[" + props[props.length - 1] + ']';
	}

}