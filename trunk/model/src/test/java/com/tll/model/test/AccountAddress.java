package com.tll.model.test;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NamedTimeStampEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * The account address entity holding a refs to a single account and single
 * address.
 * @author jpk
 */
@BusinessObject(businessKeys = {
	@BusinessKeyDef(name = "Account Id and Address Id", properties = {
		"account.id", "address.id"
	}), @BusinessKeyDef(name = "Account Id and Name", properties = {
		"account.id", INamedEntity.NAME
	})
})
public class AccountAddress extends NamedTimeStampEntity implements IChildEntity<Account> {

	private static final long serialVersionUID = -6901864365658192141L;

	public static final int MAXLEN_NAME = 32;

	private Account account;

	private Address address;

	private AddressType type;

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
	 * @return the type
	 */
	public AddressType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(AddressType type) {
		this.type = type;
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
