package com.tll.service.entity.impl.intf;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.EntityAssembler;
import com.tll.model.Interface;
import com.tll.service.entity.NamedEntityService;

/**
 * InterfaceService - {@link IInterfaceService} impl
 * @author jpk
 */
@Transactional
public class InterfaceService extends NamedEntityService<Interface> implements IInterfaceService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public InterfaceService(IEntityDao dao, EntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Interface> getEntityClass() {
		return Interface.class;
	}
}
