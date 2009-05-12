/**
 * The Logic Lab
 * @author jpk
 * Aug 24, 2007
 */
package com.tll.client;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;
import com.tll.model.AdminRole;

/**
 * AdminContext - Marshalable object definining key environment and logged in
 * user info.
 * @author jpk
 */
public final class AdminContext {

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
	 * The user's admin role which dictates functionality which is lazily
	 * computed.
	 */
	private transient AdminRole userRole;

	/**
	 * Constructor
	 */
	public AdminContext() {
		super();
	}

	/**
	 * @return <code>true</code> if we are in debug mode.
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return The server environment String token as defined in the app
	 *         configuration.
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * @return The currently logged in user
	 */
	public Model getUser() {
		return user;
	}

	/**
	 * Sets the current user.
	 * @param user
	 */
	public void setUser(Model user) {
		this.user = user;
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

	/**
	 * @return The current user's parent account.
	 */
	public Model getUserAccount() {
		assert user != null;
		try {
			return user.relatedOne("account").getModel();
		}
		catch(final PropertyPathException e) {
			return null;
		}
	}

	public AdminRole getUserRole() {
		if(userRole == null) {
			userRole = AdminRole.VISITOR; // default
			try {
				final List<Model> auths = user.relatedMany("authoritys").getList();
				if(auths != null) {
					int ordinal, highest = -1;
					for(final Model auth : auths) {
						final String role = auth.asString("authority");
						if(AdminRole.ASP.name().equals(role)) {
							ordinal = AdminRole.ASP.ordinal();
						}
						else if(AdminRole.ISP.name().equals(role)) {
							ordinal = AdminRole.ISP.ordinal();
						}
						else if(AdminRole.MERCHANT.name().equals(role)) {
							ordinal = AdminRole.MERCHANT.ordinal();
						}
						else if(AdminRole.CUSTOMER.name().equals(role)) {
							ordinal = AdminRole.CUSTOMER.ordinal();
						}
						else {
							ordinal = AdminRole.VISITOR.ordinal();
						}
						if(ordinal > highest) highest = ordinal;
					}
					userRole = AdminRole.values()[highest];
				}
			}
			catch(final PropertyPathException e) {
				Log.error(e.getMessage());
				userRole = AdminRole.VISITOR;
			}
		}
		return userRole;
	}
}
