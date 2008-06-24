package com.tll.service.entity.impl.sitecode;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.ISiteCodeDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.SiteCode;
import com.tll.service.entity.EntityService;

/**
 * SiteCodeService - {@link ISiteCodeService} impl
 * @author jpk
 */
@Transactional
public class SiteCodeService extends EntityService<SiteCode, ISiteCodeDao> implements ISiteCodeService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public SiteCodeService(ISiteCodeDao dao, EntityAssembler entityAssembler) {
		super(ISiteCodeDao.class, dao, entityAssembler);
	}

	@Override
	public Class<SiteCode> getEntityClass() {
		return SiteCode.class;
	}
}
