/**
 * The Logic Lab
 * @author jpk
 * @since Jul 3, 2009
 */
package com.tll.db;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.google.inject.Inject;
import com.tll.model.EntityGraph;
import com.tll.model.IEntity;
import com.tll.model.IEntityGraphBuilder;

/**
 * Db4oDbShell - Interfaces w/ a local (file based) db4o db using the
 * <em>native db40 api</em>.
 * @author jpk
 */
public class Db4oDbShell implements IDbShell {

	private static final Log log = LogFactory.getLog(Db4oDbShell.class);

	private final File dbFile;
	private final IEntityGraphBuilder egb;

	/**
	 * Constructor
	 * @param dbFile A ref to the db file
	 * @param egb The entity graph builder that defines the db "schema" and
	 *        content.
	 */
	@Inject
	public Db4oDbShell(File dbFile, IEntityGraphBuilder egb) {
		super();
		this.dbFile = dbFile;
		this.egb = egb;
	}

	@Override
	public boolean clear() {
		if(dbFile.exists()) {
			dbFile.delete();
			create();
			return true;
		}
		return false;
	}

	@Override
	public boolean create() {
		if(dbFile.exists()) return false;
		final ObjectContainer db = Db4o.openFile(dbFile.getPath());
		db.close();
		return true;
	}

	@Override
	public boolean delete() {
		if(!dbFile.exists()) return false;
		dbFile.delete();
		return true;
	}

	@Override
	public void restub() {
		if(dbFile.exists()) {
			delete();
		}
		create();
		stub();
	}

	@Override
	public boolean stub() {
		final ObjectContainer db = Db4o.openFile(dbFile.getPath());
		try {
			final EntityGraph eg = egb.buildEntityGraph();
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
			db.close();
		}
	}
}
