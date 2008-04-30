/**
 * The Logic Lab
 * @author jpk
 * Aug 29, 2007
 */
package com.tll.client.data;

import com.tll.client.IMarshalable;

/**
 * Payload - Common container with which to send data to client.
 * @author jpk
 */
public class Payload implements IMarshalable {

	protected Status status;

	/**
	 * Constructor
	 */
	public Payload() {
		super();
		this.status = new Status();
	}

	/**
	 * Constructor
	 * @param status
	 */
	public Payload(Status status) {
		super();
		this.status = status;
	}

	public String descriptor() {
		return "Payload";
	}

	public final Status getStatus() {
		return status;
	}

	public final boolean hasErrors() {
		return status == null ? false : status.hasErrors();
	}
}
