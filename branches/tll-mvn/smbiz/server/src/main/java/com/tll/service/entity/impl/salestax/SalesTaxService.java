package com.tll.service.entity.impl.salestax;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.EntityAssembler;
import com.tll.model.SalesTax;
import com.tll.service.entity.EntityService;

/**
 * SalesTaxService - {@link ISalesTaxService} impl
 * @author jpk
 */
@Transactional
public class SalesTaxService extends EntityService<SalesTax> implements ISalesTaxService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public SalesTaxService(IEntityDao dao, EntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<SalesTax> getEntityClass() {
		return SalesTax.class;
	}
}
