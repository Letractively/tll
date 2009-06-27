/**
 * The Logic Lab
 * @author jpk
 * Jan 21, 2009
 */
package com.tll.service.entity;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidatorFactory;

import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.INamedEntity;
import com.tll.model.key.NameKey;

/**
 * NamedEntityService
 * @param <N> The named entity type
 * @author jpk
 */
public abstract class NamedEntityService<N extends INamedEntity> extends EntityService<N> implements
INamedEntityService<N> {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param validatorFactory
	 */
	public NamedEntityService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory validatorFactory) {
		super(dao, entityAssembler, validatorFactory);
	}

	public N load(NameKey<N> key) throws EntityNotFoundException {
		return dao.load(key);
	}

}
