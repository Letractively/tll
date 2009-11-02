/**
 * The Logic Lab
 * @author jpk
 * @since Nov 1, 2009
 */
package com.tll.di;

import java.sql.Connection;
import java.util.Properties;

import javax.jdo.PersistenceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datanucleus.ManagedConnection;
import org.datanucleus.ObjectManager;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.jdo.JDOPersistenceManager;
import org.datanucleus.store.StoreManager;
import org.datanucleus.store.rdbms.RDBMSManager;
import org.datanucleus.store.valuegenerator.ValueGenerationConnectionProvider;
import org.datanucleus.store.valuegenerator.ValueGenerationManager;
import org.datanucleus.store.valuegenerator.ValueGenerator;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;

/**
 * JdoRdbmsIdGeneratorModule - Provides a {@link ValueGenerator} impl intended
 * for eager entity primary key generation.
 * @author jpk
 */
public class JdoRdbmsIdGeneratorModule extends AbstractModule {

	static final Log log = LogFactory.getLog(JdoRdbmsIdGeneratorModule.class);

	@Override
	protected void configure() {
		bind(ValueGenerator.class).toProvider(new Provider<ValueGenerator>() {

			@Inject
			PersistenceManager pm;

			@Override
			public ValueGenerator get() {

				final JDOPersistenceManager jdopm;
				try {
					jdopm = (JDOPersistenceManager) pm;
				}
				catch(final ClassCastException e) {
					throw new IllegalStateException("PersistenceManager is not a JDOPersistenceManager type");
				}

				final ObjectManager om = jdopm.getObjectManager();
				if(om == null) throw new IllegalStateException("No ObjectManager present");

				final StoreManager sm = om.getStoreManager();
				if(sm == null) throw new IllegalStateException("No StoreManager present");

				// Obtain a ValueGenerator of the required type
				final Properties properties = new Properties();
				properties.setProperty("table-name", "GLOBAL"); // Use a global sequence number (for all tables)

				// Obtain a ValueGenerationManager
				final ValueGenerationManager mgr = new ValueGenerationManager();

				final ValueGenerator generator =
					mgr.createValueGenerator("MyGenerator", org.datanucleus.store.rdbms.valuegenerator.TableGenerator.class,
							properties, sm, new ValueGenerationConnectionProvider() {

						RDBMSManager rdbmsManager = null;
						ManagedConnection con;

						public ManagedConnection retrieveConnection() {
							rdbmsManager = (RDBMSManager) sm;
							try {
								// TODO verify NO TRANSACTION is correct!
								con = rdbmsManager.getConnection(Connection.TRANSACTION_NONE);
								return con;
							}
							catch(final NucleusException e) {
								log.error("Failed to obtain new DB connection for identity generation!");
								throw e;
							}
						}

						public void releaseConnection() {
							try {
								con.close();
								con = null;
							}
							catch(final NucleusException e) {
								log.error("Failed to close DB connection for identity generation!");
								throw e;
							}
							finally {
								rdbmsManager = null;
							}
						}
					});

				return generator;
			}
		}).in(Scopes.SINGLETON);
	}

}
