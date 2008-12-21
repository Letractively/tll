/**
 * 
 */
package com.tll.dao.impl.hibernate;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IProductCategoryDao;
import com.tll.model.impl.ProductCategory;
import com.tll.model.key.NameKey;

/**
 * ProductCategoryDao
 * @author jpk
 */
public class ProductCategoryDao extends TimeStampEntityDao<ProductCategory> implements IProductCategoryDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public ProductCategoryDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<ProductCategory> getEntityClass() {
		return ProductCategory.class;
	}

	@Override
	public ProductCategory load(NameKey<? extends ProductCategory> nameKey) {
		return (ProductCategory) loadByName(nameKey);
	}
}
