package com.tll.service.entity.product;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ProductCategory;
import com.tll.service.entity.NamedEntityService;

/**
 * ProductCategoryService - {@link IProductCategoryService} impl
 * @author jpk
 */
@Transactional
public class ProductCategoryService extends NamedEntityService<ProductCategory> implements IProductCategoryService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ProductCategoryService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<ProductCategory> getEntityClass() {
		return ProductCategory.class;
	}
}
