/*
 * The Logic Lab 
 */
package com.tll.dao.hibernate.event;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.ejb.event.EJB3SaveEventListener;
import org.hibernate.event.EventSource;

import com.tll.model.ITimeStampEntity;

/**
 * UpdateEventListener
 * @author jpk
 */
public class UpdateEventListener extends EJB3SaveEventListener {
	private static final long serialVersionUID = -2855104149870296942L;

	@Override
	protected Serializable saveWithGeneratedId(Object entity, String entityName, Object anything, EventSource source,
			boolean requiresImmediateIdAccess) {
		
		// set date modified
		if(entity instanceof ITimeStampEntity) {
			((ITimeStampEntity) entity).setDateModified(new Date());
		}
		return super.saveWithGeneratedId(entity, entityName, anything, source, requiresImmediateIdAccess);
	}

}
