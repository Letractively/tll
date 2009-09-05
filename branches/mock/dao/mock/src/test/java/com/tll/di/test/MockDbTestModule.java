package com.tll.di.test;

import com.google.inject.Scopes;
import com.tll.dao.IDbTrans;
import com.tll.dao.mock.MockDbTrans;
import com.tll.di.MockDbShellModule;


/**
 * MockDbTestModule
 * @author jpk
 */
public class MockDbTestModule extends MockDbShellModule {

	public MockDbTestModule() {
		super();
	}

	@Override
	protected void configure() {
		super.configure();
		bind(IDbTrans.class).to(MockDbTrans.class).in(Scopes.SINGLETON);
	}

}
