/**
 * The Logic Lab
 * @author jpk
 * Nov 17, 2007
 */
package com.tll.dao.impl.mock;

import java.util.Set;

import com.google.inject.Inject;
import com.tll.dao.impl.IAppPropertyDao;
import com.tll.dao.mock.EntityDao;
import com.tll.dao.mock.IMockDao;
import com.tll.model.impl.AppProperty;
import com.tll.model.key.IBusinessKeyFactory;

public class AppPropertyDao extends EntityDao<AppProperty> implements IAppPropertyDao, IMockDao<AppProperty> {

	@Inject
	public AppPropertyDao(Set<AppProperty> set, IBusinessKeyFactory bkf) {
		super(AppProperty.class, set, bkf);
	}
}