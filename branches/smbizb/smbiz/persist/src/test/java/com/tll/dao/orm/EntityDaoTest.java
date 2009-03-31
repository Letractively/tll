/**
 * The Logic Lab
 * @author jpk
 * Jan 28, 2009
 */
package com.tll.dao.orm;

import java.io.File;
import java.io.FilenameFilter;

import org.testng.annotations.Test;

import com.tll.dao.AbstractOrmEntityDaoTest;
import com.tll.dao.IEntityDaoTestHandler;
import com.tll.util.CommonUtil;

/**
 * EntityDaoTest
 * @author jpk
 */
@Test(groups = "dao")
public class EntityDaoTest extends AbstractOrmEntityDaoTest {

	@Override
	protected IEntityDaoTestHandler<?>[] getDaoTestHandlers() {
		/*
		return new IEntityDaoTestHandler<?>[] {
			new InterfaceSingleDaoTestHandler()
		};
		*/
		try {
			Class<?>[] handlerTypes =
					CommonUtil.getClasses("com.tll.dao", IEntityDaoTestHandler.class, true, null, new FilenameFilter() {

						@Override
						public boolean accept(File dir, String name) {
							return dir.getPath().indexOf("smbiz") > 0 && dir.getPath().indexOf("test-classes") > 0;
						}
					});

			IEntityDaoTestHandler<?>[] arr = new IEntityDaoTestHandler[handlerTypes.length];
			int i = 0;
			for(Class<?> type : handlerTypes) {
				IEntityDaoTestHandler<?> handler = (IEntityDaoTestHandler<?>) type.newInstance();
				arr[i++] = handler;
			}
			return arr;
		}
		catch(ClassNotFoundException e) {
			throw new IllegalStateException("Unable to obtain the entity dao test handlers: " + e.getMessage(), e);
		}
		catch(InstantiationException e) {
			throw new IllegalStateException("Unable to instantiate an entity dao test handler: " + e.getMessage(), e);
		}
		catch(IllegalAccessException e) {
			throw new IllegalStateException("Unable to access an entity dao test handler: " + e.getMessage(), e);
		}
	}

}
