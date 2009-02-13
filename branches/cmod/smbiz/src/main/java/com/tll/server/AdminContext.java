/**
 * The Logic Lab
 * @author jpk
 * Aug 24, 2007
 */
package com.tll.server;

import com.tll.model.Account;
import com.tll.model.User;

/**
 * AdminContext
 * @author jpk
 */
public final class AdminContext {

	/**
	 * The logged in user.
	 */
	private User user;
	
	/**
	 * The current selected account.
	 */
	private Account account;

	public AdminContext() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}


}
