package com.tll.service.entity.sitecode;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.SiteCode;
import com.tll.service.entity.NamedEntityService;

/**
 * SiteCodeService - {@link ISiteCodeService} impl
 * @author jpk
 */
@Transactional
public class SiteCodeService extends NamedEntityService<SiteCode> implements ISiteCodeService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public SiteCodeService(IEntityDao dao, IEntityAssembler entityAssembler) {
		super(dao, entityAssembler);
	}

	@Override
	public Class<SiteCode> getEntityClass() {
		return SiteCode.class;
	}
}
