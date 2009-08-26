package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * The ship mode entity
 * @author jpk
 */
@PersistenceCapable
@Uniques(value =
	@Unique(name = "Account Id and Name", members = { "account.id", INamedEntity.NAME }))
public class ShipMode extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8602635055012335230L;

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_SRC_ZIP = 16;

	@Persistent
	private ShipModeType type;

	@Persistent
	private float surcharge = 0f;

	@Persistent
	private String srcZip;

	@Persistent
	private Account account;

	public Class<? extends IEntity> entityClass() {
		return ShipMode.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
	public String getName() {
		return name;
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
	 * @return Returns the srcZip.
	 */
	@Length(max = MAXLEN_SRC_ZIP)
	public String getSrcZip() {
		return srcZip;
	}

	/**
	 * @param srcZip The srcZip to set.
	 */
	public void setSrcZip(String srcZip) {
		this.srcZip = srcZip;
	}

	/**
	 * @return Returns the surcharge.
	 */
	// @Size(min = 0, max = 999999)
	public float getSurcharge() {
		return surcharge;
	}

	/**
	 * @param surcharge The surcharge to set.
	 */
	public void setSurcharge(float surcharge) {
		this.surcharge = surcharge;
	}

	/**
	 * @return Returns the type.
	 */
	@NotNull
	public ShipModeType getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(ShipModeType type) {
		this.type = type;
	}

	public Account getParent() {
		return getAccount();
	}

	public void setParent(Account e) {
		setAccount(e);
	}

	public String accountId() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}
}