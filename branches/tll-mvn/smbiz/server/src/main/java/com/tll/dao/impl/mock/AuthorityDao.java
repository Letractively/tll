/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IAuthorityDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.Authority;
import com.tll.model.key.IBusinessKeyFactory;

public class AuthorityDao extends EntityDao<Authority> implements IAuthorityDao, IMockDao<Authority> {

	@Inject
	public AuthorityDao(Set<Authority> set, IBusinessKeyFactory bkf) {
		super(Authority.class, set, bkf);
	}
}