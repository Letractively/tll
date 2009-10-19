package com.tll.service.entity.visitor;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.Visitor;
import com.tll.service.entity.EntityService;

/**
 * VisitorService - {@link IVisitorService} impl
 * @author jpk
 */
@Transactional
public class VisitorService extends EntityService<Visitor> implements IVisitorService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public VisitorService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Visitor> getEntityClass() {
		return Visitor.class;
	}

}
