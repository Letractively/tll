/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.di;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.dao.IDbShell;
import com.tll.dao.mock.MockDbShell;
import com.tll.model.EntityGraph;
import com.tll.model.IEntityGraphPopulator;

/**
 * MockDbShellModule - Depends on the {@link EGraphModule}.
 * @author jpk
 */
public class MockDbShellModule extends AbstractModule {

	private static final Log log = LogFactory.getLog(MockDbShellModule.class);

	/**
	 * Constructor
	 */
	public MockDbShellModule() {
		super();
	}

	@Override
	protected void configure() {
		log.info("Employing mock db shell module.");

		bind(IDbShell.class).toProvider(new Provider<IDbShell>() {
			
			@Inject
			IEntityGraphPopulator populator;
			
			@Override
			public IDbShell get() {
				return new MockDbShell(new EntityGraph(), populator);
			}

		}).in(Scopes.SINGLETON);
	}

}
