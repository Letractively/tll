package com.tll.model.test;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.IEntity;
import com.tll.model.INamedEntity;
import com.tll.model.NamedEntity;
import com.tll.model.schema.BusinessKeyDef;
import com.tll.model.schema.BusinessObject;

/**
 * The currency entity
 * @author jpk
 */
@BusinessObject(businessKeys = {
	@BusinessKeyDef(name = "Name", properties = { INamedEntity.NAME }),
	@BusinessKeyDef(name = "Symbol", properties = { "symbol" }),
	@BusinessKeyDef(name = "ISO4217", properties = { "iso4217" })
})
public class Currency extends NamedEntity {
	private static final long serialVersionUID = -6944161437125857044L;

	/**
	 * The name of the dollar currency.
	 */
	public static final String DOLLAR_NAME = "dollar";

	private String iso4217;

	private String symbol;

	private float usdExchangeRate = 0f;

	public Class<? extends IEntity> entityClass() {
		return Currency.class;
	}

	@NotEmpty
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the iso4217.
	 */
	@NotEmpty
	public String getIso4217() {
		return iso4217;
	}

	/**
	 * @param iso4217 The iso4217 to set.
	 */
	public void setIso4217(String iso4217) {
		this.iso4217 = iso4217;
	}

	/**
	 * @return Returns the symbol.
	 */
	@NotEmpty
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol The symbol to set.
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return Returns the usdExchangeRate.
	 */
	@NotNull
	// @Size(min = 0, max = 9999)
	public float getUsdExchangeRate() {
		return usdExchangeRate;
	}

	/**
	 * @param usdExchangeRate The usdExchangeRate to set.
	 */
	public void setUsdExchangeRate(float usdExchangeRate) {
		this.usdExchangeRate = usdExchangeRate;
	}
}
