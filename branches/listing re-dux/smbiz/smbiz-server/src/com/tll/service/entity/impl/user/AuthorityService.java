/**
 * The Logic Lab
 * @author jpk
 * Feb 23, 2008
 */
package com.tll.service.entity.impl.user;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAuthorityDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.Authority;
import com.tll.service.entity.EntityService;

/**
 * AuthorityService - {@link IAuthorityService} impl
 * @author jpk
 */
@Transactional
public class AuthorityService extends EntityService<Authority, IAuthorityDao> implements IAuthorityService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AuthorityService(IAuthorityDao dao, EntityAssembler entityAssembler) {
		super(IAuthorityDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Authority> getEntityClass() {
		return Authority.class;
	}
}
