package com.tll.model.impl.key;

import com.tll.model.impl.Currency;
import com.tll.model.key.NameKey;

public final class CurrencyNameKey extends NameKey<Currency> {

	private static final long serialVersionUID = 655052034293938722L;

	/**
	 * Constructor
	 */
	public CurrencyNameKey() {
		super(Currency.class);
	}

	/**
	 * Constructor
	 * @param name
	 */
	public CurrencyNameKey(String name) {
		super(Currency.class, name);
	}

}