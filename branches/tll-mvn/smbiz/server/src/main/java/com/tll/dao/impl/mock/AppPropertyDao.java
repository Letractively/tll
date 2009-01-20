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
import com.tll.model.AppProperty;

public class AppPropertyDao extends EntityDao<AppProperty> implements IAppPropertyDao, IMockDao<AppProperty> {

	@Inject
	public AppPropertyDao(Set<AppProperty> set) {
		super(AppProperty.class, set);
	}
}