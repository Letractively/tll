/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.service.entity.user;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.Authority;
import com.tll.service.entity.IEntityAssembler;
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
	 */
	@Inject
	public AuthorityService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Authority> getEntityClass() {
		return Authority.class;
	}
}
