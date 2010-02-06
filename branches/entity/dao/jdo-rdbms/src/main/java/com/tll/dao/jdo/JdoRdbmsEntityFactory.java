/**
 * The Logic Lab
 * @author jpk
 * @since Oct 27, 2009
 */
package com.tll.dao.jdo;

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

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.model.AbstractEntityFactory;
import com.tll.model.IEntity;

/**
 * JdoRdbmsEntityFactory - Generates unique id tokens based on DataNucleus'
 * {@link ValueGenerator} strategies.
 * @author jpk
 */
public class JdoRdbmsEntityFactory extends AbstractEntityFactory<Long> {

	static final Log log = LogFactory.getLog(JdoRdbmsEntityFactory.class);

	private ValueGenerator generator;

	/**
	 * Constructor
	 * @param pmProvider the required {@link PersistenceManager} provider
	 */
	@Inject
	public JdoRdbmsEntityFactory(Provider<PersistenceManager> pmProvider) {
		super();
		if(pmProvider == null) throw new NullPointerException();

		final JDOPersistenceManager jdopm;
		try {
			jdopm = (JDOPersistenceManager) pmProvider.get();
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
		// use a single sequence for all entity types
		// NOTE: ids will *not* necessarily be contiguous
		// 	and there is no rhyme or reason to ascribing ids to entities
		//  and is soley a function
		properties.setProperty("sequence-name", "GLOBAL");

		// Obtain a ValueGenerationManager
		final ValueGenerationManager mgr = new ValueGenerationManager();

		generator =
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
	}

	@Override
	public <E extends IEntity> E createEntity(Class<E> entityClass, boolean generate) throws IllegalStateException {
		return newEntity(entityClass);
	}

	@Override
	public String primaryKeyToString(Long pk) {
		return pk == null ? null : pk.toString();
	}

	@Override
	public Long stringToPrimaryKey(String s) {
		return s == null ? null : Long.valueOf(s);
	}

	@Override
	// NOTE: the internals of generator are synchronized
	public /*synchronized*/ Long generatePrimaryKey(IEntity entity) {
		final long id = generator.nextValue();
		log.debug(">Generated jdo-rdbms id: " + id);
		entity.setGenerated(id);
		return id;
	}
}
