package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * The visitor entity (those people who visit a storefront site).
 * @author jpk
 */
@PersistenceCapable
@BusinessObject(businessKeys =
	@BusinessKeyDef(name = "Account Id, Date Created, Remote Host",
			properties = { "account.id", "dateCreated", "remoteHost" }))
			public class Visitor extends TimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 3466539674112418212L;

	public static final int MAXLEN_REMOTE_HOST = 64;
	public static final int MAXLEN_REMOTE_ADDR = 64;
	public static final int MAXLEN_REMOTE_USER = 64;
	public static final int MAXLEN_MC = 16;

	@Persistent
	private String remoteHost;

	@Persistent
	private String remoteAddr;

	@Persistent
	private String remoteUser;

	@Persistent
	private String mc;

	@Persistent
	private Account account;

	public Class<? extends IEntity> entityClass() {
		return Visitor.class;
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
	public void setAccount(Account account) {
		this.account = account;
	}

	/**
	 * @return Returns the mc.
	 */
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
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}
}