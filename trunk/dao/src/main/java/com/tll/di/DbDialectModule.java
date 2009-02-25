/**
 * The Logic Lab
 * @author jpk
 * Jan 24, 2009
 */
package com.tll.di;

import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.jdbc.DbDialectHandlerBuilder;

/**
 * DbDialectModule - Resolves the db "type" from the configuration and wires it
 * up accordingly.
 * <p>
 * The db dialect is used by the dao and db shell layers.
 * @author jpk
 */
public class DbDialectModule extends GModule {

	@Override
	protected void configure() {
		bind(IDbDialectHandler.class).to(DbDialectHandlerBuilder.getDbDialectHandlerTypeFromDbType(Config.instance())).in(
				Scopes.SINGLETON);
	}

}
