/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.model.key;

import org.datanucleus.store.valuegenerator.ValueGenerator;

import com.google.inject.Inject;
import com.tll.model.IEntity;

/**
 * JdoPrimaryKeyGenerator - Generates unique id tokens based on DataNucleus'
 * {@link ValueGenerator} strategies.
 * @author jpk
 */
public class JdoPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	private final ValueGenerator generator;

	/**
	 * Constructor
	 * @param generator the required value generator
	 */
	@Inject
	public JdoPrimaryKeyGenerator(ValueGenerator generator) {
		super();
		if(generator == null) throw new NullPointerException();
		this.generator = generator;
	}

	@Override
	public synchronized String generateIdentifier(Class<? extends IEntity> entityClass) {
		final Object o = generator.next();
		return o.toString();
	}
}
