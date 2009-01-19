package com.tll.service.entity.impl.salestax;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.ISalesTaxDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.impl.SalesTax;
import com.tll.service.entity.EntityService;

/**
 * SalesTaxService - {@link ISalesTaxService} impl
 * @author jpk
 */
@Transactional
public class SalesTaxService extends EntityService<SalesTax, ISalesTaxDao> implements ISalesTaxService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public SalesTaxService(ISalesTaxDao dao, IEntityAssembler entityAssembler) {
		super(ISalesTaxDao.class, dao, entityAssembler);
	}

	@Override
	public Class<SalesTax> getEntityClass() {
		return SalesTax.class;
	}
}
