package com.tll.service.acl.afterinvocation;

import java.util.Iterator;

/**
 * Clone of Acegi's {@link org.acegisecurity.afterinvocation.Filterer}
 * necessary because said interface does not have public access.
 */
interface IFilterer {
    //~ Methods ========================================================================================================

    /**
     * Gets the filtered collection or array.
     *
     * @return the filtered collection or array
     */
    public Object getFilteredObject();

    /**
     * Returns an iterator over the filtered collection or array.
     *
     * @return an Iterator
     */
    public Iterator<Object> iterator();

    /**
     * Removes the the given object from the resulting list.
     *
     * @param object the object to be removed
     */
    public void remove(Object object);
}
