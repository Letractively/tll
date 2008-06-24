package com.tll.model.impl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.NamedTimeStampEntity;
import com.tll.model.key.BusinessKeyDefinition;
import com.tll.model.key.IBusinessKeyDefinition;

/**
 * The account address entity holding a refs to a single account and single
 * address.
 * @author jpk
 */
@Entity
@Table(name = "account_address")
public class AccountAddress extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 7356724207827323290L;

	public static final IBusinessKeyDefinition BinderBk =
			new BusinessKeyDefinition(AccountHistory.class, "Binder", new String[] {
				"account.id",
				"address.id" });

	public static final IBusinessKeyDefinition NameBk =
			new BusinessKeyDefinition(AccountHistory.class, "Account Id and Name", new String[] {
				"account.id",
				"name" });

	public static final int MAXLEN_NAME = 32;

	private Account account;

	private Address address;

	private AddressType type;

	public Class<? extends IEntity> entityClass() {
		return AccountAddress.class;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the account.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aid")
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
	@ManyToOne(fetch = FetchType.EAGER, cascade = {
		CascadeType.MERGE,
		CascadeType.PERSIST })
	@JoinColumn(name = "address_id")
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
	@Column(name = "type")
	@NotNull
	public AddressType getType() {
		return type;
	}

	public void setType(AddressType type) {
		this.type = type;
	}

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("type", type).append("account",
				account == null ? "NULL" : account.descriptor()).append("address",
				address == null ? "NULL" : address.descriptor());
	}

	@Transient
	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public Integer accountId() {
		try {
			return getAccount().getId();
		}
		catch(NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}

}
