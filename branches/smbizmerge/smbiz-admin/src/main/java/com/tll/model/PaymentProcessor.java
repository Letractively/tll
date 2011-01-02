package com.tll.model;

import com.tll.IMarshalable;
import com.tll.INameValueProvider;

/**
 * Payment Processor
 * @author jpk
 */
public enum PaymentProcessor implements INameValueProvider<String>, IMarshalable {
	PFP("PayFloPro - Vital"),
	CC("Cybercash");

	private final String name;

	private PaymentProcessor(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return name();
	}

}
