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
public class AddAccountRequest extends EntityModelRequest {

	private Model account;
	private Collection<Model> accountInterfaceOptions;
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
	 * @param accountInterfaceOptions
	 * @param users
	 */
	public AddAccountRequest(Model account, Collection<Model> accountInterfaceOptions, Collection<Model> users) {
		super();
		this.account = account;
		this.accountInterfaceOptions = accountInterfaceOptions;
		this.users = users;
	}

	@Override
	public String descriptor() {
		return account == null ? "Add account request" : "Add " + account + " request";
	}

	@Override
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
	 * @return the accountInterfaceOptions
	 */
	public Collection<Model> getAccountInterfaceOptions() {
		return accountInterfaceOptions;
	}

	/**
	 * @return the users
	 */
	public Collection<Model> getUsers() {
		return users;
	}

}
