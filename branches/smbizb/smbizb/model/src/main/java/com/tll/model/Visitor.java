package com.tll.model;

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

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.TimeStampEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * The visitor entity (those people who visit a storefront site).
 * @author jpk
 */
@Entity
@Table(name = "visitor")
@BusinessObject(businessKeys = 
	@BusinessKeyDef(name = "Account Id, Date Created, Remote Host", 
			properties = { "account.id", "dateCreated", "remoteHost" }))
public class Visitor extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 3466539674112418212L;

	public static final int MAXLEN_REMOTE_HOST = 64;
	public static final int MAXLEN_REMOTE_ADDR = 64;
	public static final int MAXLEN_REMOTE_USER = 64;
	public static final int MAXLEN_MC = 16;

	private String remoteHost;

	private String remoteAddr;

	private String remoteUser;

	private String mc;

	private Account account;

	public Class<? extends IEntity> entityClass() {
		return Visitor.class;
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
	 * @return Returns the mc.
	 */
	@Column
	@Length(max = MAXLEN_MC)
	public String getMc() {
		return mc;
	}

	/**
	 * @param mc The mc to set.
	 */
	public void setMc(String mc) {
		this.mc = mc;
	}

	/**
	 * @return Returns the remoteAddr.
	 */
	@Column(name = "remote_addr")
	@Length(max = MAXLEN_REMOTE_ADDR)
	public String getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr The remoteAddr to set.
	 */
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @return Returns the remoteHost.
	 */
	@Column(name = "remote_host")
	@NotEmpty
	@Length(max = MAXLEN_REMOTE_HOST)
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @param remoteHost The remoteHost to set.
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * @return Returns the remoteUser.
	 */
	@Column(name = "remote_user")
	@NotEmpty
	@Length(max = MAXLEN_REMOTE_USER)
	public String getRemoteUser() {
		return remoteUser;
	}

	/**
	 * @param remoteUser The remoteUser to set.
	 */
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
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