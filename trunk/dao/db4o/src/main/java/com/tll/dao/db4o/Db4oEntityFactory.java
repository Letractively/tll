/**
 * The Logic Lab
 * @author jpk
 * @since Jan 23, 2010
 */
package com.tll.dao.db4o;

import com.db4o.EmbeddedObjectContainer;
import com.google.inject.Inject;
import com.tll.model.AbstractEntityFactory;
import com.tll.model.IEntity;
import com.tll.model.IEntityMetadata;

/**
 * Db4oEntityFactory
 * @author jpk
 */
public class Db4oEntityFactory extends AbstractEntityFactory {

	private final IEntityMetadata entityMetadata;
	private EmbeddedObjectContainer oc;
	private IdState state;

	/**
	 * Constructor
	 * @param oc Required {@link EmbeddedObjectContainer} provider
	 * @param entityMetadata required to resolve root entities thus avoiding
	 *        potentially non-unique business keys!
	 */
	@Inject
	public Db4oEntityFactory(IEntityMetadata entityMetadata, EmbeddedObjectContainer oc) {
		super();
		this.entityMetadata = entityMetadata;
		setObjectContainer(oc);
	}

	@Override
	public boolean isPrimaryKeyGeneratable() {
		return true;
	}

	/**
	 * Sets the object container provider.
	 * @param oc required
	 */
	public void setObjectContainer(EmbeddedObjectContainer oc) {
		if(oc == null) throw new NullPointerException();
		this.oc = oc;
	}

	@Override
	public Long generatePrimaryKey(IEntity entity) {
		if(state == null) {
			try {
				state = oc.query(IdState.class).get(0);
				log.info(state == null ? "Db4o primary key state NOT acquired." : "Db4o primary key state acquired.");
			}
			catch(Exception e) {
				log.info("Creating Db4o primary key state entity.");
				state = new IdState();
				oc.store(state);
			}
		}

		assert state != null;
		
		Class<?> entityClass = entityMetadata.getEntityClass(entity);
		Class<?> rootEntityClass = entityMetadata.getRootEntityClass(entityClass);
		Long current = state.getCurrentId(rootEntityClass);

		final Long next = Long.valueOf(current == null ? 1L : current.longValue() + 1);
		state.setCurrentId(rootEntityClass, next);
		oc.store(state);
		entity.setGenerated(next);
		log.info("Generated Db4o primary key: " + next + " for: " + rootEntityClass.getSimpleName());
		return next;
	}

}
