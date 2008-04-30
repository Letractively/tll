package com.tll.service.acl.afterinvocation;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.AfterInvocationManager;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.GrantedAuthority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tll.model.impl.Authority;

/**
 * Extension of {@link com.tll.service.acl.afterinvocation.AfterInvocationProviderManager}
 * for custom decision making based on the authentication.
 * @author jpk
 */
public class AfterInvocationProviderManager extends
        org.acegisecurity.afterinvocation.AfterInvocationProviderManager
        implements AfterInvocationManager {
    private static final Log log = LogFactory.getLog(AfterInvocationProviderManager.class);

    /**
     * Constructor
     */
    public AfterInvocationProviderManager() {
        super();
    }

    @Override
    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject) throws AccessDeniedException {
        // allow if authentication has has an administrative role
        if( authenticationHasRole( authentication, Authority.ROLE_ADMINISTRATOR ) ) {
            return returnedObject;
        }
        return super.decide(authentication, object, config, returnedObject);
    }

    /**
     * Does the given authentication have the given role?
     * @param authentication
     * @param role
     * @return true/false
     */
    public static final boolean authenticationHasRole(Authentication authentication, String role) {
        GrantedAuthority[] gas = authentication.getAuthorities();
        for( GrantedAuthority ga : gas) {
            if( role.equals(ga.getAuthority())) {
                log.debug("Allowing access - Provided authentication has administrative role");
                return true;
            }
        }
        return false;
    }
}
