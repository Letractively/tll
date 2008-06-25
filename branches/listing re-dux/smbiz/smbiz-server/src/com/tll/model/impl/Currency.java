package com.tll.model.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Range;

import com.tll.client.model.IPropertyValue;
import com.tll.client.model.StringPropertyValue;
import com.tll.model.IEntity;
import com.tll.model.NamedEntity;
import com.tll.model.key.BusinessKey;

/**
 * The currency entity
 * @author jpk
 */
@Entity
@Table(name = "currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

	public Class<? extends IEntity> entityClass() {
		return Currency.class;
	}

	@Column
	@NotEmpty
	@Length(max = MAXLEN_NAME)
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the iso4217.
	 */
	@Column(name = "iso_4217", unique = true)
	@NotEmpty
	@Length(max = MAXLEN_ISO_4217)
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
	@Column(nullable = false, unique = true)
	@NotEmpty
	@Length(max = MAXLEN_SYMBOL)
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
	@Column(name = "usd_exchange_rate", precision = 8, scale = 4)
	@NotNull
	@Range(min = 0L, max = 9999L)
	public float getUsdExchangeRate() {
		return usdExchangeRate;
	}

	/**
	 * @param usdExchangeRate The usdExchangeRate to set.
	 */
	public void setUsdExchangeRate(float usdExchangeRate) {
		this.usdExchangeRate = usdExchangeRate;
	}

	@Override
	@Transient
	public BusinessKey[] getBusinessKeys() {
		return new BusinessKey[] {
			new BusinessKey(Currency.class, "Name", new IPropertyValue[] { new StringPropertyValue("name", getName()) }),
			new BusinessKey(Currency.class, "Symbol", new IPropertyValue[] { new StringPropertyValue("symbol", getSymbol()) }),
			new BusinessKey(Currency.class, "ISO4217",
					new IPropertyValue[] { new StringPropertyValue("iso4217", getIso4217()) }) };
	}

}
