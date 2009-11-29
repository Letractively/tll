/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.jdo;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.di.JdoDaoModule;
import com.tll.di.SmbizEGraphModule;
import com.tll.di.SmbizJdoDaoModule;
import com.tll.di.SmbizModelModule;
import com.tll.util.CommonUtil;

/**
 * JdoEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "jdo" })
public class SmbizJdoEntityDaoTest extends AbstractJdoEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
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

		/*
		return new IEntityDaoTestHandler<?>[] {
			new AspAccountDaoTestHandler(),
			new MerchantDaoTestHandler(),
			new CustomerAccountDaoTestHandler(),
			new InterfaceSingleDaoTestHandler(),
			new InterfaceSwitchDaoTestHandler(),
		};
		*/
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizModelModule());
		modules.add(new SmbizEGraphModule());
		modules.add(new SmbizJdoDaoModule(getConfig()));
	}

	@Override
	protected Config doGetConfig() {
		final Config c = Config.load(new ConfigRef("jdo-config.properties"));
		c.setProperty(JdoDaoModule.ConfigKeys.DB_TRANS_BINDTOSPRING.getKey(), Boolean.FALSE);
		return c;
	}

}
