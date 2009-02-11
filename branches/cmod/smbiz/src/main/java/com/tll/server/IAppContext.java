/**
 * The Logic Lab
 * @author jpk
 * Jan 30, 2009
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import com.tll.dao.DaoMode;

/**
 * IAppContext
 * @author jpk
 */
public interface IAppContext {

	/**
	 * The key identifying the sole {@link IAppContext} in the
	 * {@link ServletContext}.
	 */
	static final String SERVLET_CONTEXT_KEY = IAppContext.class.getName();

	/**
	 * @return true/false
	 */
	boolean isDebug();

	/**
	 * @return A token identifying the environment.
	 */
	String getEnvironment();
	
	/**
	 * @return The sole {@link EntityManagerFactory} instance.
	 */
	EntityManagerFactory getEntityManagerFactory();

	/**
	 * @return The sole {@link IEntityFactory}.
	 */
	//IEntityFactory getEntityFactory();

	/**
	 * @return The dao mode of the app.
	 */
	DaoMode getDaoMode();
}