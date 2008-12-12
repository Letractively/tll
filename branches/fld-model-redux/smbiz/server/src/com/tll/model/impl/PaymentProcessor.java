package com.tll.model.impl;

import com.tll.util.INameValueProvider;

/**
 * Payment Processor
 * @author jpk
 */
public enum PaymentProcessor implements INameValueProvider {
	PFP("PayFloPro - Vital"),
	CC("Cybercash");

	private final String name;

	private PaymentProcessor(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return name();
	}

}
