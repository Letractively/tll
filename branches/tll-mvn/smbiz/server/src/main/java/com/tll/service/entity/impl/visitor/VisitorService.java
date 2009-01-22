package com.tll.service.entity.impl.visitor;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.EntityAssembler;
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
	 */
	@Inject
	public VisitorService(IEntityDao dao, EntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<Visitor> getEntityClass() {
		return Visitor.class;
	}

}
