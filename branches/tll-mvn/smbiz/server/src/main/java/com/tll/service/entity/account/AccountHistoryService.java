package com.tll.service.entity.account;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AccountHistory;
import com.tll.service.entity.EntityService;
import com.tll.service.entity.IEntityAssembler;

/**
 * AccountHistoryService - {@link IAccountHistoryService} impl
 * @author jpk
 */
@Transactional
public class AccountHistoryService extends EntityService<AccountHistory> implements IAccountHistoryService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AccountHistoryService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<AccountHistory> getEntityClass() {
		return AccountHistory.class;
	}
}
