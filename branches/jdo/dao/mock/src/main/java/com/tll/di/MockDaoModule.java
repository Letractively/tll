package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.tll.dao.IEntityDao;

/**
 * MockDaoModule - MOCK dao impl module.
 * @author jpk
 */
public class MockDaoModule extends AbstractModule {

	static final Log log = LogFactory.getLog(MockDaoModule.class);

	@Override
	protected void configure() {
		// IEntityDao
		bind(IEntityDao.class).to(com.tll.dao.mock.EntityDao.class).in(Scopes.SINGLETON);
	}

}