/**
 * The Logic Lab
 * @author jpk
 * @since Sep 16, 2009
 */
package com.tll.di.test;

import com.google.inject.Scopes;
import com.tll.dao.IDbTrans;
import com.tll.dao.db4o.Db4oTrans;
import com.tll.di.Db4oDbShellModule;


/**
 * Db4oDbTestModule
 * @author jpk
 */
public class Db4oDbTestModule extends Db4oDbShellModule {

	@Override
	protected void configure() {
		super.configure();
		bind(IDbTrans.class).to(Db4oTrans.class).in(Scopes.SINGLETON);
	}
}
