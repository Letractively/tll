/*
 * The Logic Lab 
 */
package com.tll.service.entity;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.criteria.Criteria;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.dao.impl.IAccountDao;
import com.tll.dao.impl.IAccountHistoryDao;
import com.tll.dao.impl.ICurrencyDao;
import com.tll.dao.impl.IPaymentInfoDao;
import com.tll.dao.impl.IUserDao;
import com.tll.guice.DaoModule;
import com.tll.guice.EntityServiceModule;
import com.tll.guice.JpaModule;
import com.tll.listhandler.SearchResult;
import com.tll.model.impl.Account;
import com.tll.model.impl.AccountAddress;
import com.tll.model.impl.AccountHistory;
import com.tll.model.impl.Address;
import com.tll.model.impl.Asp;
import com.tll.model.impl.Currency;
import com.tll.model.impl.PaymentInfo;
import com.tll.model.impl.User;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.impl.account.IAccountService;
import com.tll.service.entity.impl.user.IUserService;

/**
 * EntityServiceTest
 * @author jpk
 */
@Test(groups = "entity.service")
public class EntityServiceTest extends DbTest {

	/**
	 * Necessary to retain this ref since Account.PaymentInfo orm mapping is LAZY!
	 */
	private PaymentInfo pi;

	/**
	 * Constructor
	 */
	public EntityServiceTest() {
		super(JpaMode.SPRING);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new JpaModule(jpaMode));
		modules.add(new DaoModule(DaoMode.ORM));
		modules.add(new EntityServiceModule());
	}

	@BeforeClass(alwaysRun = true)
	public void onBeforeClass() {
		beforeClass();
	}

	@BeforeMethod
	public void onBeforeMethod() {
		beforeMethod();
	}

	@Override
	protected void beforeClass() {
		super.beforeClass();
		// ensure test db is created and cleared
		getDbShell().create();
	}

	@Override
	protected void beforeMethod() {
		super.beforeMethod();
		getDbShell().clear(); // reset
	}

	private Account stubValidAccount(boolean persistAccount) throws Exception {
		Account account = null;

		startNewTransaction();
		setComplete();

		try {
			Currency c = null;

			final IAccountDao accountDao = injector.getInstance(IAccountDao.class);
			final ICurrencyDao currencyDao = injector.getInstance(ICurrencyDao.class);
			final IPaymentInfoDao piDao = injector.getInstance(IPaymentInfoDao.class);

			account = getMockEntityProvider().getEntityCopy(Asp.class);
			final AccountAddress aa = getMockEntityProvider().getEntityCopy(AccountAddress.class);
			final Address a = getMockEntityProvider().getEntityCopy(Address.class);
			aa.setAddress(a);
			getEntityAssembler().setGenerated(a);
			account.addAccountAddress(aa);

			c = currencyDao.persist(getMockEntityProvider().getEntityCopy(Currency.class));
			account.setCurrency(c);

			pi = piDao.persist(getMockEntityProvider().getEntityCopy(PaymentInfo.class));
			account.setPaymentInfo(pi);

			if(persistAccount) {
				account = accountDao.persist(account);
			}
		}
		catch(final RuntimeException re) {
			Assert.fail("Unable to stub account");
		}
		finally {
			if(isTransStarted()) endTransaction();
		}

		return account;
	}

	@Test
	public void testUserCreate() throws Exception {
		Account account = null;
		try {
			// stub the related account
			account = stubValidAccount(true);
			// the service test (should be transactional)
			final IUserService entityService = injector.getInstance(IUserService.class);
			logger.debug("About to call @Transactional method...");
			final User user = entityService.create(account, "name@domain.com", "password");
			logger.debug("@Transactional method call returned...");
			Assert.assertNotNull(user);

			startNewTransaction();
			final User dbUser = getEntityFromDb(injector.getInstance(IUserDao.class), user.getPrimaryKey());
			endTransaction();
			Assert.assertEquals(dbUser, user);
		}
		catch(final Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
	}

	@Test
	public void testAccountHistoryRecordCreation() throws Exception {
		Account account = null;
		try {
			account = stubValidAccount(false);

			final IAccountService as = injector.getInstance(IAccountService.class);
			account = as.persist(account);

			startNewTransaction();
			final Criteria<? extends AccountHistory> criteria = new Criteria<AccountHistory>(AccountHistory.class);
			criteria.getPrimaryGroup().addCriterion("account", new PrimaryKey<Account>(Account.class, account.getId()));
			final List<SearchResult<AccountHistory>> list =
					getEntitiesFromDb(injector.getInstance(IAccountHistoryDao.class), criteria);
			endTransaction();
			assert list != null && list.size() == 1;
		}
		catch(final Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
	}
}
