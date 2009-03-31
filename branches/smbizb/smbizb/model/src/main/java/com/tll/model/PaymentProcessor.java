package com.tll.model;

import com.tll.INameValueProvider;

/**
 * Payment Processor
 * @author jpk
 */
public enum PaymentProcessor implements INameValueProvider<String> {
	PFP("PayFloPro - Vital"),
	CC("Cybercash");

	private final String name;

	private PaymentProcessor(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return name();
	}

}
