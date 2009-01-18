package com.tll.model.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;

/**
 * The account history entity
 * @author jpk
 */
@Entity
@Table(name = "account_history")
// TODO re-instate immutable (hibernate 3.3.1 seems to break this!)
// http://opensource.atlassian.com/projects/hibernate/browse/HHH-3662
// @Immutable
public class AccountHistory extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 5543822993709686604L;

	private Account account;

	private Date transDate = new Date();

	private AccountStatus status;

	private String notes;

	private PaymentTrans pymntTrans;

	public Class<? extends IEntity> entityClass() {
		return AccountHistory.class;
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
	 * @return Returns the notes.
	 */
	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes The notes to set.
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return Returns the pymntTrans.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pt_id")
	public PaymentTrans getPymntTrans() {
		return pymntTrans;
	}

	/**
	 * @param pymntTrans The pymntTrans to set.
	 */
	public void setPymntTrans(PaymentTrans pymntTrans) {
		this.pymntTrans = pymntTrans;
	}

	/**
	 * @return Returns the status.
	 */
	@Column(name = "status")
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
	 * @return Returns the transDate.
	 */
	@Column(name = "trans_date")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	public Date getTransDate() {
		return transDate;
	}

	/**
	 * @param transDate The transDate to set.
	 */
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
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