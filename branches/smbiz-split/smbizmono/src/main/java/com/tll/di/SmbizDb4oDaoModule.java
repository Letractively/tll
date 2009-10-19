/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.di;

import com.db4o.Db4o;
import com.db4o.config.Configuration;
import com.tll.config.Config;
import com.tll.dao.db4o.IDb4oNamedQueryTranslator;
import com.tll.dao.db4o.SmbizNamedQueryTranslator;


/**
 * SmbizDb4oDaoModule
 * @author jpk
 */
public class SmbizDb4oDaoModule extends Db4oDaoModule {

	/**
	 * Constructor
	 */
	public SmbizDb4oDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public SmbizDb4oDaoModule(Config config) {
		super(config);
	}

	@Override
	protected Configuration getConfiguration() {
		final Configuration c = Db4o.newConfiguration();
		//c.generateVersionNumbers(ConfigScope.GLOBALLY);
		c.updateDepth(3);
		return c;
	}

	@Override
	protected Class<? extends IDb4oNamedQueryTranslator> getNamedQueryTranslatorImpl() {
		return SmbizNamedQueryTranslator.class;
	}

}
