/*
 * The Logic Lab 
 */
package com.tll.dao.hibernate.event;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.ejb.event.EJB3PersistEventListener;
import org.hibernate.event.EventSource;

import com.tll.model.ITimeStampEntity;

/**
 * CreateEventListener
 * @author jpk
 */
public class CreateEventListener extends EJB3PersistEventListener {
	private static final long serialVersionUID = 6111417850398392720L;

	@Override
	protected Serializable saveWithGeneratedId(Object entity, String entityName, Object anything, EventSource source,
			boolean requiresImmediateIdAccess) {
		
		// set date created
		if(entity instanceof ITimeStampEntity) {
			((ITimeStampEntity) entity).setDateCreated(new Date());
		}
		return super.saveWithGeneratedId(entity, entityName, anything, source, requiresImmediateIdAccess);
	}

}
