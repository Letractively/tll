/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.jdo;

import java.util.List;

import javax.jdo.PersistenceManagerFactory;

import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.IDbTrans;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.dao.UserDaoTestHandler;
import com.tll.dao.jdo.test.JdoTrans;
import com.tll.di.SmbizEGraphModule;
import com.tll.di.SmbizJdoDaoModule;
import com.tll.di.SmbizModelModule;

/**
 * SmbizJdoEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "jdo" })
public class SmbizJdoEntityDaoTest extends AbstractJdoEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		/*
		try {
			final Class<?>[] handlerTypes =
				CommonUtil.getClasses("com.tll.dao", IEntityDaoTestHandler.class, true, null, null);

			final IEntityDaoTestHandler<?>[] arr = new IEntityDaoTestHandler[handlerTypes.length];
			int i = 0;
			for(final Class<?> type : handlerTypes) {
				final IEntityDaoTestHandler<?> handler = (IEntityDaoTestHandler<?>) type.newInstance();
				arr[i++] = handler;
			}
			return arr;
		}
		catch(final ClassNotFoundException e) {
			throw new IllegalStateException("Unable to obtain the entity dao test handlers: " + e.getMessage(), e);
		}
		catch(final InstantiationException e) {
			throw new IllegalStateException("Unable to instantiate an entity dao test handler: " + e.getMessage(), e);
		}
		catch(final IllegalAccessException e) {
			throw new IllegalStateException("Unable to access an entity dao test handler: " + e.getMessage(), e);
		}
		 */
		return new IEntityDaoTestHandler<?>[] {
			new UserDaoTestHandler(),
		};
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizModelModule());
		modules.add(new SmbizEGraphModule());
		modules.add(new SmbizJdoDaoModule(getConfig()));
		// ad hoc IDbTrans binding
		modules.add(new Module() {

			@Override
			public void configure(Binder theBinder) {
				theBinder.bind(IDbTrans.class).toProvider(new Provider<IDbTrans>() {

					@Inject
					PersistenceManagerFactory pmf;

					@Override
					public IDbTrans get() {
						return new JdoTrans(pmf);
					}
				}).in(Scopes.SINGLETON);
			}
		});
	}

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("jdo-config.properties"));
	}

}
