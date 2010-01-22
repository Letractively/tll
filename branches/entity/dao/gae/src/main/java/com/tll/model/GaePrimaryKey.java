/**
 * The Logic Lab
 * @author jpk
 * @since Jan 22, 2010
 */
package com.tll.model;

import com.google.appengine.api.datastore.Key;
import com.tll.key.AbstractKey;


/**
 * GaePrimaryKey
 * @author jpk
 */
public class GaePrimaryKey extends AbstractKey implements IPrimaryKey {

	private static final long serialVersionUID = -683631901301021253L;
	
	private Key gkey;

	/**
	 * Constructor
	 * @param type The required entity type to which this key refers
	 */
	public GaePrimaryKey(Class<?> type) {
		super(type);
	}

	@Override
	public void clear() {
		gkey = null;
	}

	@Override
	public boolean isSet() {
		return gkey == null ? false : gkey.isComplete();
	}

	@Override
	public String descriptor() {
		return "Id: " + (gkey == null ? "-" : gkey.getId());
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public String toString() {
		return descriptor();
	}

}
