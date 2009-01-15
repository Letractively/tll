package com.tll.service.impl;

import java.util.Collection;

import javax.persistence.EntityExistsException;

import org.hibernate.validator.InvalidStateException;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.ApplicationException;
import com.tll.model.impl.Account;
import com.tll.model.impl.Customer;
import com.tll.model.impl.CustomerAccount;
import com.tll.model.impl.InterfaceOptionAccount;
import com.tll.model.impl.Isp;
import com.tll.model.impl.Merchant;
import com.tll.model.impl.User;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.impl.account.IAccountService;
import com.tll.service.entity.impl.account.ICustomerAccountService;
import com.tll.service.entity.impl.intf.IInterfaceOptionAccountService;
import com.tll.service.entity.impl.pymnt.IPaymentInfoService;
import com.tll.service.entity.impl.user.IUserService;

/**
 * The add account service implementation.
 * @author jpk
 */
@Transactional
public class AddAccountService {

	private final IEntityServiceFactory serviceFactory;

	/**
	 * Constructor
	 * @param serviceFactory
	 */
	@Inject
	public AddAccountService(IEntityServiceFactory serviceFactory) {
		super();
		this.serviceFactory = serviceFactory;
	}

	/**
	 * adds an account along with any related account interface options and users.
	 * @param account
	 * @param accountInterfaceOptions may be null
	 * @param users may be null
	 * @throws EntityExistsException when the account already exists (by business
	 *         key)
	 * @throws ApplicationException when an exception occures adding the related
	 *         entities
	 */
	private Account addAccount(Account account, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws EntityExistsException {

		IAccountService accountService = serviceFactory.instance(IAccountService.class);
		IPaymentInfoService piService = serviceFactory.instance(IPaymentInfoService.class);
		IInterfaceOptionAccountService ioaService = serviceFactory.instance(IInterfaceOptionAccountService.class);
		IUserService userService = serviceFactory.instance(IUserService.class);

		// create payment info
		if(account.getPaymentInfo() != null && account.getPaymentInfo().isNew() && account.getPersistPymntInfo()) {
			piService.persist(account.getPaymentInfo());
		}

		// create account
		Account persistedAccount = accountService.persist(account);

		// add the account interface options
		ioaService.persistAll(accountInterfaceOptions);

		// add the users
		userService.persistAll(users);

		return persistedAccount;
	}

	@Transactional
	public void addIsp(Isp isp, Collection<InterfaceOptionAccount> accountInterfaceOptions, Collection<User> users)
			throws InvalidStateException, EntityExistsException {
		addAccount(isp, accountInterfaceOptions, users);
	}

	@Transactional
	public void addMerchant(Merchant merchant, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws InvalidStateException, EntityExistsException {
		addAccount(merchant, accountInterfaceOptions, users);
	}

	@Transactional
	public void addCustomer(Customer customer, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws InvalidStateException, EntityExistsException {
		addAccount(customer, accountInterfaceOptions, users);
	}

	@Transactional
	public void addCustomer(CustomerAccount customerAccount, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws InvalidStateException, EntityExistsException {

		// add Customer first
		Customer customer = customerAccount.getCustomer();
		if(customer.isNew()) {
			addAccount(customer, accountInterfaceOptions, users);
		}

		// add CustomerAccount now
		ICustomerAccountService caService = serviceFactory.instance(ICustomerAccountService.class);
		caService.persist(customerAccount);
	}
}
