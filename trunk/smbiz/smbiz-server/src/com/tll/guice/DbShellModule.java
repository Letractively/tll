package com.tll.guice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.tll.config.Config;
import com.tll.config.ConfigKeys;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.jdbc.DbShell;

/**
 * DbShellModule
 * @author jpk
 */
public class DbShellModule extends CompositeModule {

	@Override
	protected Module[] getModulesToBind() {
		return new Module[] {
			new ProductionDbShellModule(),
			new TestDbShellModule() };
	}

	/**
	 * ProductionDb annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface ProductionDb {
	}

	/**
	 * TestDb annotation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target( {
		ElementType.FIELD,
		ElementType.PARAMETER })
	@BindingAnnotation
	public @interface TestDb {
	}

	/**
	 * AbstractDbShellModule
	 * @author jpk
	 */
	private static abstract class AbstractDbShellModule extends GModule {

		private final String driverClassName, rootDbName, dbName, urlPrefix, username, password, dbSchemaFileName,
				dbDataDeleteFileName, dbDataStubFileName;

		/**
		 * Constructor
		 * @param dbName
		 */
		public AbstractDbShellModule(String dbName) {
			super();
			final Config config = Config.instance();
			this.driverClassName = config.getString(ConfigKeys.DB_DRIVER.getKey());
			this.rootDbName = config.getString(ConfigKeys.DB_NAME_ROOT.getKey());
			this.dbName = dbName;
			this.urlPrefix = config.getString(ConfigKeys.DB_URL_PREFIX.getKey());
			this.username = config.getString(ConfigKeys.DB_USERNAME.getKey());
			this.password = config.getString(ConfigKeys.DB_PASSWORD.getKey());
			this.dbSchemaFileName = config.getString(ConfigKeys.DB_SCHEMA_FILE_NAME.getKey());
			this.dbDataDeleteFileName = config.getString(ConfigKeys.DB_DELETE_FILE_NAME.getKey());
			this.dbDataStubFileName = config.getString(ConfigKeys.DB_STUB_FILE_NAME.getKey());
		}

		protected DbShell createDbShell(IDbDialectHandler exceptionTranslator) {
			return new DbShell(driverClassName, rootDbName, dbName, urlPrefix, username, password, dbSchemaFileName,
					dbDataDeleteFileName, dbDataStubFileName, exceptionTranslator);
		}
	}

	/**
	 * ProductionDbShellModule
	 * @author jpk
	 */
	private static final class ProductionDbShellModule extends AbstractDbShellModule {

		/**
		 * Constructor
		 */
		public ProductionDbShellModule() {
			super(Config.instance().getString(ConfigKeys.DB_NAME.getKey()));
			log.info("Employing Production Db shell");
		}

		@Override
		protected void configure() {

			bind(DbShell.class).annotatedWith(ProductionDb.class).toProvider(new Provider<DbShell>() {

				@Inject
				IDbDialectHandler exceptionTranslator;

				public DbShell get() {
					return createDbShell(exceptionTranslator);
				}

			}).in(Scopes.SINGLETON);
		}
	}

	/**
	 * TestDbShellModule
	 * @author jpk
	 */
	private static final class TestDbShellModule extends AbstractDbShellModule {

		/**
		 * Constructor
		 */
		public TestDbShellModule() {
			super(Config.instance().getString(ConfigKeys.DB_TEST_NAME.getKey()));
			log.info("Employing TEST Db shell");
		}

		@Override
		protected void configure() {

			bind(DbShell.class).annotatedWith(TestDb.class).toProvider(new Provider<DbShell>() {

				@Inject
				IDbDialectHandler exceptionTranslator;

				public DbShell get() {
					return createDbShell(exceptionTranslator);
				}

			}).in(Scopes.SINGLETON);
		}
	}
}