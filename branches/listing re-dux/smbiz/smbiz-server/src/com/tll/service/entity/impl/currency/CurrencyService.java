package com.tll.service.entity.impl.currency;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.criteria.CriteriaFactory;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.impl.ICurrencyDao;
import com.tll.model.EntityAssembler;
import com.tll.model.impl.Currency;
import com.tll.model.key.INameKey;
import com.tll.service.entity.EntityService;

/**
 * CurrencyService - {@link ICurrencyService} impl
 * @author jpk
 */
@Transactional
public class CurrencyService extends EntityService<Currency, ICurrencyDao> implements ICurrencyService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 */
	@Inject
	public CurrencyService(ICurrencyDao dao, EntityAssembler entityAssembler) {
		super(ICurrencyDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Currency> getEntityClass() {
		return Currency.class;
	}

	public Currency load(INameKey<? extends Currency> key) throws EntityNotFoundException {
		return dao.load(key);
	}

	public Currency loadByIso4217(String iso4217) throws EntityNotFoundException {
		try {
			return dao.findEntity(CriteriaFactory.buildEntityCriteria(getEntityClass(), "iso4217", iso4217));
		}
		catch(InvalidCriteriaException e) {
			throw new SystemError("Unexpected invalid criteria exception occurred");
		}
	}

}
