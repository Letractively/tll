package com.tll.service.entity.sitecode;

import javax.validation.ValidatorFactory;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.SiteCode;
import com.tll.service.entity.NamedEntityService;

/**
 * SiteCodeService - {@link ISiteCodeService} impl
 * @author jpk
 */
public class SiteCodeService extends NamedEntityService<SiteCode> implements ISiteCodeService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public SiteCodeService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<SiteCode> getEntityClass() {
		return SiteCode.class;
	}
}
