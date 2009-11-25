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

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.jdk.JdkClass;
import com.google.inject.Inject;
import com.tll.dao.IDbShell;
import com.tll.model.EntityGraph;
import com.tll.model.IEntity;
import com.tll.model.IEntityGraphPopulator;

/**
 * MockDbShell
 * @author jpk
 */
public class Db4oDbShell implements IDbShell {

	private static final Log log = LogFactory.getLog(Db4oDbShell.class);

	/**
	 * Clears out all stored data (hopefully) in target given db4o db given a
	 * session ref bound to it.
	 * @param oc the session ref to the target db to clear
	 */
	private static void clearDb4oDb(ObjectContainer oc) {
		if(oc == null) return;
		final ExtObjectContainer s = oc.ext();
		final ReflectClass[] rcs = s.knownClasses();
		if(rcs != null) {
			for(final ReflectClass rc : rcs) {
				if(rc.getDelegate() instanceof JdkClass) {
					final Class<?> objType = ((JdkClass) rc.getDelegate()).getJavaClass();
					final String cn = objType.getName();
					if(cn.indexOf("com.db4o") < 0 && objType != Object.class && cn.indexOf("java") < 0) {
						final Collection<?> clc = s.query(objType);
						if(clc != null) {
							for(final Object o : clc) {
								log.debug("Removing object: " + o);
								s.delete(o);
							}
						}
					}
				}
			}
			s.purge();
		}
	}

	@Inject
	private final URI dbFile;
	@Inject(optional = true)
	private final IEntityGraphPopulator populator;
	//@Inject
	//private final Configuration c;

	/**
	 * Constructor
	 * @param dbFile A ref to the db file
	 * @param populator The entity graph populator that defines the db "schema"
	 *        and content.
	 */
	public Db4oDbShell(URI dbFile, IEntityGraphPopulator populator/*, Configuration c*/) {
		super();
		this.dbFile = dbFile;
		this.populator = populator;
		//this.c = c;
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
	private ObjectContainer createDbSession() {
		//if(c == null) {
			log.info("Instantiating db4o session for: " + dbFile + " with NO configuration");
			return Db4o.openFile(dbFile.getPath());
		/*
		}
		log.info("Instantiating db4o session for: " + dbFile + " with config: " + c);
		return Db4o.openFile(c, dbFile.getPath());
		*/
	}

	/**
	 * Closes a db session.
	 * @param session A db4o session
	 */
	private void killDbSession(ObjectContainer session) {
		if(session != null) {
			log.info("Killing db4o session for: " + dbFile);
			while(!session.close()) {
			}
		}
	}

	@Override
	public boolean clear() {
		final File f = getHandle();
		if(!f.exists()) return false;
		log.info("Clearing db4o db: " + f.getPath());
		ObjectContainer oc = null;
		try {
			oc = createDbSession();
			clearDb4oDb(oc);
		}
		finally {
			if(oc != null) killDbSession(oc);
		}
		return true;
	}

	/**
	 * Clears the database targeted by the given db session ref of all data. If
	 * the db doesn't exist, nothing happens.
	 * @param dbSession The session that targets the desired db to clear
	 * @return <code>true</code> if the db was actually cleared as a result of
	 *         calling this method and
	 *         <code>false<code> if the db is <code>not</code> cleared by way of
	 *         this method.
	 */
	public boolean clear(Object dbSession) {
		if(dbSession instanceof ObjectContainer) {
			log.info("Clearing db4o db for session: " + dbSession);
			clearDb4oDb((ObjectContainer) dbSession);
			return true;
		}
		return false;
	}

	@Override
	public boolean create() {
		File f = getHandle();
		if(f.exists()) return false;
		log.info("Creating db4o db: " + f.getPath());
		f = null;
		ObjectContainer db = null;
		try {
			db = createDbSession();
		}
		finally {
			killDbSession(db);
		}
		return true;
	}

	@Override
	public boolean delete() {
		final File f = getHandle();
		if(!f.exists()) return false;
		log.info("Deleting db4o db: " + f.getPath());
		if(!f.delete()) throw new IllegalStateException("Unable to delete db4o file: " + f.getAbsolutePath());
		return true;
	}

	@Override
	public boolean stub() {
		ObjectContainer dbSession = null;
		try {
			log.info("Stubbing db4o db for db4o db: " + dbFile);
			dbSession = createDbSession();
			return doStub(dbSession);
		}
		finally {
			killDbSession(dbSession);
		}
	}

	/**
	 * Adds data to the db targeted by the given db sesssion ref serving to stub
	 * the db. The db <em>must</em> already exist else an error is raised.
	 * @param dbSession The session that targets the desired db to stub
	 * @return <code>true</code> if the db was actually stubbed with the stub data
	 *         as a result of calling this method.
	 */
	public boolean stub(Object dbSession) {
		if(dbSession instanceof ObjectContainer) {
			log.info("Stubbing db4o db for session: " + dbSession);
			doStub((ObjectContainer) dbSession);
			return true;
		}
		return false;
	}

	/**
	 * Stubs a targeted db.
	 * @param dbSession the db session ref
	 * @return true/false
	 */
	private boolean doStub(ObjectContainer dbSession) {
		assert dbSession != null;
		try {
			if(populator == null) throw new IllegalStateException("No populator set");
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
			return true;
		}
		catch(final Exception e) {
			log.error("Unable to stub db: " + e.getMessage(), e);
			return false;
		}
	}

	@Override
	public void restub() {
		create();
		clear();
		stub();
	}

	/**
	 * Stubs or re-stubs the data in the targeted db creating the db if not already created
	 * and/or clearing the the db if it contains existing data.
	 * @param dbSession The session that targets the desired db
	 */
	public void restub(Object dbSession) {
		clear(dbSession);
		stub(dbSession);
	}
}
