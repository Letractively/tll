/**
 * The Logic Lab
 */
package com.tll.service.entity.currency;

import com.tll.model.Currency;
import com.tll.service.entity.EntityNotFoundException;
import com.tll.service.entity.INamedEntityService;

/**
 * ICurrencyService
 * @author jpk
 */
public interface ICurrencyService extends INamedEntityService<Currency> {

	/**
	 * Loads a currency entity by its iso-4217 name.
	 * @param iso4217
	 * @return currency entity
	 * @throws EntityNotFoundException
	 * @throws IllegalArgumentException
	 */
	Currency loadByIso4217(String iso4217) throws EntityNotFoundException, IllegalArgumentException;
}
