/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.model;

import org.datanucleus.store.valuegenerator.UUIDStringGenerator;

import com.tll.model.key.IPrimaryKeyGenerator;

/**
 * JdoPrimaryKeyGenerator
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
