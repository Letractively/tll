package com.tll.service.entity.intf;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
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
	 * @param vfactory
	 */
	@Inject
	public InterfaceService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Interface> getEntityClass() {
		return Interface.class;
	}
}
