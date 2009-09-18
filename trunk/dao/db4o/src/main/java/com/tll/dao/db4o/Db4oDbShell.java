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
import com.db4o.config.Configuration;
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

	@Inject
	private final URI dbFile;
	@Inject(optional = true)
	private final IEntityGraphPopulator populator;
	@Inject
	private final Configuration c;

	/**
	 * Constructor
	 * @param dbFile A ref to the db file
	 * @param populator The entity graph populator that defines the db "schema" and
	 *        content.
	 * @param c the db4o configuration
	 */
	//@Inject
	public Db4oDbShell(URI dbFile, IEntityGraphPopulator populator, Configuration c) {
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

	private ObjectContainer instantiateObjectContainer() {
		return c == null ? Db4o.openFile(dbFile.getPath()) : Db4o.openFile(c, dbFile.getPath());
	}

	@Override
	public boolean clear() {
		final File f = getHandle();
		if(f.exists()) {
			log.info("Clearing db4o db: " + f.getName());
			f.delete();
			create();
			return true;
		}
		return false;
	}

	@Override
	public boolean create() {
		File f = getHandle();
		if(f.exists()) return false;
		log.info("Creating db4o db: " + f.getName());
		f = null;
		ObjectContainer db = null;
		try {
			db = instantiateObjectContainer();
		}
		finally {
			if(db != null) db.close();
		}
		return true;
	}

	@Override
	public boolean delete() {
		final File f = getHandle();
		if(!f.exists()) return false;
		log.info("Deleting db4o db: " + f.getName());
		if(!f.delete()) throw new IllegalStateException("Unable to delete db4o file: " + f.getAbsolutePath());
		return true;
	}

	@Override
	public void restub() {
		final File f = getHandle();
		if(f.exists()) {
			delete();
		}
		create();
		stub();
	}

	@Override
	public boolean stub() {
		if(populator == null) throw new IllegalStateException("No populator set");
		ObjectContainer db = null;
		try {
			log.info("Stubbing db4o db: " + dbFile.getPath());
			db = instantiateObjectContainer();
			populator.populateEntityGraph();
			final EntityGraph eg = populator.getEntityGraph();
			final Iterator<Class<? extends IEntity>> itr = eg.getEntityTypes();
			while(itr.hasNext()) {
				final Class<? extends IEntity> et = itr.next();
				log.info("Storing entities of type: " + et.getSimpleName() + "...");
				final Collection<? extends IEntity> ec = eg.getEntitiesByType(et);
				for(final IEntity e : ec) {
					db.store(e);
				}
			}
			return true;
		}
		catch(final Throwable t) {
			return false;
		}
		finally {
			if(db != null) db.close();
		}
	}
}
