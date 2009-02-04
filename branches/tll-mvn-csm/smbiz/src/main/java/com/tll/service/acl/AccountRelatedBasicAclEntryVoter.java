package com.tll.service.acl;

import java.util.Iterator;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthorizationServiceException;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.acegisecurity.vote.BasicAclEntryVoter;
import org.aopalliance.intercept.MethodInvocation;

import com.tll.model.AuthorityRoles;
import com.tll.model.IAccountRelatedEntity;
import com.tll.service.acl.afterinvocation.AfterInvocationProviderManager;

/**
 * ACL entry voter specifically for {@link IAccountRelatedEntity} based decisions.
 * The default decide behavior must be overridden to ensure {@link IAccountRelatedEntity} 
 * implementations are acknowledged.
 * 
 * <p>Also, the {@link BasicAclEntryVoter#vote(Authentication, Object, ConfigAttributeDefinition)}
 * method is overridden to allow administrators to have default access granted privilage.
 * This gets around adding entities that do not exist in the data store and therefore have no ACLs
 * associated with them!
 * 
 * @author jpk
 */
public class AccountRelatedBasicAclEntryVoter extends org.acegisecurity.vote.BasicAclEntryVoter {

    public AccountRelatedBasicAclEntryVoter() {
        super();
    }

    @Override
    public Class<IAccountRelatedEntity> getProcessDomainObjectClass() {
        return IAccountRelatedEntity.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object getDomainObjectInstance(Object secureObject) {
        Object[] args;
        //Class[] params;

        if (secureObject instanceof MethodInvocation) {
            MethodInvocation invocation = (MethodInvocation) secureObject;
            //params = invocation.getMethod().getParameterTypes();
            args = invocation.getArguments();
        } else {
            /*
            JoinPoint jp = (JoinPoint) secureObject;
            params = ((CodeSignature) jp.getStaticPart().getSignature()).getParameterTypes();
            args = jp.getArgs();
            */
            throw new IllegalStateException(
                    "Secure object method invocation seems to be based on the " +
                    "unsupported AspectJ methodology.");
        }

        for (Object element : args) {
            Class argClass = element.getClass();
            if (getProcessDomainObjectClass().isAssignableFrom( argClass )) {
                return element;
            }
        }

        throw new AuthorizationServiceException("Secure object: " + secureObject
            + " did not provide any argument of type: " + getProcessDomainObjectClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        if(AfterInvocationProviderManager.authenticationHasRole(authentication, AuthorityRoles.ADMINISTRATOR.toString())) {
            Iterator<ConfigAttribute> iter = config.getConfigAttributes();
            while (iter.hasNext()) {
                ConfigAttribute attr = iter.next();
                if (this.supports(attr)) {
                    return AccessDecisionVoter.ACCESS_GRANTED;
                }
            }
        }
        return super.vote(authentication, object, config);
    }

}
