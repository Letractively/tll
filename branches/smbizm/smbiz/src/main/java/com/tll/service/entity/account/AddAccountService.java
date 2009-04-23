package com.tll.service.entity.account;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.model.Account;
import com.tll.model.Customer;
import com.tll.model.CustomerAccount;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.User;
import com.tll.service.entity.IEntityServiceFactory;
import com.tll.service.entity.intf.IInterfaceOptionAccountService;
import com.tll.service.entity.pymnt.IPaymentInfoService;
import com.tll.service.entity.user.IUserService;

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
	 * @return the added account
	 * @throws EntityExistsException when the account already exists (by business
	 *         key)
	 */
	private Account addAccount(Account account, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws EntityExistsException {

		final IAccountService accountService = serviceFactory.instance(IAccountService.class);
		final IPaymentInfoService piService = serviceFactory.instance(IPaymentInfoService.class);
		final IInterfaceOptionAccountService ioaService = serviceFactory.instance(IInterfaceOptionAccountService.class);
		final IUserService userService = serviceFactory.instance(IUserService.class);

		// create payment info
		if(account.getPaymentInfo() != null && account.getPaymentInfo().isNew() && account.getPersistPymntInfo()) {
			piService.persist(account.getPaymentInfo());
		}

		// create account
		final Account persistedAccount = accountService.persist(account);

		// add the account interface options
		ioaService.persistAll(accountInterfaceOptions);

		// add the users
		userService.persistAll(users);

		return persistedAccount;
	}

	@Transactional
	public void addIsp(Isp isp, Collection<InterfaceOptionAccount> accountInterfaceOptions, Collection<User> users)
	throws ConstraintViolationException, EntityExistsException {
		addAccount(isp, accountInterfaceOptions, users);
	}

	@Transactional
	public void addMerchant(Merchant merchant, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {
		addAccount(merchant, accountInterfaceOptions, users);
	}

	@Transactional
	public void addCustomer(Customer customer, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {
		addAccount(customer, accountInterfaceOptions, users);
	}

	@Transactional
	public void addCustomer(CustomerAccount customerAccount, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {

		// add Customer first
		final Customer customer = customerAccount.getCustomer();
		if(customer.isNew()) {
			addAccount(customer, accountInterfaceOptions, users);
		}

		// add CustomerAccount now
		final ICustomerAccountService caService = serviceFactory.instance(ICustomerAccountService.class);
		caService.persist(customerAccount);
	}
}
