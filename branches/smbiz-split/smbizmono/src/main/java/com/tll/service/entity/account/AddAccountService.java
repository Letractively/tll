package com.tll.service.entity.account;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.EntityExistsException;
import com.tll.model.Account;
import com.tll.model.AccountInterface;
import com.tll.model.Customer;
import com.tll.model.Isp;
import com.tll.model.Merchant;
import com.tll.model.User;
import com.tll.service.entity.intf.IInterfaceService;
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
	private final IInterfaceService iService;
	private final IUserService userService;
	// private final ICustomerAccountService caService;

	/**
	 * Constructor
	 * @param accountService
	 * @param piService
	 * @param iService
	 * @param userService
	 */
	@Inject
	public AddAccountService(IAccountService accountService, IPaymentInfoService piService,
			IInterfaceService iService, IUserService userService/*, ICustomerAccountService caService*/) {
		super();
		this.accountService = accountService;
		this.piService = piService;
		this.iService = iService;
		this.userService = userService;
		// this.caService = caService;
	}

	/**
	 * adds an account along with any related account interface options and users.
	 * @param account
	 * @param accountInterfaces may be null
	 * @param users may be null
	 * @return the added account
	 * @throws EntityExistsException when the account already exists (by business
	 *         key)
	 */
	private Account addAccount(Account account, Collection<AccountInterface> accountInterfaces,
			Collection<User> users) throws EntityExistsException {

		// create payment info
		if(account.getPaymentInfo() != null && account.getPaymentInfo().isNew() && account.getPersistPymntInfo()) {
			piService.persist(account.getPaymentInfo());
		}

		// create account
		final Account persistedAccount = accountService.persist(account);

		// add the account interface options
		iService.setAccountInterfaces(accountInterfaces);

		// add the users
		userService.persistAll(users);

		return persistedAccount;
	}

	/**
	 * Adds an Isp.
	 * @param isp
	 * @param accountInterfaces
	 * @param users
	 * @return the persisted isp
	 * @throws ConstraintViolationException
	 * @throws EntityExistsException
	 */
	@Transactional
	public Isp addIsp(Isp isp, Collection<AccountInterface> accountInterfaces, Collection<User> users)
	throws ConstraintViolationException, EntityExistsException {
		return (Isp) addAccount(isp, accountInterfaces, users);
	}

	/**
	 * Adds a merchant.
	 * @param merchant
	 * @param accountInterfaces
	 * @param users
	 * @return the persisted merchant
	 * @throws ConstraintViolationException
	 * @throws EntityExistsException
	 */
	@Transactional
	public Merchant addMerchant(Merchant merchant, Collection<AccountInterface> accountInterfaces,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {
		return (Merchant) addAccount(merchant, accountInterfaces, users);
	}

	/**
	 * Adds a customer
	 * @param customer
	 * @param accountInterfaces
	 * @param users
	 * @return the persisted customer
	 * @throws ConstraintViolationException
	 * @throws EntityExistsException
	 */
	@Transactional
	public Customer addCustomer(Customer customer, Collection<AccountInterface> accountInterfaces,
			Collection<User> users) throws ConstraintViolationException, EntityExistsException {
		return (Customer) addAccount(customer, accountInterfaces, users);
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
