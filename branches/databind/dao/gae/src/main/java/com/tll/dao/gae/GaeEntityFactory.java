/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.dao.gae;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.tll.model.AbstractEntityFactory;
import com.tll.model.IEntity;

/**
 * GaePrimaryKeyGenerator - Generates unique id tokens by relying on the dao
 * impl to persist entity instances.
 * @author jpk
 */
public final class GaeEntityFactory extends AbstractEntityFactory<Key> {

	static final Log log = LogFactory.getLog(GaeEntityFactory.class);

	@Override
	public boolean isPrimaryKeyGeneratable() {
		return false;
	}

	@Override
	public String primaryKeyToString(Key pk) {
		return KeyFactory.keyToString(pk);
	}

	@Override
	public Key stringToPrimaryKey(String s) {
		return KeyFactory.stringToKey(s);
	}

	@Override
	public Key generatePrimaryKey(IEntity entity) {
		throw new UnsupportedOperationException("GAE relies on initial datastore persistence to obtain primary keys");
	}
}
