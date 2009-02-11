/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.entity;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletContext;

import com.tll.dao.DaoMode;
import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
import com.tll.server.rpc.ExceptionHandler;
import com.tll.service.entity.IEntityServiceFactory;


/**
 * IMEntityServiceContext
 * @author jpk
 */
public interface IMEntityServiceContext {

	/**
	 * The key identifying the {@link IMEntityServiceContext} in the
	 * {@link ServletContext}.
	 */
	static final String SERVLET_CONTEXT_KEY = IMEntityServiceContext.class.getName();
	
	/**
	 * @return The dao mode.
	 */
	DaoMode getDaoMode();

	/**
	 * @return The marshaler.
	 */
	Marshaler getMarshaler();
	
	/**
	 * @return The {@link EntityManagerFactory}.
	 */
	EntityManagerFactory getEntityManagerFactory();

	/**
	 * @return The entity service factory.
	 */
	IEntityServiceFactory getEntityServiceFactory();
	
	/**
	 * @return The entity factory.
	 */
	IEntityFactory getEntityFactory();

	/**
	 * @return The ref data provider.
	 */
	RefData getRefData();

	/**
	 * @return The mail manager
	 */
	MailManager getMailManager();
	
	/**
	 * @return The {@link INamedQueryResolver}.
	 */
	INamedQueryResolver getQueryResolver();

	/**
	 * @return The {@link IMEntityServiceImplResolver}.
	 */
	IMEntityServiceImplResolver getServiceResolver();

	/**
	 * @return The exception handler
	 */
	ExceptionHandler getExceptionHandler();
}
