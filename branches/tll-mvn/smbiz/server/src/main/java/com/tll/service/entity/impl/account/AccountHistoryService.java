package com.tll.service.entity.impl.account;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AccountHistory;
import com.tll.model.EntityAssembler;
import com.tll.service.entity.EntityService;

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
	public AccountHistoryService(IEntityDao dao, EntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<AccountHistory> getEntityClass() {
		return AccountHistory.class;
	}
}
