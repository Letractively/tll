package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.impl.Interface;

/**
 * Handles the persistence of {@link Interface} and child entities.
 * @author jpk
 */
public interface IInterfaceDao extends ITimeStampEntityDao<Interface>, INamedEntityDao<Interface> {
}
