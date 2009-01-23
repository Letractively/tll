package com.tll.service.entity.impl.intf;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.InterfaceOptionAccount;
import com.tll.service.entity.EntityService;

/**
 * InterfaceOptionAccountService - {@link IInterfaceOptionAccountService} impl
 * @author jpk
 */
@Transactional
public class InterfaceOptionAccountService extends EntityService<InterfaceOptionAccount> implements
		IInterfaceOptionAccountService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public InterfaceOptionAccountService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<InterfaceOptionAccount> getEntityClass() {
		return InterfaceOptionAccount.class;
	}

}
