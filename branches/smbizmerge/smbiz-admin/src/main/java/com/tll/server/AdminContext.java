/**
 * The Logic Lab
 * @author jpk
 * Aug 24, 2007
 */
package com.tll.server;

import com.tll.model.User;

/**
 * Session scoped smbiz admin context.
 * @author jpk
 */
public final class AdminContext {

	/**
	 * A unique token to serve as a pointer to an instance of this type.
	 */
	public static final String KEY = AdminContext.class.getName();

	/**
	 * The logged in user.
	 */
	private User user;
	
	public AdminContext() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
