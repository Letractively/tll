package com.tll.model.key;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.TestBase;
import com.tll.dao.DaoMode;
import com.tll.guice.DaoModule;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.IEntity;
import com.tll.model.impl.key.AccountNameKey;
import com.tll.util.CommonUtil;

@Test(groups = "model.key")
public class KeyFactoryTest extends TestBase {

	/**
	 * Constructor
	 */
	public KeyFactoryTest() {
		super();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.MOCK));
	}

	@Test(groups = "model.key")
	public void testGetBusinessKey() {
		final AccountNameKey key = KeyFactory.getBusinessKey(AccountNameKey.class);
		Assert.assertNotNull(key, "null AccountNameKey returned!");
	}

	@Test(groups = "model.key")
	@SuppressWarnings("unchecked")
	public void validateAllBusinessKeys() throws Exception {
		Class<? extends IEntity>[] entityClasses;
		try {
			entityClasses = CommonUtil.getClasses("com.tll.model", IEntity.class, true, "Test");
		}
		catch(final ClassNotFoundException e) {
			Assert.fail(e.getMessage());
			return;
		}
		for(final Class<? extends IEntity> entityClass : entityClasses) {
			final IEntity e = getMockEntityProvider().getEntityCopy(entityClass);
			logger.debug("Entity Type: " + e.typeName());
			try {

				// primary key
				final IPrimaryKey pk = KeyFactory.getPrimaryKey(e);
				Assert.assertNotNull(pk, "The entity primary key is null");

				// business key
				final IBusinessKey[] bks = KeyFactory.getBusinessKeys(e);
				Assert.assertTrue(bks != null && bks.length > 0, "Empty business key array");
				for(final IBusinessKey abk : bks) {
					logger.debug("Discovered business key: " + abk.descriptor());
				}
			}
			catch(final BusinessKeyNotDefinedException e1) {
				logger.debug("No business keys defined for: " + e.typeName());
			}
		}

	}
}
