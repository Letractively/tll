/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.service.entity.user;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Authority;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;

/**
 * AuthorityService - {@link IAuthorityService} impl
 * @author jpk
 */
@Transactional
public class AuthorityService extends NamedEntityService<Authority> implements IAuthorityService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public AuthorityService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Authority> getEntityClass() {
		return Authority.class;
	}
}
