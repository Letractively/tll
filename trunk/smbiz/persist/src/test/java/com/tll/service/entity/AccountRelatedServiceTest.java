/*
 * The Logic Lab
 */
package com.tll.service.entity;

import org.testng.annotations.Test;

import com.tll.dao.IEntityDao;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.PaymentInfo;

/**
 * AccountRelatedServiceTest
 * @author jpk
 */
@Test(groups = "service.entity")
public abstract class AccountRelatedServiceTest extends AbstractEntityServiceTest {

	/**
	 * Necessary to retain this ref since Account.PaymentInfo orm mapping is LAZY!
	 */
	private PaymentInfo pi;

	/**
	 * Constructor
	 */
	public AccountRelatedServiceTest() {
		super();
	}

	/**
	 * Stub a valid account optionally persisting it in the datastore.
	 * @param persist persist into datastore?
	 * @return The stubbed account
	 */
	protected final Account stubValidAccount(boolean persist) {
		Account account = null;

		startNewTransaction();
		setComplete();
		try {
			final IEntityDao dao = getDao();

			account = getEntityBeanFactory().getEntityCopy(Asp.class, false);
			final AccountAddress aa = getEntityBeanFactory().getEntityCopy(AccountAddress.class, false);
			final Address a = getEntityBeanFactory().getEntityCopy(Address.class, false);
			aa.setAddress(a);
			account.addAccountAddress(aa);

			final Currency c = dao.persist(getEntityBeanFactory().getEntityCopy(Currency.class, false));
			account.setCurrency(c);

			pi = dao.persist(getEntityBeanFactory().getEntityCopy(PaymentInfo.class, false));
			account.setPaymentInfo(pi);

			if(persist) {
				account = dao.persist(account);
			}
		}
		finally {
			endTransaction();
		}

		return account;
	}
}
