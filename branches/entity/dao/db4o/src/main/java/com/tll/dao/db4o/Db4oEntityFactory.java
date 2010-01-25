/**
 * The Logic Lab
 * @author jpk
 * @since Jan 23, 2010
 */
package com.tll.dao.db4o;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AbstractEntityFactory;
import com.tll.model.IEntity;


/**
 * Db4oEntityFactory
 * @author jpk
 */
public class Db4oEntityFactory extends AbstractEntityFactory<Long> {

	private final IEntityDao dao;
	private final IdState state;
	
	/**
	 * Constructor
	 * @param dao required
	 */
	@Inject
	public Db4oEntityFactory(IEntityDao dao) {
		super();
		if(dao == null) throw new NullPointerException();
		this.dao = dao;
		state = dao.load(IdState.class, Long.valueOf(1));
	}

	@Override
	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) throws IllegalStateException {
		return newEntity(entityClass);
	}

	@Override
	public String primaryKeyToString(Long pk) {
		return pk == null ? null : pk.toString();
	}

	@Override
	public Long stringToPrimaryKey(String s) {
		return s == null ? null : Long.valueOf(s);
	}

	@Override
	public Long generatePrimaryKey(IEntity entity) {
		Long current = state.getCurrentId(entity.entityClass());
		final long next = current == null ? 1L : current + 1;
		state.setCurrentId(entity.entityClass(), next);
		dao.persist(state);
		entity.setGenerated(next);
		return Long.valueOf(next);
	}

}
