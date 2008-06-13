package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Valid;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;

/**
 * The customer account entity
 * @author jpk
 */
@Entity
@Table(name = "customer_account")
public class CustomerAccount extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 7262902363821073379L;

	protected Customer customer;

	protected Account account;

	protected AccountSource source;

	protected AccountStatus status;

	protected Visitor initialVisitorRecord;

	public Class<? extends IEntity> entityClass() {
		return CustomerAccount.class;
	}

	/**
	 * @return Returns the customer.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id", updatable = false)
	@NotNull
	@Valid
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer The customer to set.
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return Returns the account.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "aid", updatable = false)
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
	 * @return Returns the source.
	 */
	@Column
	@NotNull
	public AccountSource getSource() {
		return source;
	}

	/**
	 * @param source The source to set.
	 */
	public void setSource(AccountSource source) {
		this.source = source;
	}

	/**
	 * @return Returns the status.
	 */
	@Column
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
	 * @return Returns the initialVisitorRecord.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visitor_id")
	public Visitor getInitialVisitorRecord() {
		return initialVisitorRecord;
	}

	/**
	 * @param initialVisitorRecord The initialVisitorRecord to set.
	 */
	public void setInitialVisitorRecord(Visitor initialVisitorRecord) {
		this.initialVisitorRecord = initialVisitorRecord;
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

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("customer", customer == null ? "NULL" : customer.descriptor()).append(
				"account", account == null ? "NULL" : account.descriptor()).append("source", source).append("status", status)
				.append("initialVisitorRecord", initialVisitorRecord == null ? "NULL" : initialVisitorRecord.descriptor());
	}

}