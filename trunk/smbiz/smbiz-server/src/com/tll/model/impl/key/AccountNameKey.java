package com.tll.model.impl.key;

import com.tll.model.impl.Account;
import com.tll.model.key.NameKey;

public final class AccountNameKey extends NameKey<Account> {

	private static final long serialVersionUID = 8690815058857176251L;

	/**
	 * Constructor
	 */
	public AccountNameKey() {
		super(Account.class);
	}

	/**
	 * Constructor
	 * @param name
	 */
	public AccountNameKey(String name) {
		this();
		setName(name);
	}

}