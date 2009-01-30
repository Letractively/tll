/**
 * 
 */
package com.tll.client.validate;

/**
 * IValidator - Contract for validator implementations.
 * <p>
 * <strong>IMPT: </strong>Implementations having state <em>must</em> override
 * equals() and hashCode() to maintain the ability to logically identify
 * validator implementations thus enabling the ability to determine whether a
 * particular validator exists in a collection which is key for composite
 * validator implementations.
 * @author jpk
 */
public interface IValidator {

	/**
	 * Validate the given value returning the "validated" value.
	 * <p>
	 * E.g.: A numeric validator will return a number when validation passes.
	 * @param value value to validate
	 * @return The validated value possibly of a different type
	 * @throws ValidationException When the value is found invalid.
	 */
	Object validate(Object value) throws ValidationException;
}
