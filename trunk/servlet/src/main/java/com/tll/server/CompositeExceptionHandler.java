/**
 * The Logic Lab
 * @author jkirton
 * @since Mar 21, 2011
 */
package com.tll.server;


/**
 * A way to chain exception handlers together masking as one.
 * @author jkirton
 */
public class CompositeExceptionHandler implements IExceptionHandler {

	private final IExceptionHandler[] handlers;

	/**
	 * Constructor
	 * @param handlers
	 */
	public CompositeExceptionHandler(IExceptionHandler... handlers) {
		super();
		if(handlers == null || handlers.length < 1) throw new IllegalArgumentException();
		this.handlers = handlers;
	}

	@Override
	public void handleException(Throwable t) {
		for(IExceptionHandler h : handlers) {
			h.handleException(t);
		}
	}

}
