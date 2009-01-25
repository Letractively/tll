/*
 * The Logic Lab 
 */
package com.tll.dao.hibernate.event;

import java.util.Date;

import org.hibernate.ejb.event.EJB3SaveEventListener;
import org.hibernate.event.SaveOrUpdateEvent;

import com.tll.model.ITimeStampEntity;

/**
 * UpdateEventListener
 * @author jpk
 */
public class UpdateEventListener extends EJB3SaveEventListener {
	private static final long serialVersionUID = -2855104149870296942L;

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		// set date modified
		if(event.getObject() instanceof ITimeStampEntity) {
			((ITimeStampEntity) event.getObject()).setDateModified(new Date());
		}
		super.onSaveOrUpdate(event);
	}
}
