/**
 * The Logic Lab
 * @author jpk
 * Sep 3, 2007
 */
package com.tll.common.admin.data.rpc;

import com.tll.common.admin.AdminContext;
import com.tll.common.data.Payload;
import com.tll.common.data.Status;

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
	 * @param status
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
