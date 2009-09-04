package com.tll.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.validate.AtLeastOne;
import com.tll.model.validate.BusinessKeyUniqueness;

/**
 * Account - Base class for account type entities.
 * @author jpk
 */
@PersistenceCapable(detachable = "true")
@Uniques(value = @Unique(name = "Name", members = INamedEntity.NAME))
public class Account extends NamedTimeStampEntity implements IChildEntity<Account> {
	private static final long serialVersionUID = 9049425291965389270L;

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_BILLING_MODEL = 32;
	public static final int MAXLEN_BILLING_CYCLE = 32;

	@Persistent
	protected Account parent;

	@Persistent
	protected FieldEnum status;

	@Persistent
	protected String billingModel;

	@Persistent
	protected Date dateLastCharged;

	@Persistent(defaultFetchGroup = "true")
	protected NestedEntity nestedEntity;

	@Persistent(defaultFetchGroup = "true")
	protected Currency currency;

	@Persistent(mappedBy = "account", defaultFetchGroup = "true")
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
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	public Account getParent() {
		return parent;
	}

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
	public FieldEnum getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(FieldEnum status) {
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

	public AccountAddress getAccountAddress(int id) {
		return findEntityInCollection(this.addresses, id);
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