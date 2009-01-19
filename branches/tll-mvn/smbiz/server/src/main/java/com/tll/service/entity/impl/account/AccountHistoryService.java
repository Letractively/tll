package com.tll.service.entity.impl.account;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAccountHistoryDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.impl.AccountHistory;
import com.tll.service.entity.EntityService;

/**
 * AccountHistoryService - {@link IAccountHistoryService} impl
 * @author jpk
 */
@Transactional
public class AccountHistoryService extends EntityService<AccountHistory, IAccountHistoryDao> implements IAccountHistoryService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AccountHistoryService(IAccountHistoryDao dao, IEntityAssembler entityAssembler) {
		super(IAccountHistoryDao.class, dao, entityAssembler);
	}

	@Override
	public Class<AccountHistory> getEntityClass() {
		return AccountHistory.class;
	}
}
