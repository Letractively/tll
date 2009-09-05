package com.tll.model;

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
@BusinessObject(businessKeys = {
	@BusinessKeyDef(name = "Account Id and Address Id", properties = { "account.id", "address.id" }), 
	@BusinessKeyDef(name = "Account Id and Name", properties = { "account.id", INamedEntity.NAME }) 
})
public class AccountAddress extends NamedTimeStampEntity implements IChildEntity<Account> {

	private static final long serialVersionUID = -6901864365658192141L;

	public static final int MAXLEN_NAME = 32;

	private Account account;

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