package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.NotNull;

import com.tll.model.EntityBase;
import com.tll.model.IChildEntity;
import com.tll.model.IEntity;

/**
 * product cateory binder entity
 * 
 * @author jpk
 */
@Entity
@Table(name="prod_cat")
public class ProdCat extends EntityBase implements IChildEntity<ProductInventory>, IAccountRelatedEntity {
  private static final long serialVersionUID = -8353863817821839414L;

  protected boolean isFeaturedProduct = false;

  protected ProductInventory product;

  protected ProductCategory category;

  public Class<? extends IEntity> entityClass() {
    return ProdCat.class;
  }

  /**
   * @return Returns the bIsFeaturedProduct.
   */
  @Column(name="is_featured_product")
  @NotNull
  public boolean getIsFeaturedProduct() {
    return isFeaturedProduct;
  }

  /**
   * @param isFeaturedProduct
   *          The bIsFeaturedProduct to set.
   */
  public void setIsFeaturedProduct(boolean isFeaturedProduct) {
    this.isFeaturedProduct = isFeaturedProduct;
  }

  /**
   * @return Returns the category.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prodcat_id")
  @NotNull
  public ProductCategory getCategory() {
    return category;
  }

  /**
   * @param category
   *          The category to set.
   */
  public void setCategory(ProductCategory category) {
    this.category = category;
  }

  /**
   * @return Returns the product.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "prodinv_id")
  @NotNull
  public ProductInventory getProduct() {
    return product;
  }

  /**
   * @param product
   *          The product to set.
   */
  public void setProduct(ProductInventory product) {
    this.product = product;
  }

  @Transient
  public ProductInventory getParent() {
    return getProduct();
  }

  public void setParent(ProductInventory e) {
    setProduct(e);
  }

  public Integer accountId() {
    try {
      return getProduct().getAccount().getId();
    }
    catch(NullPointerException npe) {
      LOG.warn("Unable to provide related account id due to a NULL nested entity");
      return null;
    }
  }
  
  @Override
  protected ToStringBuilder toStringBuilder() {
    return super.toStringBuilder()
    
    .append("isFeaturedProduct", isFeaturedProduct)
    .append("product", product==null? "NULL" : product.descriptor())
    .append("category", category==null? "NULL" : category.descriptor());
  }
}