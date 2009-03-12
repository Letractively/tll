/*
 * The Logic Lab 
 */
package com.tll.di;

import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.IConfigKey;
import com.tll.server.SecurityMode;
import com.tll.util.EnumUtil;

/**
 * SecurityModule
 * @author jpk
 */
public class SecurityModule extends CompositeModule {
	
	/**
	 * ConfigKeys - Config keys for the security module.
	 * @author jpk
	 */
	public static enum ConfigKeys implements IConfigKey {
		SECURITY_MODE_PARAM("security.mode");

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

	/**
	 * Security mode override.
	 */
	private final SecurityMode mode;

	/**
	 * Constructor
	 */
	public SecurityModule() {
		super();
		this.mode =
				EnumUtil.fromString(SecurityMode.class, Config.instance().getString(ConfigKeys.SECURITY_MODE_PARAM.getKey()));
	}

	@Override
	protected Module[] getModulesToBind() {
		if(SecurityMode.ACEGI.equals(mode)) {
			return new Module[] { new AcegiModule() };
		}
		return null;
	}

}
