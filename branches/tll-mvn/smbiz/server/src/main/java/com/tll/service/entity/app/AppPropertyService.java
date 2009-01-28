/*
 * The Logic Lab 
 */
package com.tll.service.entity.app;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AppProperty;
import com.tll.service.entity.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;

/**
 * AppPropertyService - {@link IAppPropertyService} impl
 * @author jpk
 */
@Transactional
public class AppPropertyService extends NamedEntityService<AppProperty> implements IAppPropertyService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AppPropertyService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<AppProperty> getEntityClass() {
		return AppProperty.class;
	}
}
