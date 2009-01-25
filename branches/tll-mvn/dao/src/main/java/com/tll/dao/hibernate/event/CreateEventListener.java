/*
 * The Logic Lab 
 */
package com.tll.dao.hibernate.event;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.ejb.event.EJB3PersistEventListener;
import org.hibernate.event.PersistEvent;

import com.tll.model.ITimeStampEntity;

/**
 * CreateEventListener
 * @author jpk
 */
public class CreateEventListener extends EJB3PersistEventListener {
	private static final long serialVersionUID = 6111417850398392720L;

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		// set date created
		if(event.getObject() instanceof ITimeStampEntity) {
			((ITimeStampEntity) event.getObject()).setDateCreated(new Date());
		}
		super.onPersist(event);
	}

}
