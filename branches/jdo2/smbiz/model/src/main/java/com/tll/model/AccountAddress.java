package com.tll.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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
public class AccountAddress extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 7356724207827323290L;

	public static final int MAXLEN_NAME = 32;

	private Account account;

	private Address address;

	private AddressType type;

	public Class<? extends IEntity> entityClass() {
		return AccountAddress.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
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

	/**
	 * @return Returns the address.
	 */
	@NotNull
	@Valid
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
	 * @return the type
	 */
	@NotNull
	public AddressType getType() {
		return type;
	}

	public void setType(AddressType type) {
		this.type = type;
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public long accountId() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return -1;
		}
	}

	public long addressId() {
		try {
			return getAddress().getId();
		}
		catch(final NullPointerException npe) {
			return -1;
		}
	}
}
