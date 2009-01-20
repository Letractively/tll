package com.tll.service.entity.impl.intf;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IInterfaceOptionAccountDao;
import com.tll.model.EntityAssembler;
import com.tll.model.InterfaceOptionAccount;
import com.tll.service.entity.EntityService;

/**
 * InterfaceOptionAccountService - {@link IInterfaceOptionAccountDao} impl
 * @author jpk
 */
@Transactional
public class InterfaceOptionAccountService extends EntityService<InterfaceOptionAccount, IInterfaceOptionAccountDao> implements IInterfaceOptionAccountService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public InterfaceOptionAccountService(IInterfaceOptionAccountDao dao, EntityAssembler entityAssembler) {
		super(IInterfaceOptionAccountDao.class, dao, entityAssembler);
	}

	@Override
	public Class<InterfaceOptionAccount> getEntityClass() {
		return InterfaceOptionAccount.class;
	}

}
