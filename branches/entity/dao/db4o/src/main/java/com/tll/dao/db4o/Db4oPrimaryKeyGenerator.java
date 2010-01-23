/**
 * The Logic Lab
 * @author jpk
 * @since Jan 23, 2010
 */
package com.tll.dao.db4o;

import com.tll.model.GlobalLongPrimaryKey;
import com.tll.model.IPrimaryKeyGenerator;


/**
 * Db4oPrimaryKeyGenerator
 * @author jpk
 */
public class Db4oPrimaryKeyGenerator implements IPrimaryKeyGenerator<GlobalLongPrimaryKey> {

	@Override
	public GlobalLongPrimaryKey generateIdentifier(Class<?> entityType) {
		throw new UnsupportedOperationException("TODO implement");
	}

}
