package com.tll.service.entity.account;

import com.tll.listhandler.IListingDataProvider;
import com.tll.model.Account;
import com.tll.service.entity.INamedEntityService;
import com.tll.service.entity.IStatefulEntityService;

public interface IAccountService extends INamedEntityService<Account>, IStatefulEntityService<Account> {

	/**
	 * Provides the ability to list-handle account histories.
	 * @return An account history list handler data provider.
	 */
	IListingDataProvider getAccountHistoryDataProvider();
}