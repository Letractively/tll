/**
 * The Logic Lab
 * @author jpk
 * @since Sep 21, 2009
 */
package com.tll.di.test;

import com.db4o.config.Configuration;
import com.tll.config.Config;
import com.tll.dao.db4o.IDb4oNamedQueryTranslator;
import com.tll.dao.db4o.test.LoadAllQueryTranslator;
import com.tll.di.AbstractDb4oDaoModule;


/**
 * TestDb4oDaoModule
 * @author jpk
 */
public class TestDb4oDaoModule extends AbstractDb4oDaoModule {

	/**
	 * Constructor
	 */
	public TestDb4oDaoModule() {
		super();
	}

	/**
	 * Constructor
	 * @param config
	 */
	public TestDb4oDaoModule(Config config) {
		super(config);
	}

	@Override
	protected void configureConfiguration(Configuration c) {
		c.updateDepth(3);
	}

	@Override
	protected Class<? extends IDb4oNamedQueryTranslator> getNamedQueryTranslatorImpl() {
		return LoadAllQueryTranslator.class;
	}

}
