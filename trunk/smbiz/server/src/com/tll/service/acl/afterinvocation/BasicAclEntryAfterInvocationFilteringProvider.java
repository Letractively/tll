package com.tll.service.acl.afterinvocation;

import java.util.Iterator;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.acl.AclEntry;
import org.acegisecurity.acl.AclManager;
import org.acegisecurity.acl.basic.BasicAclEntry;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;


/**
 * Generalized filtering provider supporting maps in addition to collections.
 * Much of this code is a clone of Acegi's stock collection filtering provider
 * class due to the inability to extend from it.
 * 
 * @author jpk
 */
public class BasicAclEntryAfterInvocationFilteringProvider implements AfterInvocationProvider /*,  InitializingBean */ {
    protected static final Log logger = LogFactory.getLog(BasicAclEntryAfterInvocationFilteringProvider.class);

    // ~ Instance fields
    // ================================================================================================

    private AclManager aclManager;
    private Class<Object> processDomainObjectClass = Object.class;
    private String processConfigAttribute = "AFTER_ACL_COLLECTION_READ";
    private int[] requirePermission = {SimpleAclEntry.READ};
    
    private FiltererFactory filtererFactory;
    public void setFiltererFactory(FiltererFactory filtererFactory) {
        this.filtererFactory = filtererFactory;
    }

    // ~ Methods
    // ========================================================================================================

    /*
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(processConfigAttribute, "A processConfigAttribute is mandatory");
        Assert.notNull(aclManager, "An aclManager is mandatory");

        if ((requirePermission == null) || (requirePermission.length == 0)) {
            throw new IllegalArgumentException("One or more requirePermission entries is mandatory");
        }
    }
    */
    
    @SuppressWarnings("unchecked")
    public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config,
        Object returnedObject) throws AccessDeniedException {
        Iterator iter = config.getConfigAttributes();

        while (iter.hasNext()) {
            ConfigAttribute attr = (ConfigAttribute) iter.next();

            if (this.supports(attr)) {
                // Need to process the Collection for this invocation
                if (returnedObject == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Return object is null, skipping");
                    }

                    return null;
                }

                // obtain a filterer
                IFilterer filterer = filtererFactory.create(returnedObject);

                // Locate unauthorised Collection elements
                Iterator collectionIter = filterer.iterator();

                while (collectionIter.hasNext()) {
                    Object domainObject = collectionIter.next();

                    boolean hasPermission = false;

                    AclEntry[] acls = null;

                    if (domainObject == null) {
                        hasPermission = true;
                    } else if (!processDomainObjectClass.isAssignableFrom(domainObject.getClass())) {
                        hasPermission = true;
                    } else {
                        acls = aclManager.getAcls(domainObject, authentication);
                    }

                    if ((acls != null) && (acls.length != 0)) {
                        for (AclEntry element : acls) {
                            // Locate processable AclEntrys
                            if (element instanceof BasicAclEntry) {
                                BasicAclEntry processableAcl = (BasicAclEntry) element;

                                for (int element0 : requirePermission) {
                                    if (processableAcl.isPermitted(element0)) {
                                        hasPermission = true;

                                        if (logger.isDebugEnabled()) {
                                            logger.debug("Principal is authorised for element: " + domainObject
                                                + " due to ACL: " + processableAcl.toString());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!hasPermission) {
                        filterer.remove(domainObject);

                        if (logger.isDebugEnabled()) {
                            logger.debug("Principal is NOT authorised for element: " + domainObject);
                        }
                    }
                }

                return filterer.getFilteredObject();
            }
        }

        return returnedObject;
    }

    public AclManager getAclManager() {
        return aclManager;
    }

    public String getProcessConfigAttribute() {
        return processConfigAttribute;
    }

    public int[] getRequirePermission() {
        return requirePermission;
    }

    public void setAclManager(AclManager aclManager) {
        this.aclManager = aclManager;
    }

    public void setProcessConfigAttribute(String processConfigAttribute) {
        this.processConfigAttribute = processConfigAttribute;
    }

    public void setProcessDomainObjectClass(Class<Object> processDomainObjectClass) {
        Assert.notNull(processDomainObjectClass, "processDomainObjectClass cannot be set to null");
        this.processDomainObjectClass = processDomainObjectClass;
    }

    public void setRequirePermission(int[] requirePermission) {
        this.requirePermission = requirePermission;
    }

    public boolean supports(ConfigAttribute attribute) {
        if ((attribute.getAttribute() != null) && attribute.getAttribute().equals(getProcessConfigAttribute())) {
            return true;
        }
        return false;
    }

    /**
     * This implementation supports any type of class, because it does not query
     * the presented secure object.
     * 
     * @param clazz
     *          the secure object
     * @return always <code>true</code>
     */
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return true;
    }
}
