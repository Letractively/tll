package com.tll.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.tll.model.bk.BusinessKeyDef;

/**
 * The currency entity
 * @author jpk
 */
@BusinessObject(businessKeys = {
	@BusinessKeyDef(name = "Name", properties = { INamedEntity.NAME }),
	@BusinessKeyDef(name = "Symbol", properties = { "symbol" }),
	@BusinessKeyDef(name = "ISO4217", properties = { "iso4217" }) })
public class Currency extends NamedEntity {

	private static final long serialVersionUID = -1627972414433764825L;

	public static final int MAXLEN_NAME = 64;
	public static final int MAXLEN_SYMBOL = 8;
	public static final int MAXLEN_ISO_4217 = 16;

	/**
	 * The name of the dollar currency.
	 */
	public static final String DOLLAR_NAME = "dollar";

	private String iso4217;

	private String symbol;

	private float usdExchangeRate = 0f;

	@Override
	public Class<? extends IEntity> entityClass() {
		return Currency.class;
	}

	@Override
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the iso4217.
	 */
	@NotEmpty
	@Length(max = MAXLEN_ISO_4217)
	public String getIso4217() {
		return iso4217;
	}

	/**
	 * @param iso4217 The iso4217 to set.
	 */
	public void setIso4217(final String iso4217) {
		this.iso4217 = iso4217;
	}

	/**
	 * @return Returns the symbol.
	 */
	@NotEmpty
	@Length(max = MAXLEN_SYMBOL)
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol The symbol to set.
	 */
	public void setSymbol(final String symbol) {
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
	public void setUsdExchangeRate(final float usdExchangeRate) {
		this.usdExchangeRate = usdExchangeRate;
	}
}
