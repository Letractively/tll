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

		getDbSupport().startNewTransaction();
		getDbSupport().setComplete();
		try {
			final IEntityDao dao = getEntityDao();
			
			account = getMockEntityFactory().getEntityCopy(Asp.class, false);
			final AccountAddress aa = getMockEntityFactory().getEntityCopy(AccountAddress.class, false);
			final Address a = getMockEntityFactory().getEntityCopy(Address.class, false);
			aa.setAddress(a);
			account.addAccountAddress(aa);

			Currency c = dao.persist(getMockEntityFactory().getEntityCopy(Currency.class, false));
			account.setCurrency(c);

			pi = dao.persist(getMockEntityFactory().getEntityCopy(PaymentInfo.class, false));
			account.setPaymentInfo(pi);

			if(persist) {
				account = dao.persist(account);
			}
		}
		finally {
			getDbSupport().endTransaction();
		}

		return account;
	}
}
