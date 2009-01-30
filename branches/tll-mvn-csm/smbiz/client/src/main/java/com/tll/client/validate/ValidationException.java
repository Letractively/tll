/**
 * The Logic Lab
 * @author jpk
 * Apr 13, 2008
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * ValidationException
 * @author jpk
 */
public class ValidationException extends Exception {

	private final List<Msg> errors;

	private final Map<Widget, List<Msg>> sourcedErrors;

	/**
	 * Constructor
	 * @param error
	 */
	public ValidationException(Msg error) {
		super();
		errors = new ArrayList<Msg>(1);
		errors.add(error);
		sourcedErrors = null;
	}

	/**
	 * Constructor
	 * @param errors
	 */
	public ValidationException(List<Msg> errors) {
		super();
		this.errors = errors;
		sourcedErrors = null;
	}

	/**
	 * Constructor
	 * @param error
	 */
	public ValidationException(String error) {
		this(new Msg(error, MsgLevel.ERROR));
	}

	/**
	 * Constructor - Use when this exception contains error messages for
	 * <em>more than one</em> target.
	 * @param sourcedErrors Map of sourced error messages keyed by the source.
	 */
	public ValidationException(Map<Widget, List<Msg>> sourcedErrors) {
		super();
		this.errors = null;
		this.sourcedErrors = sourcedErrors;
	}

	/**
	 * @return The error messages.
	 */
	public List<Msg> getErrors() {
		return errors;
	}

	/**
	 * @return The error messages for multiple sources.
	 */
	public Map<Widget, List<Msg>> getSourcedErrors() {
		return sourcedErrors;
	}
}
