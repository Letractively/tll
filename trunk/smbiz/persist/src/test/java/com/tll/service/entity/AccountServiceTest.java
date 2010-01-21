/*
 * The Logic Lab
 */
package com.tll.service.entity;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.criteria.Criteria;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.dao.SearchResult;
import com.tll.dao.Sorting;
import com.tll.listhandler.IListHandler;
import com.tll.listhandler.IListingDataProvider;
import com.tll.listhandler.ListHandlerFactory;
import com.tll.listhandler.ListHandlerType;
import com.tll.model.Account;
import com.tll.model.AccountHistory;
import com.tll.model.key.PrimaryKey;
import com.tll.service.entity.account.IAccountService;

/**
 * AccountRelatedServiceTest
 * @author jpk
 */
@Test(groups = "service.entity")
public abstract class AccountServiceTest extends AccountRelatedServiceTest {

	/**
	 * Constructor
	 */
	public AccountServiceTest() {
		super();
	}

	/**
	 * Test the creation of an account history record.
	 * @throws Exception
	 */
	@Test
	public void testAccountHistoryRecordCreation() throws Exception {
		Account account = null;
		try {
			account = stubValidAccount(false);

			final IAccountService accountService = getEntityServiceFactory().instance(IAccountService.class);
			account = accountService.persist(account);

			startNewTransaction();
			final Criteria<AccountHistory> criteria = new Criteria<AccountHistory>(AccountHistory.class);
			criteria.getPrimaryGroup().addCriterion("account", new PrimaryKey<Account>(Account.class, account.getId()));
			final List<SearchResult> list = AbstractDbAwareTest.getEntitiesFromDb(getDao(), criteria);
			endTransaction();
			assert list != null && list.size() == 1;
		}
		catch(final Throwable t) {
			Assert.fail(t.getMessage(), t);
		}
	}

	/**
	 * Tests {@link IAccountService#getAccountHistoryDataProvider()}.
	 * @throws Exception
	 */
	@Test
	public void testAccountHistoryListHandlerDataProvider() throws Exception {
		final IAccountService accountService = getEntityServiceFactory().instance(IAccountService.class);

		Account account = stubValidAccount(false);
		account = accountService.persist(account);

		final IListingDataProvider dataProvider = accountService.getAccountHistoryDataProvider();

		final Criteria<AccountHistory> criteria = new Criteria<AccountHistory>(AccountHistory.class);
		criteria.getPrimaryGroup().addCriterion("account", new PrimaryKey<Account>(Account.class, account.getId()));

		final Sorting sorting = new Sorting("transDate");

		final IListHandler<SearchResult> lh =
			ListHandlerFactory.create(criteria, sorting, ListHandlerType.PAGE, dataProvider);
		final List<SearchResult> chunk = lh.getElements(0, 25, sorting);
		Assert.assertTrue(chunk != null && chunk.size() == 1);
	}
}
