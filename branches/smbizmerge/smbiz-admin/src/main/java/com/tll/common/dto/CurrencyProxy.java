/**
 * The Logic Lab
 * @author jpk
 */
package com.tll.common.dto;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.tll.model.Currency;

/**
 * @author jpk
 */
@ProxyFor(Currency.class)
public interface CurrencyProxy extends EntityProxy {

	Long getId();

	String getIso4217();
	void setIso4217(String s);
	
	String getSymbol();
	void setSymbol(String s);
	
	float getUsdExchangeRate();
	void setUsdExchangeRate(Float f);
}
