package com.tll.model.test;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.BusinessKeyDef;
import com.tll.model.BusinessObject;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NamedTimeStampEntity;
import com.tll.model.Reference;
import com.tll.model.validate.AtLeastOne;
import com.tll.model.validate.BusinessKeyUniqueness;

/**
 * Account - Base class for account type entities.
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = INamedEntity.NAME))
public class Account extends NamedTimeStampEntity implements IChildEntity<Account> {
	private static final long serialVersionUID = 9049425291965389270L;

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_BILLING_MODEL = 32;
	public static final int MAXLEN_BILLING_CYCLE = 32;

	protected Account parent;

	protected AccountStatus status;

	protected String billingModel;

	protected Date dateLastCharged;

	protected NestedEntity nestedEntity;

	protected Currency currency;

	protected Set<AccountAddress> addresses = new LinkedHashSet<AccountAddress>(3);

	/**
	 * Constructor
	 */
	public Account() {
		super();
	}

	@Override
	public Class<? extends IEntity> entityClass() {
		return Account.class;
	}

	@Override
	public String getEntityType() {
		return "Account";
	}

	@Override
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	@Reference
	@Override
	public Account getParent() {
		return parent;
	}

	@Override
	public void setParent(Account parent) {
		this.parent = parent;
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
	@Reference
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
	 * @return Returns the nestedEntity.
	 */
	@Valid
	public NestedEntity getNestedEntity() {
		return nestedEntity;
	}

	/**
	 * @param nestedEntity The nestedEntity to set.
	 */
	public void setNestedEntity(NestedEntity nestedEntity) {
		this.nestedEntity = nestedEntity;
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

	public AccountAddress getAccountAddress(String accountName) {
		return findNamedEntityInCollection(this.addresses, accountName);
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
}