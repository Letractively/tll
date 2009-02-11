/**
 * The Logic Lab
 * @author jpk
 * Feb 10, 2009
 */
package com.tll.server.rpc.entity;

import javax.servlet.ServletContext;

import com.tll.mail.MailManager;
import com.tll.model.IEntityFactory;
import com.tll.refdata.RefData;
import com.tll.server.marshal.Marshaler;
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
	 * @return The marshaler.
	 */
	Marshaler getMarshaler();

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
}
