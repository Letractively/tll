/**
 * The Logic Lab
 * @author jpk
 * @since Jul 21, 2009
 */
package com.tll.model.key;

import org.datanucleus.store.valuegenerator.UUIDStringGenerator;

import com.tll.model.IEntity;

/**
 * JdoPrimaryKeyGenerator - Primary key generation using the JDO api.
 * @author jpk
 */
public class JdoPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	private final UUIDStringGenerator generator;

	/**
	 * Constructor
	 */
	public JdoPrimaryKeyGenerator() {
		super();
		generator = new UUIDStringGenerator("uuid", null);
	}

	@Override
	public synchronized String generateIdentifier(Class<? extends IEntity> entityClass) {
		return (String) generator.next();
	}

}
