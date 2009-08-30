package com.tll.service.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityExistsException;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IEntityDao;
import com.tll.dao.IPageResult;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.model.IEntity;
import com.tll.model.IEntityAssembler;
import com.tll.model.key.IBusinessKey;
import com.tll.model.key.PrimaryKey;

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
	 * The jsr-303 validation factory.
	 */
	private final ValidatorFactory validationFactory;

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	protected EntityService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory validationFactory) {
		super();
		this.log = LogFactory.getLog(this.getClass());
		this.dao = dao;
		this.entityAssembler = entityAssembler;
		this.validationFactory = validationFactory;
	}

	public abstract Class<E> getEntityClass();

	/**
	 * Validates an entity instance.
	 * @param e the entity to validate
	 * @returns The set of invalids
	 */
	// TODO re-instate validation
	protected final Set<ConstraintViolation<E>> validateNoException(E e) {
		// return validationFactory.getValidator().validate(e);
		return null;
	}

	protected final void validate(E e) throws ConstraintViolationException {
		final Set<ConstraintViolation<E>> invalids = validateNoException(e);
		if(invalids != null && invalids.size() > 0) {
			final HashSet<ConstraintViolation<?>> bunk = new HashSet<ConstraintViolation<?>>(invalids.size());
			bunk.addAll(invalids);
			throw new ConstraintViolationException(bunk);
		}
	}

	/**
	 * Validates <em>all</em> entities in a collection.
	 * @param entities The entity collection to validate
	 * @throws ConstraintViolationException When one or more entities are found to
	 *         be invalid in the collection.
	 */
	protected final void validateAll(Collection<E> entities) throws ConstraintViolationException {
		if(entities != null && entities.size() > 0) {
			final HashSet<ConstraintViolation<?>> all = new HashSet<ConstraintViolation<?>>();
			for(final E e : entities) {
				if(e != null) {
					final Set<ConstraintViolation<E>> invalids = validateNoException(e);
					if(invalids != null) {
						all.addAll(invalids);
					}
				}
			}
			if(all.size() > 0) {
				throw new ConstraintViolationException(all);
			}
		}
	}

	public E persist(E entity) throws EntityExistsException, ConstraintViolationException {
		validate(entity);
		return dao.persist(entity);
	}

	public Collection<E> persistAll(Collection<E> entities) throws ConstraintViolationException {
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
	public List<E> findEntities(Criteria<E> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.findEntities(criteria, sorting);
	}

	// IListHandlerDataProvider impl:

	@Transactional(readOnly = true)
	public List<SearchResult<?>> find(Criteria<? extends IEntity> criteria, Sorting sorting)
	throws InvalidCriteriaException {
		return dao.find(criteria, sorting);
	}

	@Transactional(readOnly = true)
	public <ET extends IEntity> List<ET> getEntitiesFromIds(Class<ET> entityClass, Collection<String> ids, Sorting sorting) {
		return dao.findByIds(entityClass, ids, sorting);
	}

	@Transactional(readOnly = true)
	public List<String> getIds(Criteria<? extends IEntity> criteria, Sorting sorting) throws InvalidCriteriaException {
		return dao.getIds(criteria, sorting);
	}

	@Transactional(readOnly = true)
	public IPageResult<SearchResult<?>> getPage(Criteria<? extends IEntity> criteria, Sorting sorting, int offset,
			int pageSize)
			throws InvalidCriteriaException {
		return dao.getPage(criteria, sorting, offset, pageSize);
	}
}
