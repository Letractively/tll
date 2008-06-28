package com.tll.dao.impl;

import com.tll.dao.INamedEntityDao;
import com.tll.dao.ITimeStampEntityDao;
import com.tll.model.impl.SiteCode;



/**
 * @author jpk
 */
public interface ISiteCodeDao extends ITimeStampEntityDao<SiteCode>, INamedEntityDao<SiteCode> {
}