package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * The account address entity holding a refs to a single account and single
 * address.
 * @author jpk
 */
@PersistenceCapable
@BusinessObject(businessKeys = {
	@BusinessKeyDef(name = "Account Id and Address Id", properties = { "account.id", "address.id" }),
	@BusinessKeyDef(name = "Account Id and Name", properties = { "account.id", INamedEntity.NAME })
})
public class AccountAddress extends NamedTimeStampEntity implements IChildEntity<Account> {
	private static final long serialVersionUID = -6901864365658192141L;

	public static final int MAXLEN_NAME = 32;

	@Persistent
	private Account account;

	@Persistent
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

	/**
	 * @return Returns the account.
	 */
	@NotNull
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account The account to set.
	 */
	public void setAccount(Account account) {
		this.account = account;
	}

	public Address getAddress() {
		return address;
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}
}
