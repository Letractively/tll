package com.tll.dao.hibernate;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.ITimeStampEntity;

/**
 * Base class for EntityDao for {@link ITimeStampEntity} derived entities.
 * @author jpk
 */
public abstract class TimeStampEntityDao<E extends ITimeStampEntity> extends EntityDao<E> implements
		ITimeStampEntityDao<E> {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public TimeStampEntityDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	/**
	 * @param <T>
	 * @param e
	 * @param isCreate
	 * @param date
	 * @return
	 */
	protected static <T extends ITimeStampEntity> Date setTimestamping(T e, boolean isCreate, Date date) {
		final Date old = isCreate ? e.getDateCreated() : e.getDateModified();
		if(isCreate) {
			e.setDateCreated(date);
		}
		e.setDateModified(date);
		return old;
	}

	/**
	 * @param entity
	 * @param now
	 * @return the old (original) create date
	 */
	/*
	protected final Date applyCreateTimestamping(E entity, Date now) {
	  final Date old = setTimestamping(entity, true, now);
	  propagateTimestamping(entity, true, now);
	  return old;
	}
	*/

	/**
	 * @param entity
	 * @param old
	 */
	/*
	protected final void revertCreateTimestamping(E entity, Date old) {
	  setTimestamping(entity, true, old);
	  propagateTimestamping(entity, true, old);
	}
	*/

	/**
	 * @param entity
	 * @param now
	 * @return the old (original) modify date
	 */
	protected final Date applyModifyTimestamping(E entity, Date now) {
		final Date old = setTimestamping(entity, false, now);
		propagateTimestamping(entity, false, now);
		return old;
	}

	/**
	 * @param entity
	 * @param old
	 */
	protected final void revertModifyTimestamping(E entity, Date old) {
		setTimestamping(entity, false, old);
		propagateTimestamping(entity, false, old);
	}

	/**
	 * Override this method when the entity has uni-directional related many associations and
	 * transitive persistence is in play.
	 * @param entity
	 * @param isCreate
	 * @param date
	 */
	protected void propagateTimestamping(E entity, boolean isCreate, Date date) {
		// default implementation does nothing
	}

	@Override
	public E persist(E entity) {
		E merged;
		final Date now = new Date();
		final Date old = entity.getDateModified();
		applyModifyTimestamping(entity, now);
		// IMPT: we don't reset the create timestamping as it is already set
		// and from this context we honor this value
		// final Date old = entity.getDateCreated();
		// applyCreateTimestamping(entity, now);
		boolean b = false;
		try {
			merged = super.persist(entity);
			b = true;
		}
		finally {
			if(!b) {
				revertModifyTimestamping(entity, old);
			}
		}
		return merged;
	}

	@Override
	public Collection<E> persistAll(Collection<E> entities) {
		if(entities == null) return null;
		Collection<E> merged;
		if(entities.size() > 0) {
			final Date now = new Date();
			Map<Integer, Date> map = new HashMap<Integer, Date>(entities.size());
			for(E e : entities) {
				map.put(e.getId(), e.getDateModified());
				applyModifyTimestamping(e, now);
			}
			boolean b = false;
			try {
				merged = super.persistAll(entities);
				b = true;
			}
			finally {
				if(!b) {
					for(E e : entities) {
						revertModifyTimestamping(e, map.get(e.getId()));
					}
				}
			}
		}
		else {
			merged = super.persistAll(entities);
		}
		return merged;
	}
}
