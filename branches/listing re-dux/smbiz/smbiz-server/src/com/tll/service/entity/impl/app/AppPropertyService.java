/*
 * The Logic Lab 
 */
package com.tll.service.entity.impl.app;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IAppPropertyDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.AppProperty;
import com.tll.model.key.INameKey;
import com.tll.service.entity.EntityService;

/**
 * AppPropertyService - {@link IAppPropertyService} impl
 * @author jpk
 */
@Transactional
public class AppPropertyService extends EntityService<AppProperty, IAppPropertyDao> implements IAppPropertyService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public AppPropertyService(IAppPropertyDao dao, EntityAssembler entityAssembler) {
		super(IAppPropertyDao.class, dao, entityAssembler);
	}

	@Override
	public Class<AppProperty> getEntityClass() {
		return AppProperty.class;
	}

	public AppProperty load(INameKey<? extends AppProperty> key) throws EntityNotFoundException {
		return dao.load(key);
	}

}