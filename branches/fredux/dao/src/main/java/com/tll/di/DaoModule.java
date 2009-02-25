/*
 * The Logic Lab 
 */
package com.tll.di;

import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.dao.DaoMode;
import com.tll.util.EnumUtil;

/**
 * DaoModule
 * @author jpk
 */
public class DaoModule extends CompositeModule {

	/**
	 * ConfigKeys - Configuration property keys for the dao module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {

		DAO_MODE_PARAM("db.dao.mode");

		private final String key;

		/**
		 * Constructor
		 * @param key
		 */
		private ConfigKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	@Override
	protected Module[] getModulesToBind() {
		final DaoMode daoMode =
				EnumUtil.fromString(DaoMode.class, Config.instance().getString(ConfigKeys.DAO_MODE_PARAM.getKey()));
		switch(daoMode) {
			case ORM:
				return new Module[] {
					new DbDialectModule(), new HibernateDaoModule() };
			case MOCK:
				return new Module[] { new MockDaoModule() };
			default:
				throw new IllegalStateException("Unhandled dao mode: " + daoMode);
		}
	}
}
