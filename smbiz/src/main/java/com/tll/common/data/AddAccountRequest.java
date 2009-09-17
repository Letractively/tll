/**
 * The Logic Lab
 * @author jpk
 * Mar 1, 2008
 */
package com.tll.common.data;

import java.util.Collection;

import com.tll.common.model.IEntityType;
import com.tll.common.model.Model;

/**
 * AddAccountRequest
 * @author jpk
 */
public class AddAccountRequest extends ModelRequest {

	private Model account;
	private Collection<Model> accountInterfaces;
	private Collection<Model> users;

	/**
	 * Constructor
	 */
	public AddAccountRequest() {
		super();
	}

	/**
	 * Constructor
	 * @param account
	 * @param accountInterfaces
	 * @param users
	 */
	public AddAccountRequest(Model account, Collection<Model> accountInterfaces, Collection<Model> users) {
		super();
		this.account = account;
		this.accountInterfaces = accountInterfaces;
		this.users = users;
	}

	@Override
	public String descriptor() {
		return account == null ? "Add account request" : "Add " + account + " request";
	}

	public IEntityType getEntityType() {
		return account.getEntityType();
	}

	/**
	 * @return the account
	 */
	public Model getAccount() {
		return account;
	}

	/**
	 * @return the accountInterfaces
	 */
	public Collection<Model> getAccountInterfaces() {
		return accountInterfaces;
	}

	/**
	 * @return the users
	 */
	public Collection<Model> getUsers() {
		return users;
	}

}
