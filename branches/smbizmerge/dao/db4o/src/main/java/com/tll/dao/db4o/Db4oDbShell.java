/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.dao.db4o;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.dao.IDbShell;
import com.tll.model.IEntity;
import com.tll.model.egraph.EntityGraph;
import com.tll.model.egraph.IEntityGraphPopulator;

/**
 * MockDbShell
 * @author jpk
 */
public class Db4oDbShell implements IDbShell {

	private static final Log log = LogFactory.getLog(Db4oDbShell.class);

	private final URI dbFile;

	private final IEntityGraphPopulator populator;

	private final Provider<EmbeddedConfiguration> c;

	/**
	 * Constructor
	 * @param dbFile A ref to the db file
	 * @param populator The entity graph populator that defines the db "schema"
	 *        and content.
	 * @param c The db4o configuration (optional).
	 */
	@Inject
	public Db4oDbShell(URI dbFile, IEntityGraphPopulator populator, Provider<EmbeddedConfiguration> c) {
		super();
		this.dbFile = dbFile;
		this.populator = populator;
		this.c = c;
	}

	/**
	 * @return A {@link File} ref to the db file on the file system irregardless
	 *         of whether or not it actually exists.
	 */
	private File getHandle() {
		return new File(dbFile);
	}

	/**
	 * @return A newly created db4o session.
	 */
	public EmbeddedObjectContainer createDbSession() {
		if(c == null) {
			log.info("Instantiating db4o session for: " + dbFile + " with NO configuration");
			return Db4oEmbedded.openFile(dbFile.getPath());
		}
		log.info("Instantiating db4o session for: " + dbFile + " with config: " + c);
		return Db4oEmbedded.openFile(c.get(), dbFile.getPath());
	}

	/**
	 * Closes a db session.
	 * @param session A db4o session
	 */
	public void killDbSession(EmbeddedObjectContainer session) {
		if(session != null) {
			log.info("Killing db4o session for: " + dbFile);
			while(!session.close()) {}
		}
	}
	
	public static void clearData(EmbeddedObjectContainer container) {
		ObjectSet<Object> set = container.queryByExample(null);
		if(set != null) {
			for(Object obj : set) {
				container.delete(obj);
			}
		}
	}

	@Override
	public void clearData() {
		EmbeddedObjectContainer container = createDbSession();
		log.info("Killing db4o session for: " + dbFile);
		try {
			clearData(container);
		}
		finally {
			killDbSession(container);
		}
	}

	@Override
	public void create() {
		File f = getHandle();
		if(f.exists()) return;
		log.info("Creating db4o db: " + f.getPath());
		f = null;
		EmbeddedObjectContainer db = null;
		try {
			db = createDbSession();
		}
		finally {
			killDbSession(db);
		}
	}

	@Override
	public void drop() {
		final File f = getHandle();
		if(!f.exists()) return;
		log.info("Deleting db4o db: " + f.getPath());
		if(!f.delete()) throw new IllegalStateException("Unable to delete db4o file: " + f.getAbsolutePath());
	}
	
	public void addData(EmbeddedObjectContainer dbSession) {
		if(populator == null) throw new IllegalStateException("No populator set");
		try {
			populator.populateEntityGraph();
			final EntityGraph eg = populator.getEntityGraph();
			final Iterator<Class<? extends IEntity>> itr = eg.getEntityTypes();
			while(itr.hasNext()) {
				final Class<? extends IEntity> et = itr.next();
				log.info("Storing entities of type: " + et.getSimpleName() + "...");
				final Collection<? extends IEntity> ec = eg.getEntitiesByType(et);
				for(final IEntity e : ec) {
					log.info("Storing entity: " + et + "...");
					dbSession.store(e);
				}
			}
		}
		catch(final Exception e) {
			log.error("Unable to stub db: " + e.getMessage(), e);
			if(e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void addData() {
		EmbeddedObjectContainer dbSession = null;
		try {
			log.info("Stubbing db4o db for db4o db: " + dbFile);
			dbSession = createDbSession();
			addData(dbSession);
		}
		finally {
			killDbSession(dbSession);
		}
	}
}
