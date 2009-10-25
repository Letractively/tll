/*
 * The Logic Lab
 */
package com.tll.service.entity.app;

import javax.validation.ValidatorFactory;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.AppProperty;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;

/**
 * AppPropertyService - {@link IAppPropertyService} impl
 * @author jpk
 */
public class AppPropertyService extends NamedEntityService<AppProperty> implements IAppPropertyService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public AppPropertyService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<AppProperty> getEntityClass() {
		return AppProperty.class;
	}
}
