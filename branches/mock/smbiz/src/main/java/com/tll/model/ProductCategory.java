package com.tll.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Uniques;
import javax.validation.constraints.NotNull;

import org.hibernate.validation.constraints.Length;
import org.hibernate.validation.constraints.NotEmpty;

/**
 * Product category entity
 * @author jpk
 */
@PersistenceCapable
@Uniques(value = 
	@Unique(name = "Account Id and Name", members = { "account.id", INamedEntity.NAME }))
public class ProductCategory extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 5218888162655443332L;

	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_DESCRIPTION = 255;
	public static final int MAXLEN_IMAGE = 64;

	@Persistent
	private Account account;

	@Persistent
	private String description;

	@Persistent
	private String image;

	public Class<? extends IEntity> entityClass() {
		return ProductCategory.class;
	}

	@NotEmpty
	@Length(max = MAXLEN_NAME)
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the description.
	 */
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