/**
 * The Logic Lab
 * @author jpk
 * Dec 15, 2008
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * CreditCardValidator - Credit card validation based on Hibernate's
 * <code>CreditCardNumberValidator</code> class in hibernate-validator.jar.
 * @author jpk
 */
public class CreditCardValidator implements IValidator {

	public static final CreditCardValidator INSTANCE = new CreditCardValidator();

	public Object validate(Object value) throws ValidationException {
		if(value != null) {
			if(!(value instanceof String)) throw new ValidationException("Not a string.");
			String creditCard = (String) value;
			char[] chars = creditCard.toCharArray();

			List<Integer> ints = new ArrayList<Integer>();
			for(char c : chars) {
				if(Character.isDigit(c)) ints.add(c - '0');
			}
			int length = ints.size();
			int sum = 0;
			boolean even = false;
			for(int index = length - 1; index >= 0; index--) {
				int digit = ints.get(index);
				if(even) {
					digit *= 2;
				}
				if(digit > 9) {
					digit = digit / 10 + digit % 10;
				}
				sum += digit;
				even = !even;
			}
			if(!(sum % 10 == 0)) {
				throw new ValidationException("Invalid credit card number.");
			}
		}
		return value;
	}

}
