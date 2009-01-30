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
import com.tll.dao.IEntityDao;
import com.tll.dao.JpaMode;
import com.tll.dao.SearchResult;
import com.tll.di.DaoModule;
import com.tll.di.EntityServiceImplModule;
import com.tll.model.Account;
import com.tll.model.AccountAddress;
import com.tll.model.AccountHistory;
import com.tll.model.Address;
import com.tll.model.Asp;
import com.tll.model.Currency;
import com.tll.model.IEntityFactory;
import com.tll.model.MockEntityFactory;
import com.tll.model.PaymentInfo;
import com.tll.model.User;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.account.IAccountService;
import com.tll.service.entity.user.IUserService;

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
		super(JpaMode.SPRING, true);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.ORM));
		modules.add(new EntityServiceImplModule());
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

	private IEntityDao getEntityDao() {
		return injector.getInstance(IEntityDao.class);
	}

	private MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
	}

	private IEntityFactory getEntityFactory() {
		return injector.getInstance(IEntityFactory.class);
	}

	private Account stubValidAccount(boolean persistAccount) throws Exception {
		Account account = null;

		startNewTransaction();
		setComplete();

		try {
			Currency c = null;

			account = getMockEntityFactory().getEntityCopy(Asp.class, false);
			final AccountAddress aa = getMockEntityFactory().getEntityCopy(AccountAddress.class, false);
			final Address a = getMockEntityFactory().getEntityCopy(Address.class, false);
			aa.setAddress(a);
			getEntityFactory().setGenerated(a);
			account.addAccountAddress(aa);

			final IEntityDao dao = getEntityDao();

			c = dao.persist(getMockEntityFactory().getEntityCopy(Currency.class, false));
			account.setCurrency(c);

			pi = dao.persist(getMockEntityFactory().getEntityCopy(PaymentInfo.class, false));
			account.setPaymentInfo(pi);

			if(persistAccount) {
				account = dao.persist(account);
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
			final User dbUser = getEntityFromDb(getEntityDao(), new PrimaryKey<User>(user));
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
			final Criteria<AccountHistory> criteria = new Criteria<AccountHistory>(AccountHistory.class);
			criteria.getPrimaryGroup().addCriterion("account", new PrimaryKey<Account>(Account.class, account.getId()));
			final List<SearchResult<AccountHistory>> list = getEntitiesFromDb(getEntityDao(), criteria);
			endTransaction();
			assert list != null && list.size() == 1;
		}
		catch(final Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
	}
}
