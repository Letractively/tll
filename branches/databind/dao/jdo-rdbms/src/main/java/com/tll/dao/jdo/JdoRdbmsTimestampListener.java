/**
 * The Logic Lab
 * @author jpk
 * @since Nov 8, 2009
 */
package com.tll.dao.jdo;

import java.util.Date;

import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.StoreLifecycleListener;

import com.tll.model.ITimeStampEntity;

/**
 * JdoRdbmsTimestampListener - Applies timestamping to {@link ITimeStampEntity}
 * instances before they are persisted.
 * @author jpk
 */
public class JdoRdbmsTimestampListener implements StoreLifecycleListener {

	@Override
	public void preStore(InstanceLifecycleEvent event) {
		final Object obj = event.getSource();
		assert obj instanceof ITimeStampEntity;
		final Date now = new Date();
		final ITimeStampEntity tse = (ITimeStampEntity) obj;
		if(tse.isNew()) tse.setDateCreated(now);
		tse.setDateModified(now);
	}

	@Override
	public void postStore(InstanceLifecycleEvent event) {
		// no-op
	}

}
