package com.tll.service.acl;

import org.acegisecurity.acl.basic.AclObjectIdentity;
import org.acegisecurity.acl.basic.NamedEntityObjectIdentity;

import com.tll.model.IAccountRelatedEntity;

/**
 * ACL provider for {@link IAccountRelatedEntity}
 * implementing entities.
 * 
 * @author jpk
 */
public class AccountRelatedAclProviderManager extends BasicAclProviderManager<IAccountRelatedEntity> {

  public AccountRelatedAclProviderManager() {
    super();
    this.setRestrictSupportToClass(IAccountRelatedEntity.class);
  }

  public Class<IAccountRelatedEntity> getSupportedClass() {
    return IAccountRelatedEntity.class;
  }

  @Override
  protected AclObjectIdentity obtainIdentity(Object domainInstance) {
    if (domainInstance instanceof IAccountRelatedEntity) {
      final Integer categoryId = ((IAccountRelatedEntity) domainInstance).accountId();
      return categoryId == null ? null : new NamedEntityObjectIdentity(IAccountRelatedEntity.class.getName(),
          categoryId.toString());
    }
    return super.obtainIdentity(domainInstance);
  }

}
