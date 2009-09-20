/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.db4o;

import org.testng.annotations.Test;

import com.tll.dao.IEntityDaoTestHandler;
import com.tll.model.IEntityGraphPopulator;
import com.tll.model.SmbizEntityGraphBuilder;
import com.tll.util.CommonUtil;

/**
 * Db4oEntityDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class Db4oEntityDaoTest extends AbstractDb4oEntityDaoTest {

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
	}

	@Override
	protected Class<? extends IEntityGraphPopulator> getEntityGraphPopulator() {
		return SmbizEntityGraphBuilder.class;
	}

}
