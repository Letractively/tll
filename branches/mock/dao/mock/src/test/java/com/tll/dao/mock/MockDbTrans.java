/**
 * 
 */
package com.tll.dao.mock;

import com.tll.dao.IDbTrans;


/**
 * MockDbTrans - {@link IDbTrans} impl for the mock dao impl.
 * @author jpk
 */
public final class MockDbTrans implements IDbTrans {
	
	private boolean started, complete;

	@Override
	public void endTrans() throws IllegalStateException {
		if(!started) {
			throw new IllegalStateException("No trans started.");
		}
		if(complete) {
			
		}
		else {
			
		}
		started = complete = false;
	}

	@Override
	public boolean isTransStarted() {
		return started;
	}

	@Override
	public void setComplete() throws IllegalStateException {
		if(!started) {
			throw new IllegalStateException("No trans started.");
		}
		complete = true;
	}

	@Override
	public void startTrans() throws IllegalStateException {
		if(started) {
			throw new IllegalStateException("Trans already started.");
		}
		this.started = true;
	}

}
