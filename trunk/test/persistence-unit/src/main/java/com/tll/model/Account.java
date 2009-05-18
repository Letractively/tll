package com.tll.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;
import com.tll.model.validate.AtLeastOne;
import com.tll.model.validate.BusinessKeyUniqueness;

@Entity
@Table(name = "account")
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Name", properties = INamedEntity.NAME))
/**
 * Account - Base class for account type entities.
 * @author jpk
 */
public class Account extends NamedTimeStampEntity implements IChildEntity<Account> {
	private static final long serialVersionUID = 9049425291965389270L;

	static final String ASP_VALUE = "0";
	static final String ISP_VALUE = "1";
	static final String MERCHANT_VALUE = "2";
	static final String CUSTOMER_VALUE = "3";

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_BILLING_MODEL = 32;
	public static final int MAXLEN_BILLING_CYCLE = 32;

	protected Account parent;

	protected FieldEnum status;

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
	 * @return Returns the nestedEntity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ne_id")
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
	@Column(nullable = false)
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
	public AccountAddress getAccountAddress(String accountName) {
		return findNamedEntityInCollection(this.addresses, accountName);
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
}