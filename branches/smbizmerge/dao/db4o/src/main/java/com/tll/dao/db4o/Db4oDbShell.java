/**
 * The Logic Lab
 * @author jpk
 * @since Aug 29, 2009
 */
package com.tll.dao.db4o;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.config.Config;
import com.tll.dao.IDbShell;
import com.tll.model.egraph.EntityGraph;
import com.tll.model.egraph.IEntityGraphPopulator;

/**
 * @author jpk
 */
public class Db4oDbShell implements IDbShell {

	private static final Logger log = LoggerFactory.getLogger(Db4oDbShell.class);

	private final URI dbFile;

	private final IEntityGraphPopulator populator;

	private final Provider<EmbeddedConfiguration> c;

	public static final String DEFAULT_DB4O_FILENAME = "db4o";

	/**
	 * Non-robust way to clear all objects held in db4o's object container. This
	 * method makes no guarantees as to correctness.
	 * <p>
	 * <b>WARNING:</b>Use at your own risk.
	 * @param container
	 */
	public static void clearData(EmbeddedObjectContainer container) {
		ObjectSet<Object> set = container.queryByExample(null);
		if(set != null) {
			for(Object obj : set) {
				container.delete(obj);
			}
		}
	}

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
			while(!session.close()) {
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
			final Iterator<Class<?>> itr = eg.getEntityTypes();
			while(itr.hasNext()) {
				final Class<?> et = itr.next();
				log.info("Storing entities of type: " + et.getSimpleName() + "...");
				final Collection<?> ec = eg.getEntitiesByType(et);
				for(final Object e : ec) {
					log.info("Storing entity: " + e + " ...");
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
			log.info("Stubbing db4o db: " + dbFile);
			dbSession = createDbSession();
			addData(dbSession);
		}
		finally {
			killDbSession(dbSession);
		}
	}

	/**
	 * Resolves the full db4o file path following these rules in order of priority:
	 * <ol>
	 * <li>If the config {@link Db4oConfigKeys#DB4O_FILEREF} property value is present, it is considered the
	 * full absolute path
	 * <li>else if config {@link Db4oConfigKeys#DB4O_FILENAME} property value is present, it is appended
	 * to the classpath root to establist the full path
	 * <li>else the {@link DEFAULT_DB4O_FILENAME} is employed at the classpath
	 * root to establish the full path
	 * </ol>
	 * <br><b>NOTE</b> The db4o file isn't required to exist for the proper function of this method.
	 * @param config
	 * @return never null URI pointing to the db4o file existant or not.
	 */
	public static URI resolveDb4oFileLocationFromConfig(Config config) {
		if(config == null) return getDb4oClasspathFileRef(null);
		
		String fileref = config.getString(Db4oConfigKeys.DB4O_FILEREF.getKey());
		if(fileref != null) {
			File f = new File(fileref);
			return f.toURI();
		}
		
		return getDb4oClasspathFileRef(config.getString(Db4oConfigKeys.DB4O_FILENAME.getKey()));
	}

	/**
	 * Provides a non-null {@link URI} pointing to the given filename even if the
	 * file doesn't exist based on the root classpath location.
	 * <p>
	 * IMPT: this method gives "undefined" results if a path is contained in the
	 * filename argument.
	 * @param filename the non-path filename
	 * @return the corresponding URI
	 */
	public static URI getDb4oClasspathFileRef(String filename) {
		if(filename == null) filename = Db4oDbShell.DEFAULT_DB4O_FILENAME;
		try {
			// first attempt to load existing file
			URL url = Db4oDbShell.class.getClassLoader().getResource(filename);
			URI uri = url == null ? null : url.toURI();
			if(uri == null) {
				url = Db4oDbShell.class.getClassLoader().getResource("");
				String npath = url.getPath() + '/' + filename;
				final File f = new File(npath);
				log.info("Db4o db file: {} does not exist.", f.getPath());
				uri = f.toURI();
			}
			return uri;
		}
		catch(final URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}
}
