package com.tll.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.tll.model.bk.BusinessKeyDef;

/**
 * The account history entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Account Id, Transaction Date and Status", properties = {
	"account.id", "transDate", "status" }))
public class AccountHistory extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 5543822993709686604L;

	private Account account;

	private Date transDate = new Date();

	private AccountStatus status;

	private String notes;

	private PaymentTrans pymntTrans;

	@Override
	public Class<? extends IEntity> entityClass() {
		return AccountHistory.class;
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
	public void setAccount(final Account account) {
		this.account = account;
	}

	/**
	 * @return Returns the notes.
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes The notes to set.
	 */
	public void setNotes(final String notes) {
		this.notes = notes;
	}

	/**
	 * @return Returns the pymntTrans.
	 */
	public PaymentTrans getPymntTrans() {
		return pymntTrans;
	}

	/**
	 * @param pymntTrans The pymntTrans to set.
	 */
	public void setPymntTrans(final PaymentTrans pymntTrans) {
		this.pymntTrans = pymntTrans;
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
	public void setStatus(final AccountStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the transDate.
	 */
	@NotNull
	public Date getTransDate() {
		return transDate;
	}

	/**
	 * @param transDate The transDate to set.
	 */
	public void setTransDate(final Date transDate) {
		this.transDate = transDate;
	}

	@Override
	public Account getParent() {
		return getAccount();
	}

	@Override
	public void setParent(final Account e) {
		setAccount(e);
	}

	@Override
	public Object accountKey() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}
}
