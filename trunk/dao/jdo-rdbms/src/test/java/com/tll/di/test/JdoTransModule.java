/**
 * The Logic Lab
 * @author jpk
 * @since Jan 17, 2010
 */
package com.tll.di.test;

import javax.jdo.PersistenceManagerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.dao.IDbTrans;
import com.tll.dao.jdo.test.JdoTrans;

/**
 * JdoTransModule - JDO IDbTrans binding.
 * @author jpk
 */
public class JdoTransModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(IDbTrans.class).toProvider(new Provider<IDbTrans>() {

			@Inject
			PersistenceManagerFactory pmf;

			@Override
			public IDbTrans get() {
				return new JdoTrans(pmf);
			}
		}).in(Scopes.SINGLETON);
	}

}
