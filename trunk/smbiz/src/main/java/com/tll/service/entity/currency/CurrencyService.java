package com.tll.service.entity.currency;

import javax.validation.ValidatorFactory;

import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.tll.criteria.Criteria;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.EntityNotFoundException;
import com.tll.dao.IEntityDao;
import com.tll.model.Currency;
import com.tll.model.IEntityAssembler;
import com.tll.service.entity.NamedEntityService;

/**
 * CurrencyService - {@link ICurrencyService} impl
 * @author jpk
 */
@Transactional
public class CurrencyService extends NamedEntityService<Currency> implements ICurrencyService {

	/**
	 * Constructor
	 * @param dao
	 * @param entityAssembler
	 * @param vfactory
	 */
	@Inject
	public CurrencyService(IEntityDao dao, IEntityAssembler entityAssembler, ValidatorFactory vfactory) {
		super(dao, entityAssembler, vfactory);
	}

	@Override
	public Class<Currency> getEntityClass() {
		return Currency.class;
	}

	public Currency loadByIso4217(String iso4217) throws EntityNotFoundException {
		try {
			final Criteria<Currency> criteria = new Criteria<Currency>(Currency.class);
			criteria.getPrimaryGroup().addCriterion("iso4217", iso4217, false);
			return dao.findEntity(criteria);
		}
		catch(final InvalidCriteriaException e) {
			throw new IllegalArgumentException("Unexpected invalid criteria exception occurred");
		}
	}

}
