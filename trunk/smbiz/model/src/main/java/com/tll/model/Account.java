package com.tll.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.validate.AtLeastOne;
import com.tll.model.validate.BusinessKeyUniqueness;
import com.tll.schema.BusinessKeyDef;
import com.tll.schema.BusinessObject;
import com.tll.schema.Reference;
import com.tll.schema.Root;

/**
 * Account - Base class for account type entities.
 * @author jpk
 */
@Root
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = INamedEntity.NAME))
public abstract class Account extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -684966689440840694L;

	static final String ASP_VALUE = "0";
	static final String ISP_VALUE = "1";
	static final String MERCHANT_VALUE = "2";
	static final String CUSTOMER_VALUE = "3";

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_BILLING_MODEL = 32;
	public static final int MAXLEN_BILLING_CYCLE = 32;

	protected Account parent;

	protected AccountStatus status;

	protected boolean persistPymntInfo;

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

	@Override
	public final Class<? extends IEntity> rootEntityClass() {
		return Account.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
	public String getName() {
		return name;
	}

	@Reference
	public Account getParent() {
		return parent;
	}

	public void setParent(Account parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the persistPymntInfo.
	 */
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

	public AccountAddress getAccountAddress(Object pk) {
		return findEntityInCollection(this.addresses, pk);
	}

	public AccountAddress getAccountAddress(String nme) {
		return findNamedEntityInCollection(this.addresses, nme);
	}

	public void addAccountAddress(AccountAddress e) {
		addEntityToCollection(addresses, e);
	}

	public void addAccountAddresses(Collection<AccountAddress> clctn) {
		addEntitiesToCollection(clctn, addresses);
	}

	public void removeAccountAddresses() {
		clearEntityCollection(addresses);
	}

	public void removeAccountAddress(AccountAddress e) {
		removeEntityFromCollection(addresses, e);
	}

	public int getNumAccountAddresses() {
		return getCollectionSize(addresses);
	}

	public Object accountKey() {
		return super.getId();
	}
}