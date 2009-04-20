package com.tll.service.entity.product;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ProductInventory;
import com.tll.service.entity.EntityService;

/**
 * ProductService - {@link IProductService} impl
 * @author jpk
 */
@Transactional
public class ProductService extends EntityService<ProductInventory> implements IProductService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ProductService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<ProductInventory> getEntityClass() {
		return ProductInventory.class;
	}
}
