package com.tll.model.impl;

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

import com.tll.client.model.IPropertyValue;
import com.tll.client.model.IntPropertyValue;
import com.tll.client.model.StringPropertyValue;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;
import com.tll.model.NamedTimeStampEntity;
import com.tll.model.key.BusinessKey;

/**
 * Product category entity
 * @author jpk
 */
@Entity
@Table(name = "product_category")
public class ProductCategory extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 5218888162655443332L;

	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_DESCRIPTION = 255;
	public static final int MAXLEN_IMAGE = 64;

	private Account account;

	private String description;

	private String image;

	public Class<? extends IEntity> entityClass() {
		return ProductCategory.class;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the description.
	 */
	@Column
	@Length(max = MAXLEN_DESCRIPTION)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the image.
	 */
	@Column
	@Length(max = MAXLEN_IMAGE)
	public String getImage() {
		return image;
	}

	/**
	 * @param image The image to set.
	 */
	public void setImage(String image) {
		this.image = image;
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
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] { new BusinessKey(ProductCategory.class, "Account Id and Name", new IPropertyValue[] {
			new IntPropertyValue("account.id", accountId()),
			new StringPropertyValue("name", getName()) }) };
	}
}