/**
 * The Logic Lab
 * @author jpk
 * @since Sep 7, 2009
 */
package com.tll.model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;

import com.tll.model.validate.BusinessKeyUniqueness;
import com.tll.schema.Transient;

/**
 * AccountInterface - Used for UI purposes to marshal an appropriate data
 * structure to/from client/server.
 * @author jpk
 */
@Transient
public class AccountInterface extends EntityBase implements IAccountRelatedEntity {

	private static final long serialVersionUID = 7903409644943150791L;

	private Object accountKey, interfaceKey;

	private String name, code, description;

	private boolean isAvailableAsp, isAvailableIsp, isAvailableMerchant, isAvailableCustomer = false;

	private boolean isRequiredAsp, isRequiredIsp, isRequiredMerchant, isRequiredCustomer;

	private Set<AccountInterfaceOption> options = new LinkedHashSet<AccountInterfaceOption>();

	@Override
	public Class<? extends IEntity> entityClass() {
		return AccountInterface.class;
	}

	/**
	 * Constructor
	 */
	public AccountInterface() {
		super();
	}

	public Object getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Object accountKey) {
		this.accountKey = accountKey;
	}

	public Object getInterfaceKey() {
		return interfaceKey;
	}

	public void setInterfaceKey(Object interfaceKey) {
		this.interfaceKey = interfaceKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isAvailableAsp() {
		return isAvailableAsp;
	}

	public void setAvailableAsp(boolean isAvailableAsp) {
		this.isAvailableAsp = isAvailableAsp;
	}

	public boolean isAvailableIsp() {
		return isAvailableIsp;
	}

	public void setAvailableIsp(boolean isAvailableIsp) {
		this.isAvailableIsp = isAvailableIsp;
	}

	public boolean isAvailableMerchant() {
		return isAvailableMerchant;
	}

	public void setAvailableMerchant(boolean isAvailableMerchant) {
		this.isAvailableMerchant = isAvailableMerchant;
	}

	public boolean isAvailableCustomer() {
		return isAvailableCustomer;
	}

	public void setAvailableCustomer(boolean isAvailableCustomer) {
		this.isAvailableCustomer = isAvailableCustomer;
	}

	public boolean isRequiredAsp() {
		return isRequiredAsp;
	}

	public void setRequiredAsp(boolean isRequiredAsp) {
		this.isRequiredAsp = isRequiredAsp;
	}

	public boolean isRequiredIsp() {
		return isRequiredIsp;
	}

	public void setRequiredIsp(boolean isRequiredIsp) {
		this.isRequiredIsp = isRequiredIsp;
	}

	public boolean isRequiredMerchant() {
		return isRequiredMerchant;
	}

	public void setRequiredMerchant(boolean isRequiredMerchant) {
		this.isRequiredMerchant = isRequiredMerchant;
	}

	public boolean isRequiredCustomer() {
		return isRequiredCustomer;
	}

	public void setRequiredCustomer(boolean isRequiredCustomer) {
		this.isRequiredCustomer = isRequiredCustomer;
	}

	/**
	 * @return Returns the options.
	 */
	@BusinessKeyUniqueness(type = "option")
	@Valid
	public Set<AccountInterfaceOption> getOptions() {
		return options;
	}

	/**
	 * @param options The options to set.
	 */
	public void setOptions(Set<AccountInterfaceOption> options) {
		this.options = options;
	}

	public AccountInterfaceOption getOption(Object pk) {
		return findEntityInCollection(options, pk);
	}

	public AccountInterfaceOption getOption(String nme) {
		return findNamedEntityInCollection(options, nme);
	}

	public void addOption(AccountInterfaceOption e) {
		addEntityToCollection(options, e);
	}

	public void addOptions(Collection<AccountInterfaceOption> clc) {
		addEntitiesToCollection(clc, options);
	}

	public void removeOption(AccountInterfaceOption e) {
		removeEntityFromCollection(options, e);
	}

	public void clearOptions() {
		clearEntityCollection(options);
	}

	public int getNumOptions() {
		return getCollectionSize(options);
	}

	@Override
	public Object accountKey() {
		return accountKey;
	}
}
