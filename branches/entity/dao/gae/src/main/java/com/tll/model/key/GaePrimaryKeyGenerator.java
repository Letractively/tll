/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.model.key;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.dao.gae.GaeEntityDao;
import com.tll.model.GaePrimaryKey;

/**
 * GaePrimaryKeyGenerator - Generates unique id tokens by relying on the dao
 * impl to persist entity instances.
 * @author jpk
 */
public class GaePrimaryKeyGenerator implements IPrimaryKeyGenerator<GaePrimaryKey> {

	static final Log log = LogFactory.getLog(GaePrimaryKeyGenerator.class);

	private final IEntityDao dao;

	/**
	 * Constructor
	 * @param dao the required gaej dao impl instance
	 */
	@Inject
	public GaePrimaryKeyGenerator(IEntityDao dao) {
		super();
		if(dao instanceof GaeEntityDao == false)
			throw new IllegalArgumentException("Null or non-gae entity dao argument.");
		this.dao = dao;
	}

	@Override
	public GaePrimaryKey generateIdentifier(Class<?> entityType) {
		// TODO re-impl!
		return null;
		/*
		// sadly, for gae, we must persist to obtain the primary key!
		dao.persist(entity);
		final long id = entity.getId().longValue();
		entity.setGenerated(id);
		log.debug(">GAE generated id: " + id);
		return id;
		*/
	}
}
