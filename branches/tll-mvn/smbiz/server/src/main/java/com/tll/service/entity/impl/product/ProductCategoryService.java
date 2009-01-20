package com.tll.service.entity.impl.product;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IProductCategoryDao;
import com.tll.model.EntityAssembler;
import com.tll.model.ProductCategory;
import com.tll.service.entity.EntityService;

/**
 * ProductCategoryService - {@link IProductCategoryService} impl
 * @author jpk
 */
@Transactional
public class ProductCategoryService extends EntityService<ProductCategory, IProductCategoryDao> implements IProductCategoryService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ProductCategoryService(IProductCategoryDao dao, EntityAssembler entityAssembler) {
		super(IProductCategoryDao.class, dao, entityAssembler);
	}

	@Override
	public Class<ProductCategory> getEntityClass() {
		return ProductCategory.class;
	}
}
