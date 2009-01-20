package com.tll.service.entity;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import com.tll.dao.IEntityDao;
import com.tll.model.IEntity;
import com.tll.model.EntityAssembler;

/**
 * For entities having some sort of state where the distinction between delete
 * and purge is relevant.
 * @author jpk
 * @param <E>
 * @param <D>
 */
@Transactional
public abstract class StatefulEntityService<E extends IEntity, D extends IEntityDao<E>> extends EntityService<E, D> implements IStatefulEntityService<E> {

	/**
	 * Constructor
	 * @param daoClass
	 * @param dao
	 * @param entityAssembler
	 */
	protected StatefulEntityService(Class<D> daoClass, D dao, EntityAssembler entityAssembler) {
		super(daoClass, dao, entityAssembler);
	}

	/**
	 * Delete all entites
	 * @param entities
	 */
	public void deleteAll(Collection<E> entities) {
		if(entities != null && entities.size() > 0) {
			for(E e : entities) {
				delete(e);
			}
		}
	}

}
