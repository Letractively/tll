package com.tll.service.entity.impl.currency;

import javax.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.SystemError;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.impl.ICurrencyDao;
import com.tll.model.IEntityAssembler;
import com.tll.model.impl.Currency;
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
	public CurrencyService(ICurrencyDao dao, IEntityAssembler entityAssembler) {
		super(ICurrencyDao.class, dao, entityAssembler);
	}

	@Override
	public Class<Currency> getEntityClass() {
		return Currency.class;
	}

	public Currency loadByIso4217(String iso4217) throws EntityNotFoundException {
		try {
			Criteria<Currency> criteria = new Criteria<Currency>(Currency.class);
			criteria.getPrimaryGroup().addCriterion("iso4217", iso4217, false);
			return dao.findEntity(criteria);
		}
		catch(InvalidCriteriaException e) {
			throw new SystemError("Unexpected invalid criteria exception occurred");
		}
	}

}
