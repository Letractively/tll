package com.tll.service.entity.impl.product;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.dao.impl.IProdCatDao;
import com.tll.model.EntityAssembler;
import com.tll.model.ProdCat;
import com.tll.service.entity.EntityService;

/**
 * ProdCatService - {@link IProdCatService} impl
 * @author jpk
 */
@Transactional
public class ProdCatService extends EntityService<ProdCat, IProdCatDao> implements IProdCatService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public ProdCatService(IProdCatDao dao, EntityAssembler entityAssembler) {
		super(IProdCatDao.class, dao, entityAssembler);
	}

	@Override
	public Class<ProdCat> getEntityClass() {
		return ProdCat.class;
	}

}
