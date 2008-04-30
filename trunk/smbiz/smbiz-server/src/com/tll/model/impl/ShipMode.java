package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;

import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.NamedTimeStampEntity;

/**
 * The ship mode entity
 * 
 * @author jpk
 */
@Entity
@Table(name="ship_mode")
public class ShipMode extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {
  private static final long serialVersionUID = -8602635055012335230L;

  public static final int MAXLEN_NAME = 32;
  public static final int MAXLEN_SRC_ZIP = 16;
  
  protected ShipModeType type;

  protected float surcharge = 0f;

  protected String srcZip;

  protected Account account;

  public Class<? extends IEntity> entityClass() {
    return ShipMode.class;
  }

  @Column
  @NotEmpty @Length(max=MAXLEN_NAME)
	public String getName() {
		return name;
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
   * @param account
   *          The account to set.
   */
  public void setAccount(Account account) {
    this.account = account;
  }

  /**
   * @return Returns the srcZip.
   */
  @Column(name="src_zip")
  @Length(max=MAXLEN_SRC_ZIP)
  public String getSrcZip() {
    return srcZip;
  }

  /**
   * @param srcZip
   *          The srcZip to set.
   */
  public void setSrcZip(String srcZip) {
    this.srcZip = srcZip;
  }

  /**
   * @return Returns the surcharge.
   */
  @Column(precision = 8, scale = 2)
  @Range(min=0, max=999999)
  public float getSurcharge() {
    return surcharge;
  }

  /**
   * @param surcharge
   *          The surcharge to set.
   */
  public void setSurcharge(float surcharge) {
    this.surcharge = surcharge;
  }

  /**
   * @return Returns the type.
   */
  @Column
  @NotNull
  public ShipModeType getType() {
    return type;
  }

  /**
   * @param type
   *          The type to set.
   */
  public void setType(ShipModeType type) {
    this.type = type;
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
    return super.toStringBuilder()
    .append("type", type)
    .append("surcharge", surcharge)
    .append("srcZip", srcZip)
    .append("account", account==null? "NULL" :  account.descriptor());
  }

}