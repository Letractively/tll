/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.model.key;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;
import com.tll.model.IEntity;
import com.tll.model.IPrimaryKeyGenerator;

/**
 * GaePrimaryKeyGenerator - Generates unique id tokens by relying on the dao
 * impl to persist entity instances.
 * @author jpk
 */
public final class GaePrimaryKeyGenerator implements IPrimaryKeyGenerator<Key> {

	static final Log log = LogFactory.getLog(GaePrimaryKeyGenerator.class);

	private final IGaePrimaryKeyGeneratorImpl impl;

	/**
	 * Constructor
	 * @param impl The required gae pk generator impl
	 */
	@Inject
	public GaePrimaryKeyGenerator(IGaePrimaryKeyGeneratorImpl impl) {
		super();
		if(impl == null) throw new NullPointerException();
		this.impl = impl;
	}

	@Override
	public Key generateIdentifier(IEntity entity) {
		return impl.generateKey(entity);
	}
}
