package com.tll.model.impl.key;

import com.tll.model.impl.Currency;
import com.tll.model.key.BusinessKey;

public final class CurrencySymbolKey extends BusinessKey<Currency> {
  private static final long serialVersionUID = -223971607816143005L;

  private static final String[] FIELDS = new String[] { "symbol" };

  public CurrencySymbolKey() {
    super();
  }

  public CurrencySymbolKey(String symbol) {
    this();
    setSymbol(symbol);
  }

	public Class<Currency> getType() {
		return Currency.class;
	}

	@Override
  protected String[] getFields() {
    return FIELDS;
  }

  public void setEntity(Currency e) {
    e.setSymbol(getSymbol());
  }

  @Override
  protected String keyDescriptor() {
    return "Symbol";
  }

  public String getSymbol() {
    return (String) getValue(0);
  }

  public void setSymbol(String symbol) {
    setValue(0, symbol);
  }

}