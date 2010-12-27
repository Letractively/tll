package com.tll.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.bk.BusinessKeyDef;

/**
 * The ship mode entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Account Id and Name", properties = {
	"account.id", INamedEntity.NAME }))
public class ShipMode extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = -8602635055012335230L;

	public static final int MAXLEN_NAME = 32;
	public static final int MAXLEN_SRC_ZIP = 16;

	private ShipModeType type;

	private float surcharge = 0f;

	private String srcZip;

	private Account account;

	@Override
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
	public void setAccount(final Account account) {
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
	public void setSrcZip(final String srcZip) {
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
	public void setSurcharge(final float surcharge) {
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
	public void setType(final ShipModeType type) {
		this.type = type;
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