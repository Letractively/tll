package com.tll.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.model.key.IPrimaryKeyGenerator;
import com.tll.util.StringUtil;

/**
 * EntityFactory - Creates new {@link IEntity} instances and optionally sets the
 * primary key.
 * @author jpk
 */
public final class EntityFactory {

	private static final Log log = LogFactory.getLog(EntityFactory.class);

	/**
	 * The optional primary key generator.
	 */
	private final IPrimaryKeyGenerator keyGenerator;

	/**
	 * Constructor - No primary key generator will be employed
	 */
	public EntityFactory() {
		this(null);
	}

	/**
	 * Constructor
	 * @param keyGenerator Optional primary key generator impl. If specified,
	 *        primary keys are created and set for newly created entities
	 */
	public EntityFactory(IPrimaryKeyGenerator keyGenerator) {
		super();
		this.keyGenerator = keyGenerator;
	}

	/**
	 * The entity factory method.
	 * @param <E>
	 * @param entityClass entity type to create
	 * @param generate set the primary key of the created entity?
	 * @return newly created entity
	 * @throws IllegalStateException Upon error instantiating the entity or when
	 *         generate primary key is desired but not primary key generator was
	 *         not set
	 */
	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) throws IllegalStateException {
		E entity;
		try {
			entity = entityClass.newInstance();
		}
		catch(final IllegalAccessException iae) {
			throw new IllegalStateException(StringUtil.replaceVariables(
					"Could not access default constructor for entity type: '%1'.", entityClass.getName()), iae);
		}
		catch(final InstantiationException ie) {
			throw new IllegalStateException(StringUtil.replaceVariables("Unable to instantiate the entity: %1", entityClass
					.getName()), ie);
		}

		if(log.isDebugEnabled()) log.debug("Created entity: " + entity);

		if(generate) {
			if(keyGenerator == null)
				throw new IllegalStateException("Unable to generate entity primary key - no primary key generator set");
			long id = keyGenerator.generateIdentifier(entity);
			if(log.isDebugEnabled()) log.debug("Created entity ID: " + id);
			entity.setGenerated(id);
		}

		return entity;
	}
}
