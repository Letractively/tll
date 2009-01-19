package com.tll.service.entity.impl.intf;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IInterfaceDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.impl.Interface;
import com.tll.service.entity.EntityService;

/**
 * InterfaceService - {@link IInterfaceService} impl
 * @author jpk
 */
@Transactional
public class InterfaceService extends EntityService<Interface, IInterfaceDao> implements IInterfaceService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public InterfaceService(IInterfaceDao dao, IEntityAssembler entityAssembler) {
		super(IInterfaceDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Interface> getEntityClass() {
		return Interface.class;
	}
}
