package com.tll.model.impl;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.IChildEntity;
import com.tll.model.NamedTimeStampEntity;
import com.tll.model.validate.AtLeastOne;
import com.tll.model.validate.BusinessKeyUniqueness;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "account")
/**
 * Account entity
 * @author jpk
 */
public abstract class Account extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	static final String ASP_VALUE = "0";
	static final String ISP_VALUE = "1";
	static final String MERCHANT_VALUE = "2";
	static final String CUSTOMER_VALUE = "3";

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_BILLING_MODEL = 32;
	public static final int MAXLEN_BILLING_CYCLE = 32;

	protected Account parent;

	protected AccountStatus status;

	protected boolean persistPymntInfo = false;

	protected String billingModel;

	protected String billingCycle;

	protected Date dateLastCharged;

	protected Date nextChargeDate;

	protected Date dateCancelled;

	protected PaymentInfo paymentInfo;

	protected Currency currency;

	protected Set<AccountAddress> addresses = new LinkedHashSet<AccountAddress>(3);

	/**
	 * Constructor
	 */
	public Account() {
		super();
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_aid")
	public Account getParent() {
		return parent;
	}

	public void setParent(Account parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the persistPymntInfo.
	 */
	@Column(name = "persist_pymnt_info")
	public boolean getPersistPymntInfo() {
		return persistPymntInfo;
	}

	/**
	 * @param persistPymntInfo The persistPymntInfo to set.
	 */
	public void setPersistPymntInfo(boolean persistPymntInfo) {
		this.persistPymntInfo = persistPymntInfo;
	}

	/**
	 * @return Returns the billingCycle.
	 */
	@Column(name = "billing_cycle")
	@NotNull
	@Length(max = MAXLEN_BILLING_CYCLE)
	public String getBillingCycle() {
		return billingCycle;
	}

	/**
	 * @param billingCycle The billingCycle to set.
	 */
	public void setBillingCycle(String billingCycle) {
		this.billingCycle = billingCycle;
	}

	/**
	 * @return Returns the billingModel.
	 */
	@Column(name = "billing_model")
	@NotNull
	@Length(max = MAXLEN_BILLING_MODEL)
	public String getBillingModel() {
		return billingModel;
	}

	/**
	 * @param billingModel The billingModel to set.
	 */
	public void setBillingModel(String billingModel) {
		this.billingModel = billingModel;
	}

	/**
	 * @return Returns the currency.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cur_id")
	@NotNull
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * @param currency The currency to set.
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * @return Returns the dateCancelled.
	 */
	@Column(name = "date_cancelled")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateCancelled() {
		return dateCancelled;
	}

	/**
	 * @param dateCancelled The dateCancelled to set.
	 */
	public void setDateCancelled(Date dateCancelled) {
		this.dateCancelled = dateCancelled;
	}

	/**
	 * @return Returns the dateLastCharged.
	 */
	@Column(name = "date_last_charged")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateLastCharged() {
		return dateLastCharged;
	}

	/**
	 * @param dateLastCharged The dateLastCharged to set.
	 */
	public void setDateLastCharged(Date dateLastCharged) {
		this.dateLastCharged = dateLastCharged;
	}

	/**
	 * @return Returns the nextChargeDate.
	 */
	@Column(name = "next_charge_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getNextChargeDate() {
		return nextChargeDate;
	}

	/**
	 * @param nextChargeDate The nextChargeDate to set.
	 */
	public void setNextChargeDate(Date nextChargeDate) {
		this.nextChargeDate = nextChargeDate;
	}

	/**
	 * @return Returns the paymentInfo.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pi_id")
	@Valid
	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	/**
	 * @param paymentInfo The paymentInfo to set.
	 */
	public void setPaymentInfo(PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	/**
	 * @return Returns the status.
	 */
	@Column(nullable = false)
	@NotNull
	public AccountStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the addresses.
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@AtLeastOne(type = "account address")
	@BusinessKeyUniqueness(type = "account address")
	@Valid
	public Set<AccountAddress> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses The addresses to set.
	 */
	public void setAddresses(Set<AccountAddress> addresses) {
		this.addresses = addresses;
	}

	@Transient
	public AccountAddress getAccountAddress(int id) {
		return findEntityInCollection(this.addresses, id);
	}

	@Transient
	public AccountAddress getAccountAddress(String name) {
		return findNamedEntityInCollection(this.addresses, name);
	}

	@Transient
	public void addAccountAddress(AccountAddress e) {
		addEntityToCollection(addresses, e);
	}

	@Transient
	public void addAccountAddresses(Collection<AccountAddress> clctn) {
		addEntitiesToCollection(clctn, addresses);
	}

	@Transient
	public void removeAccountAddresses() {
		clearEntityCollection(addresses);
	}

	@Transient
	public void removeAccountAddress(AccountAddress e) {
		removeEntityFromCollection(addresses, e);
	}

	@Transient
	public int getNumAccountAddresses() {
		return getCollectionSize(addresses);
	}

	public Integer accountId() {
		return super.getId();
	}
}