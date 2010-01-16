/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.model.key;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.store.valuegenerator.ValueGenerator;

import com.google.inject.Inject;
import com.tll.dao.gaej.GaejEntityDao;
import com.tll.model.EntityBase;
import com.tll.model.IEntity;

/**
 * GaejPrimaryKeyGenerator - Generates unique id tokens based on DataNucleus'
 * {@link ValueGenerator} UUID strategy.
 * @author jpk
 */
public class GaejPrimaryKeyGenerator implements IPrimaryKeyGenerator {

	static final Log log = LogFactory.getLog(GaejPrimaryKeyGenerator.class);

	private final GaejEntityDao dao;

	/**
	 * Constructor
	 * @param dao the required gaej dao impl instance
	 */
	@Inject
	public GaejPrimaryKeyGenerator(GaejEntityDao dao) {
		super();
		this.dao = dao;
	}

	@Override
	public long generateIdentifier(IEntity entity) {
		// sadly, for gae, we must persist to obtain the primary key!
		dao.persist(entity);
		final long id = entity.getId().longValue();
		((EntityBase)entity).setGenerated(id);
		log.debug(">GAE generated id: " + id);
		return id;
	}
}
