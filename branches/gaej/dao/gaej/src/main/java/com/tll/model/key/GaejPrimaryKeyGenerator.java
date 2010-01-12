/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.model.key;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.store.valuegenerator.UUIDStringGenerator;
import org.datanucleus.store.valuegenerator.ValueGenerator;

import com.tll.model.IEntity;

/**
 * GaejPrimaryKeyGenerator - Generates unique id tokens based on DataNucleus'
 * {@link ValueGenerator} UUID strategy.
 * @author jpk
 */
public class GaejPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	static final Log log = LogFactory.getLog(GaejPrimaryKeyGenerator.class);

	private UUIDStringGenerator generator;

	/**
	 * Constructor
	 */
	public GaejPrimaryKeyGenerator() {
		super();
		generator = new UUIDStringGenerator("gaejIdGenerator", null);
	}

	@Override
	// NOTE: the internals of generator are synchronized
	public /*synchronized*/ long generateIdentifier(Class<? extends IEntity> entityClass) {
		final long id = generator.nextValue();
		log.debug(">Generated id: " + id);
		return id;
	}
}
