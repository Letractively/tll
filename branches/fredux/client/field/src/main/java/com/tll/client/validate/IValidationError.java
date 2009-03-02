/**
 * The Logic Lab
 * @author jpk
 * Mar 2, 2009
 */
package com.tll.client.validate;

import java.util.List;

import com.tll.common.msg.Msg;


/**
 * IValidationError
 * @author jpk
 */
public interface IValidationError {

	/**
	 * Type - Categorizes the error.
	 * @author jpk
	 */
	public static enum Type {
		/**
		 * there is no source info (default).
		 */
		UNSOURCED,
		/**
		 * there is source information.
		 */
		SOURCED;
	}

	/**
	 * @return the type of error.
	 */
	Type getType();

	/**
	 * @return the errors.
	 */
	List<Msg> getErrors();
}
