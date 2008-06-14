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
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.ICriteria;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.DaoModule;
import com.tll.guice.EntityServiceModule;
import com.tll.model.INamedEntity;
import com.tll.model.impl.Account;
import com.tll.service.entity.impl.account.IAccountService;

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
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);

		DaoModule dm = new DaoModule(DaoMode.ORM);
		modules.add(dm);

		EntityServiceModule esm = new EntityServiceModule();
		modules.add(esm);
	}

	@Test
	public void test() throws Exception {

		IAccountService accountService = injector.getInstance(IAccountService.class);

		List<Account> allAccounts = accountService.loadAll();
		assert allAccounts != null && allAccounts.size() > 0 : "No accounts exist - test can't run";
		assert allAccounts.size() >= 10 : "At least 10 list elements must be present for this test";
		final int pageSize = 3;

		ICriteria<Account> c = CriteriaFactory.buildEntityCriteria(Account.class);
		Sorting sorting = new Sorting(new SortColumn(INamedEntity.NAME));
		IListHandler<SearchResult<Account>> listHandler =
				ListHandlerFactory.create(c, sorting, ListHandlerType.PAGE, accountService);

		List<SearchResult<Account>> list;

		list = listHandler.getElements(0, pageSize, null);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize, pageSize * 2, null);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		list = listHandler.getElements(pageSize * 2, pageSize * 3, null);
		assert (list != null && list.size() == pageSize) : "getElements() size mismatch";

		List<SearchResult<Account>> alist = listHandler.getElements(0, pageSize, sorting);
		for(int i = 0; i < allAccounts.size(); i++) {
			Account account = alist.get(i).getEntity();
			assert account != null : "Empty account in list";
		}
	}

}
