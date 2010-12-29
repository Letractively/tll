/**
 * The Logic Lab
 * @author jpk
 * Sep 3, 2007
 */
package com.tll.common.data.rpc;

import com.tll.common.data.Payload;
import com.tll.common.model.Model;
import com.tll.common.msg.Status;

/**
 * AdminContextPayload - Payload for initializing the client-side admin
 * context.
 * @author jpk
 */
public class AdminContextPayload extends Payload {

	/**
	 * debug config param
	 */
	private boolean debug;

	/**
	 * environment config param
	 */
	private String environment;

	/**
	 * The logged in user or the user for this http session.
	 */
	private Model user;

	/**
	 * The current account which is distinct from the logged in user.
	 */
	private Model account;

	/**
	 * Constructor
	 */
	public AdminContextPayload() {
		super();
	}

	/**
	 * Constructor
	 * @param status
	 * @param debug
	 * @param environment
	 * @param user
	 * @param account
	 */
	public AdminContextPayload(Status status, boolean debug, String environment, Model user, Model account) {
		super(status);
		this.debug = debug;
		this.environment = environment;
		this.user = user;
		this.account = account;
	}

	/**
	 * Constructor
	 * @param status
	 * @param account
	 */
	public AdminContextPayload(Status status, Model account) {
		super(status);
		this.account = account;
	}

	/**
	 * @return the debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @return the user
	 */
	public Model getUser() {
		return user;
	}

	/**
	 * @return the account
	 */
	public Model getAccount() {
		return account;
	}

}
