/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.ISiteCodeDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.SiteCode;

public class SiteCodeDao extends EntityDao<SiteCode> implements ISiteCodeDao, IMockDao<SiteCode> {

	@Inject
	public SiteCodeDao(Set<SiteCode> set) {
		super(SiteCode.class, set);
	}
}