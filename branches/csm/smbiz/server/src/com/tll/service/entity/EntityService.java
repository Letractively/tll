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

import com.tll.SystemError;
import com.tll.criteria.ICriteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IEntityDao;
import com.tll.dao.INamedEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.EntityAssembler;
import com.tll.model.IEntity;
import com.tll.model.key.BusinessKey;
import com.tll.model.key.NameKey;
import com.tll.model.key.PrimaryKey;
import com.tll.model.validate.EntityValidatorFactory;
import com.tll.model.validate.IEntityValidator;

/**
 * EntityService - Base class for all entity service implementations.
 * @author jpk
 */
@Transactional
public abstract class EntityService<E extends IEntity, D extends IEntityDao<E>> implements IEntityService<E> {

	protected final Log log;

	/**
	 * The type-wise "primary" dao
	 */
	protected final D dao;

	/**
	 * The entity assembler.
	 */
	protected final EntityAssembler entityAssembler;

	/**
	 * Constructor
	 * @param daoFactory
	 * @param entityAssembler
	 * @param validatorDelegate
	 */
	protected EntityService(Class<D> daoClass, D dao, EntityAssembler entityAssembler) {
		super();
		this.log = LogFactory.getLog(this.getClass());
		this.dao = dao;
		this.entityAssembler = entityAssembler;
	}

	public abstract Class<E> getEntityClass();

	protected <SE extends E> IEntityValidator<SE> getEntityValidator(Class<SE> specificEntityType) {
		return EntityValidatorFactory.instance(specificEntityType);
	}

	@SuppressWarnings("unchecked")
	private void validateAll(Collection<E> entities) throws InvalidStateException {
		if(entities != null && entities.size() > 0) {
			InvalidValue[] arr = new InvalidValue[] {};
			for(E e : entities) {
				if(e != null) {
					try {
						getEntityValidator((Class<E>) e.entityClass()).validate(e);
					}
					catch(InvalidStateException ise) {
						arr = (InvalidValue[]) ArrayUtils.add(arr, ise.getInvalidValues());
					}
				}
			}
			if(arr.length > 0) {
				throw new InvalidStateException(arr);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public E persist(E entity) throws EntityExistsException, InvalidStateException {
		getEntityValidator((Class<E>) entity.entityClass()).validate(entity);
		return dao.persist(entity);
	}

	public Collection<E> persistAll(Collection<E> entities) {
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
	public E load(PrimaryKey<? extends E> key) throws EntityNotFoundException {
		return dao.load(key);
	}

	@Transactional(readOnly = true)
	public E load(BusinessKey<? extends E> key) throws EntityNotFoundException {
		return dao.load(key);
	}

	@SuppressWarnings("unchecked")
	public E load(NameKey key) throws EntityNotFoundException {
		if(this instanceof INamedEntityService == false) {
			throw new SystemError("This entity service does not provide load by name");
		}
		return (E) ((INamedEntityDao) dao).load(key);
	}

	@Transactional(readOnly = true)
	public List<E> loadAll() {
		return dao.loadAll();
	}

	@Transactional(readOnly = true)
	public List<E> findEntities(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.findEntities(criteria, sorting);
	}

	// IListHandlerDataProvider impl:

	@Transactional(readOnly = true)
	public List<E> loadByIds(List<Integer> ids, Sorting sorting) {
		return dao.findByIds(ids, sorting);
	}

	@Transactional(readOnly = true)
	public List<SearchResult<E>> find(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.find(criteria, sorting);
	}

	@Transactional(readOnly = true)
	public List<E> getEntitiesFromIds(Class<? extends E> entityClass, Collection<Integer> ids, Sorting sorting) {
		return dao.getEntitiesFromIds(entityClass, ids, sorting);
	}

	@Transactional(readOnly = true)
	public List<Integer> getIds(ICriteria<? extends E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.getIds(criteria, sorting);
	}

	@Transactional(readOnly = true)
	public IPageResult<SearchResult<E>> getPage(ICriteria<? extends E> criteria, Sorting sorting, int offset, int pageSize)
			throws InvalidCriteriaException {
		return dao.getPage(criteria, sorting, offset, pageSize);
	}
}
