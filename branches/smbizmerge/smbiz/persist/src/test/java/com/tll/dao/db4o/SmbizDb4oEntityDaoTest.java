/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.db4o;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.model.SmbizEGraphModule;
import com.tll.util.CommonUtil;

/**
 * SmbizDb4oEntityDaoTest
 * @author jpk
 */
@Test(groups = { "dao", "db4o" })
public class SmbizDb4oEntityDaoTest extends AbstractDb4oEntityDaoTest {

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

//		return new IEntityDaoTestHandler<?>[] {
//			new AspAccountDaoTestHandler(),
//			new MerchantDaoTestHandler(),
//			new CustomerAccountDaoTestHandler(),
//			new InterfaceSingleDaoTestHandler(),
//			new InterfaceSwitchDaoTestHandler(),
//		};

//		return new IEntityDaoTestHandler<?>[] { new AuthorityDaoTestHandler() };
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizDb4oDaoModule(getConfig()));
		modules.add(new SmbizEGraphModule());
	}

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("db4o-config.properties"));
	}

}
