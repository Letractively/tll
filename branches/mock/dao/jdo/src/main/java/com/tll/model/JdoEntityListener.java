/**
 * The Logic Lab
 * @author jpk
 * @since Jul 17, 2009
 */
package com.tll.model;

import java.util.Date;

import javax.jdo.listener.CreateLifecycleListener;
import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.StoreLifecycleListener;

/**
 * JdoEntityListener - Handles life-cycle events relating to jdo persist events
 * decorating entities w/ addidional information.
 * @author jpk
 */
public class JdoEntityListener implements CreateLifecycleListener, StoreLifecycleListener {

	@Override
	public void postCreate(InstanceLifecycleEvent event) {
		final Object obj = event.getSource();
		if(obj instanceof EntityBase) {
			((EntityBase) obj).setNew(false);
		}
	}

	@Override
	public void postStore(InstanceLifecycleEvent event) {
		// no-op
	}

	@Override
	public void preStore(InstanceLifecycleEvent event) {
		// handle timestamping for time stamp related entities
		final Object obj = event.getSource();
		if(obj instanceof ITimeStampEntity) {
			final ITimeStampEntity tse = (ITimeStampEntity) obj;
			final Date now = new Date();
			tse.setDateModified(now);
			if(tse.getDateCreated() == null) {
				tse.setDateCreated(now);
			}
		}
	}

}
