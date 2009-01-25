package com.tll.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * The account address entity holding a refs to a single account and single
 * address.
 * @author jpk
 */
@Entity
@Table(name = "account_address")
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
		CascadeType.MERGE, CascadeType.PERSIST })
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

	@Transient
	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}
}
