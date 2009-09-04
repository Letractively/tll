package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * The account address entity holding a refs to a single account and single
 * address.
 * @author jpk
 */
@PersistenceCapable(detachable = "true")
@Uniques(value = {
	@Unique(name = "Account Id and Address Id", members = { "account.id", "address.id" }), 
	@Unique(name = "Account Id and Name", members = { "account.id", INamedEntity.NAME }) 
})
public class AccountAddress extends NamedTimeStampEntity implements IChildEntity<Account> {

	private static final long serialVersionUID = -6901864365658192141L;

	public static final int MAXLEN_NAME = 32;

	@Persistent(defaultFetchGroup = "true")
	private Account account;

	@Persistent(defaultFetchGroup = "true")
	private Address address;

	public Class<? extends IEntity> entityClass() {
		return AccountAddress.class;
	}

	@Override
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@NotNull
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}
}
