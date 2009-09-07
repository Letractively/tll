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
import javax.validation.constraints.NotNull;

import com.tll.model.validate.BusinessKeyUniqueness;

/**
 * AccountInterface - Used for UI purposes to marshal an appropriate data
 * structure to/from client/server.
 * @author jpk
 */
public class AccountInterface extends EntityBase implements IAccountRelatedEntity {

	private static final long serialVersionUID = 7903409644943150791L;


	private String accountId, interfaceId;

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

	/**
	 * Constructor
	 * @param accountId
	 * @param interfaceId
	 * @param options
	 */
	public AccountInterface(String accountId, String interfaceId, Set<AccountInterfaceOption> options) {
		super();
		this.accountId = accountId;
		this.interfaceId = interfaceId;
		this.options = options;
	}

	@NotNull
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@NotNull
	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
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

	public AccountInterfaceOption getOption(int id) {
		return findEntityInCollection(options, id);
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
	public String accountId() {
		return accountId;
	}
}
