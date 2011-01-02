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

		getDbTrans().startTrans();
		getDbTrans().setComplete();
		try {
			final IEntityDao dao = getDao();

			account = getEntityBeanFactory().getEntityCopy(Asp.class);
			final AccountAddress aa = getEntityBeanFactory().getEntityCopy(AccountAddress.class);
			final Address a = getEntityBeanFactory().getEntityCopy(Address.class);
			aa.setAddress(a);
			account.addAccountAddress(aa);

			final Currency c = dao.persist(getEntityBeanFactory().getEntityCopy(Currency.class));
			account.setCurrency(c);

			pi = dao.persist(getEntityBeanFactory().getEntityCopy(PaymentInfo.class));
			account.setPaymentInfo(pi);

			if(persist) {
				account = dao.persist(account);
			}
		}
		finally {
			getDbTrans().endTrans();
		}

		return account;
	}
}
