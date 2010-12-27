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

/**
 * AccountInterface - Used for UI purposes to marshal an appropriate data
 * structure to/from client/server.
 * @author jpk
 */
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

	public void setAccountKey(final Object accountKey) {
		this.accountKey = accountKey;
	}

	public Object getInterfaceKey() {
		return interfaceKey;
	}

	public void setInterfaceKey(final Object interfaceKey) {
		this.interfaceKey = interfaceKey;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isAvailableAsp() {
		return isAvailableAsp;
	}

	public void setAvailableAsp(final boolean isAvailableAsp) {
		this.isAvailableAsp = isAvailableAsp;
	}

	public boolean isAvailableIsp() {
		return isAvailableIsp;
	}

	public void setAvailableIsp(final boolean isAvailableIsp) {
		this.isAvailableIsp = isAvailableIsp;
	}

	public boolean isAvailableMerchant() {
		return isAvailableMerchant;
	}

	public void setAvailableMerchant(final boolean isAvailableMerchant) {
		this.isAvailableMerchant = isAvailableMerchant;
	}

	public boolean isAvailableCustomer() {
		return isAvailableCustomer;
	}

	public void setAvailableCustomer(final boolean isAvailableCustomer) {
		this.isAvailableCustomer = isAvailableCustomer;
	}

	public boolean isRequiredAsp() {
		return isRequiredAsp;
	}

	public void setRequiredAsp(final boolean isRequiredAsp) {
		this.isRequiredAsp = isRequiredAsp;
	}

	public boolean isRequiredIsp() {
		return isRequiredIsp;
	}

	public void setRequiredIsp(final boolean isRequiredIsp) {
		this.isRequiredIsp = isRequiredIsp;
	}

	public boolean isRequiredMerchant() {
		return isRequiredMerchant;
	}

	public void setRequiredMerchant(final boolean isRequiredMerchant) {
		this.isRequiredMerchant = isRequiredMerchant;
	}

	public boolean isRequiredCustomer() {
		return isRequiredCustomer;
	}

	public void setRequiredCustomer(final boolean isRequiredCustomer) {
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
	public void setOptions(final Set<AccountInterfaceOption> options) {
		this.options = options;
	}

	public AccountInterfaceOption getOption(final Object pk) {
		return findEntityInCollection(options, pk);
	}

	public AccountInterfaceOption getOption(final String nme) {
		return findNamedEntityInCollection(options, nme);
	}

	public void addOption(final AccountInterfaceOption e) {
		addEntityToCollection(options, e);
	}

	public void addOptions(final Collection<AccountInterfaceOption> clc) {
		addEntitiesToCollection(clc, options);
	}

	public void removeOption(final AccountInterfaceOption e) {
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
