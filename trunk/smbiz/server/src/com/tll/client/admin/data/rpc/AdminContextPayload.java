/**
 * The Logic Lab
 * @author jpk
 * Sep 3, 2007
 */
package com.tll.client.admin.data.rpc;

import com.tll.client.admin.AdminContext;
import com.tll.client.data.Payload;
import com.tll.client.data.Status;

/**
 * AdminContextPayload
 * @author jpk
 */
public class AdminContextPayload extends Payload {

	private AdminContext adminContext;

	/**
	 * Constructor
	 */
	public AdminContextPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param adminContext
	 */
	public AdminContextPayload(Status status, AdminContext adminContext) {
		super(status);
		this.adminContext = adminContext;
	}

	public AdminContext getAdminContext() {
		return adminContext;
	}

}
