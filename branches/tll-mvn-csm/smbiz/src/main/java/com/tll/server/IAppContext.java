/**
 * The Logic Lab
 * @author jpk
 * Jan 30, 2009
 */
package com.tll.server;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AuthenticationManager;

import com.tll.dao.DaoMode;
import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * IAppContext
 * @author jpk
 */
public interface IAppContext {

	/**
	 * The key identifying the sole {@link AppContext} in the
	 * {@link ServletContext}.
	 */
	static final String SERVLET_CONTEXT_KEY = AppContext.class.getName();

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
	IEntityFactory getEntityFactory();

	/**
	 * @return The sole {@link IEntityServiceFactory} instance.
	 */
	IEntityServiceFactory getEntityServiceFactory();

	/**
	 * @return The application ref data.
	 */
	RefData getAppRefData();

	/**
	 * @return The mail manager.
	 */
	MailManager getMailManager();

	/**
	 * @return The marshaler.
	 */
	Marshaler getMarshaler();

	/**
	 * @return the security mode of the app.
	 */
	SecurityMode getSecurityMode();
	
	/**
	 * @return The dao mode of the app.
	 */
	DaoMode getDaoMode();

	/**
	 * @return The security related authentication manager.
	 */
	AuthenticationManager getAuthenticationManager();
	
	/**
	 * @return The http request access decision manager.
	 */
	AccessDecisionManager getHttpRequestAccessDecisionManager();
}