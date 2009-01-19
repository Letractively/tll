package com.tll.service.entity.impl.product;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IProductInventoryDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.impl.ProductInventory;
import com.tll.service.entity.EntityService;

/**
 * ProductService - {@link IProductService} impl
 * @author jpk
 */
@Transactional
public class ProductService extends EntityService<ProductInventory, IProductInventoryDao> implements IProductService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ProductService(IProductInventoryDao dao, IEntityAssembler entityAssembler) {
		super(IProductInventoryDao.class, dao, entityAssembler);
	}

	@Override
	public Class<ProductInventory> getEntityClass() {
		return ProductInventory.class;
	}
}
