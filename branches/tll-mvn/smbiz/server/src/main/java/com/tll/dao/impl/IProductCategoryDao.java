package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.ProductCategory;

/**
 * @author jpk
 */
public interface IProductCategoryDao extends ITimeStampEntityDao<ProductCategory>, INamedEntityDao<ProductCategory> {
}
