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
import com.tll.model.EntityBase;
import com.tll.model.IEntity;

/**
 * GaePrimaryKeyGenerator - Generates unique id tokens by relying on the dao
 * impl to persist entity instances.
 * @author jpk
 */
public class GaePrimaryKeyGenerator implements IPrimaryKeyGenerator {

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
	public long generateIdentifier(IEntity entity) {
		// sadly, for gae, we must persist to obtain the primary key!
		dao.persist(entity);
		final long id = entity.getId().longValue();
		((EntityBase) entity).setGenerated(id);
		log.debug(">GAE generated id: " + id);
		return id;
	}
}
