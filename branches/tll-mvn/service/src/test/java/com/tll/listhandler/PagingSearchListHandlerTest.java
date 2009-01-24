/**
 * The Logic Lab
 * @author jpk
 * Sep 6, 2007
 */
package com.tll.listhandler;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;
import com.tll.di.JpaModule;

/**
 * PagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = "listhandler")
public class PagingSearchListHandlerTest extends DbTest {

	/**
	 * Constructor
	 */
	public PagingSearchListHandlerTest() {
		super(JpaMode.LOCAL);
	}

	@BeforeClass(alwaysRun = true)
	public void onBeforeClass() {
		beforeClass();
		getDbShell().restub();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new JpaModule(jpaMode));
		modules.add(new DaoModule(DaoMode.ORM));
		// modules.add(new EntityServiceModule());
	}

	@Test
	public void test() throws Exception {

		/*
		IAccountService accountService = injector.getInstance(IAccountService.class);

		List<Account> allAccounts = accountService.loadAll();
		assert allAccounts != null && allAccounts.size() > 0 : "No accounts exist - test can't run";
		assert allAccounts.size() >= 10 : "At least 10 list elements must be present for this test";
		final int pageSize = 3;

		Criteria<Account> criteria = new Criteria<Account>(Account.class);
		Sorting sorting = new Sorting(new SortColumn(INamedEntity.NAME));
		IListHandler<SearchResult<Account>> listHandler =
				ListHandlerFactory.create(criteria, sorting, ListHandlerType.PAGE, accountService);

		List<SearchResult<Account>> list;

		list = listHandler.getElements(0, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize * 2, pageSize, sorting);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		List<SearchResult<Account>> alist = listHandler.getElements(0, allAccounts.size(), sorting);
		assert alist.size() == allAccounts.size();

		for(int i = 0; i < allAccounts.size(); i++) {
			Account account = alist.get(i).getEntity();
			assert account != null : "Empty account in list";
		}
		*/
	}

}
