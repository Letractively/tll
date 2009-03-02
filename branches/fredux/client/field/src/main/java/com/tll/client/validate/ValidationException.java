/**
 * The Logic Lab
 * @author jpk
 * Apr 13, 2008
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.List;

import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * ValidationException - Default un-sourced validation error.
 * @author jpk
 */
@SuppressWarnings("serial")
public class ValidationException extends Exception implements IValidationError {

	private final List<Msg> errors;

	/**
	 * Constructor
	 * @param error
	 */
	public ValidationException(Msg error) {
		errors = new ArrayList<Msg>(1);
		errors.add(error);
	}

	/**
	 * Constructor
	 * @param errors
	 */
	public ValidationException(List<Msg> errors) {
		this.errors = errors;
	}

	/**
	 * Constructor
	 * @param error
	 */
	public ValidationException(String error) {
		this(new Msg(error, MsgLevel.ERROR));
	}

	@Override
	public Type getType() {
		return Type.UNSOURCED; // the default
	}

	/**
	 * @return The error messages.
	 */
	public List<Msg> getErrors() {
		return errors;
	}
}
