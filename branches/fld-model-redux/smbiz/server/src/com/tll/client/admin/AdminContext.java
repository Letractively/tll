/**
 * The Logic Lab
 * @author jpk
 * Aug 24, 2007
 */
package com.tll.client.admin;

import com.tll.client.IMarshalable;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPathException;

/**
 * AdminContext - Marshalable object definining key environment and logged in
 * user info.
 * @author jpk
 */
public final class AdminContext implements IMarshalable {

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
	public AdminContext() {
		super();
	}

	/**
	 * Constructor
	 * @param debug In debug mode?
	 * @param environment The server environment
	 * @param user The logged in user
	 * @param account The initial account to set as current usu. the account of
	 *        the logged in user but as a DISTINCT reference!
	 */
	public AdminContext(boolean debug, String environment, Model user, Model account) {
		super();
		this.debug = debug;
		assert environment != null;
		this.environment = environment;
		assert user != null;
		this.user = user;
		this.account = account;
	}

	/**
	 * @return <code>true</code> if we are in debug mode.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @return The server environment String token as defined in the app
	 *         configuration.
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @return The currently logged in user
	 */
	public Model getUser() {
		return user;
	}

	/**
	 * @return The current user's parent account.
	 */
	public Model getUserAccount() {
		assert user != null;
		try {
			return user.relatedOne("account").getModel();
		}
		catch(PropertyPathException e) {
			return null;
		}
	}

	/**
	 * @return The current account. This property is able to be set in the client
	 *         context.
	 */
	public Model getAccount() {
		return account;
	}

	/**
	 * Sets the current account
	 * @param account The account to set as current
	 */
	public void setAccount(Model account) {
		this.account = account;
	}
}
