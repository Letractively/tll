/*
 * The Logic Lab 
 */
package com.tll.di;

import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.server.SecurityMode;
import com.tll.util.EnumUtil;

/**
 * SecurityModule
 * @author jpk
 */
public class SecurityModule extends CompositeModule {

	/**
	 * Security mode override.
	 */
	private final SecurityMode mode;

	/**
	 * Constructor
	 */
	public SecurityModule() {
		this(null);
	}

	/**
	 * Constructor
	 * @param mode
	 */
	public SecurityModule(SecurityMode mode) {
		super();
		this.mode =
				mode == null ? EnumUtil.fromString(SecurityMode.class, Config.instance().getString(
						ConfigKeys.SECURITY_MODE_PARAM.getKey())) : mode;
	}

	@Override
	protected Module[] getModulesToBind() {
		if(SecurityMode.ACEGI.equals(mode)) {
			return new Module[] { new AcegiModule() };
		}
		return null;
	}

}
