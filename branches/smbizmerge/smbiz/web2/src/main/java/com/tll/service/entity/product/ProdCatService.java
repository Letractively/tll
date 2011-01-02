package com.tll.service.entity.product;

import javax.validation.ValidatorFactory;

import com.google.inject.Inject;
import com.tll.dao.IEntityDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.ProdCat;
import com.tll.service.entity.EntityService;

/**
 * ProdCatService - {@link IProdCatService} impl
 * @author jpk
 */
public class ProdCatService extends EntityService<ProdCat> implements IProdCatService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public ProdCatService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<ProdCat> getEntityClass() {
		return ProdCat.class;
	}

}
