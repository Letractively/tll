package com.tll.service.entity;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.springframework.transaction.annotation.Transactional;

import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.model.IEntityAssembler;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;
import com.tll.model.validate.EntityValidatorFactory;
import com.tll.model.validate.IEntityValidator;

/**
 * EntityService - Base class for all entity service implementations.
 * @param <E> The entity type
 * @author jpk
 */
@Transactional
public abstract class EntityService<E extends IEntity> implements IEntityService<E> {

	protected final Log log;

	/**
	 * The entity dao.
	 */
	protected final IEntityDao dao;

	/**
	 * The entity assembler.
	 */
	protected final IEntityAssembler entityAssembler;

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	protected EntityService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super();
		this.log = LogFactory.getLog(this.getClass());
		this.dao = dao;
		this.entityAssembler = entityAssembler;
	}

	public abstract Class<E> getEntityClass();

	/**
	 * Gets the entity validator.
	 * @param <SE> the specific entity type
	 * @param specificEntityType
	 * @return the entity validator
	 */
	protected final <SE extends E> IEntityValidator<SE> getEntityValidator(Class<SE> specificEntityType) {
		return EntityValidatorFactory.instance(specificEntityType);
	}
	
	/**
	 * Validates an entity instance.
	 * @param e the entity to validate
	 * @throws InvalidStateException When the entity is found to be invalid
	 */
	@SuppressWarnings("unchecked")
	protected final void validate(E e) throws InvalidStateException {
		getEntityValidator((Class<E>) e.entityClass()).validate(e);
	}

	/**
	 * Validates <em>all</em> entities in a collection.
	 * @param entities The entity collection to validate
	 * @throws InvalidStateException When one or more entities are found to be
	 *         invalid in the collection.
	 */
	protected final void validateAll(Collection<E> entities) throws InvalidStateException {
		if(entities != null && entities.size() > 0) {
			InvalidValue[] arr = new InvalidValue[] {};
			for(final E e : entities) {
				if(e != null) {
					try {
						validate(e);
					}
					catch(final InvalidStateException ise) {
						arr = (InvalidValue[]) ArrayUtils.add(arr, ise.getInvalidValues());
					}
				}
			}
			if(arr.length > 0) {
				throw new InvalidStateException(arr);
			}
		}
	}

	public E persist(E entity) throws EntityExistsException, InvalidStateException {
		validate(entity);
		return dao.persist(entity);
	}

	public Collection<E> persistAll(Collection<E> entities) throws InvalidStateException {
		validateAll(entities);
		return dao.persistAll(entities);
	}

	public void purge(E entity) {
		dao.purge(entity);
	}

	public void purgeAll(Collection<E> entities) {
		dao.purgeAll(entities);
	}

	@Transactional(readOnly = true)
	public E load(PrimaryKey<E> key) throws EntityNotFoundException {
		return dao.load(key);
	}

	@Transactional(readOnly = true)
	public E load(IBusinessKey<E> key) throws EntityNotFoundException {
		return dao.load(key);
	}

	@Transactional(readOnly = true)
	public List<E> loadAll() {
		return dao.loadAll(getEntityClass());
	}

	@Transactional(readOnly = true)
	public List<E> findEntities(ICriteria<E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.findEntities(criteria, sorting);
	}

	// IListHandlerDataProvider impl:

	@Transactional(readOnly = true)
	public List<E> loadByIds(List<Integer> ids, Sorting sorting) {
		return dao.findByIds(getEntityClass(), ids, sorting);
	}

	@Transactional(readOnly = true)
	public List<SearchResult<E>> find(ICriteria<E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.find(criteria, sorting);
	}

	@Transactional(readOnly = true)
	public List<E> getEntitiesFromIds(Class<E> entityClass, Collection<Integer> ids, Sorting sorting) {
		return dao.getEntitiesFromIds(entityClass, ids, sorting);
	}

	@Transactional(readOnly = true)
	public List<Integer> getIds(ICriteria<E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.getIds(criteria, sorting);
	}

	@Transactional(readOnly = true)
	public IPageResult<SearchResult<E>> getPage(ICriteria<E> criteria, Sorting sorting, int offset, int pageSize)
			throws InvalidCriteriaException {
		return dao.getPage(criteria, sorting, offset, pageSize);
	}
}
