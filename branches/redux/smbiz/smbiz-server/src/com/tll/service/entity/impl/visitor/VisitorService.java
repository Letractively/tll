package com.tll.service.entity.impl.visitor;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IVisitorDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.Visitor;
import com.tll.service.entity.EntityService;

/**
 * VisitorService - {@link IVisitorService} impl
 * @author jpk
 */
@Transactional
public class VisitorService extends EntityService<Visitor, IVisitorDao> implements IVisitorService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public VisitorService(IVisitorDao dao, EntityAssembler entityAssembler) {
		super(IVisitorDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Visitor> getEntityClass() {
		return Visitor.class;
	}

}
