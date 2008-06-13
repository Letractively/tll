package com.tll.service.entity.impl.intf;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IInterfaceDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.Interface;
import com.tll.model.key.INameKey;
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
	public InterfaceService(IInterfaceDao dao, EntityAssembler entityAssembler) {
		super(IInterfaceDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Interface> getEntityClass() {
		return Interface.class;
	}

	public Interface load(INameKey<? extends Interface> key) throws EntityNotFoundException {
		return dao.load(key);
	}

}
