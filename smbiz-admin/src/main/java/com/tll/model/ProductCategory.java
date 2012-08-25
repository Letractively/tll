package com.tll.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.bk.BusinessKeyDef;
import com.tll.model.bk.BusinessObject;

/**
 * Product category entity
 * @author jpk
 */
@BusinessObject(businessKeys = @BusinessKeyDef(name = "Account Id and Name", properties = {
	"account.id", INamedEntity.NAME }))
public class ProductCategory extends NamedTimeStampEntity implements IChildEntity<Account>, IAccountRelatedEntity {

	private static final long serialVersionUID = 5218888162655443332L;

	public static final int MAXLEN_NAME = 128;
	public static final int MAXLEN_DESCRIPTION = 255;
	public static final int MAXLEN_IMAGE = 64;

	private Account account;

	private String description;

	private String image;

	@Override
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
	public void setDescription(final String description) {
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
	public void setImage(final String image) {
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
	public void setAccount(final Account account) {
		this.account = account;
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
	public Long accountKey() {
		try {
			return getAccount().getId();
		}
		catch(final NullPointerException npe) {
			LOG.warn("Unable to provide related account id due to a NULL nested entity");
			return null;
		}
	}
}