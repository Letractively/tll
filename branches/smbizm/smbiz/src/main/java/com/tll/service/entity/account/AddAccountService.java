package com.tll.service.entity.account;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.model.Account;
import com.tll.model.Customer;
import com.tll.model.InterfaceOptionAccount;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.User;
import com.tll.service.entity.intf.IInterfaceOptionAccountService;
import com.tll.service.entity.pymnt.IPaymentInfoService;
import com.tll.service.entity.user.IUserService;

/**
 * AddAccountService - Derived entity service dedicated to adding
 * {@link Account}s.
 * @author jpk
 */
@Transactional
public class AddAccountService {

	private final IAccountService accountService;
	private final IPaymentInfoService piService;
	private final IInterfaceOptionAccountService ioaService;
	private final IUserService userService;
	// private final ICustomerAccountService caService;

	/**
	 * Constructor
	 * @param accountService
	 * @param piService
	 * @param ioaService
	 * @param userService
	 */
	@Inject
	public AddAccountService(IAccountService accountService, IPaymentInfoService piService,
			IInterfaceOptionAccountService ioaService, IUserService userService/*, ICustomerAccountService caService*/) {
		super();
		this.accountService = accountService;
		this.piService = piService;
		this.ioaService = ioaService;
		this.userService = userService;
		// this.caService = caService;
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

	/**
	 * Adds an Isp.
	 * @param isp
	 * @param accountInterfaceOptions
	 * @param users
	 * @return the persisted isp
	 * @throws ConstraintViolationException
	 * @throws EntityExistsException
	 */
	@Transactional
	public Isp addIsp(Isp isp, Collection<InterfaceOptionAccount> accountInterfaceOptions, Collection<User> users)
	throws ConstraintViolationException, EntityExistsException {
		return (Isp) addAccount(isp, accountInterfaceOptions, users);
	}

	/**
	 * Adds a merchant.
	 * @param merchant
	 * @param accountInterfaceOptions
	 * @param users
	 * @return the persisted merchant
	 * @throws ConstraintViolationException
	 * @throws EntityExistsException
	 */
	@Transactional
	public Merchant addMerchant(Merchant merchant, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {
		return (Merchant) addAccount(merchant, accountInterfaceOptions, users);
	}

	/**
	 * Adds a customer
	 * @param customer
	 * @param accountInterfaceOptions
	 * @param users
	 * @return the persisted customer
	 * @throws ConstraintViolationException
	 * @throws EntityExistsException
	 */
	@Transactional
	public Customer addCustomer(Customer customer, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {
		return (Customer) addAccount(customer, accountInterfaceOptions, users);
	}

	/*
	@Transactional
	public void addCustomer(CustomerAccount customerAccount, Collection<InterfaceOptionAccount> accountInterfaceOptions,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {

		// add Customer first
		final Customer customer = customerAccount.getCustomer();
		if(customer.isNew()) {
			addAccount(customer, accountInterfaceOptions, users);
		}

		// add CustomerAccount now
		caService.persist(customerAccount);
	}
	 */
}
