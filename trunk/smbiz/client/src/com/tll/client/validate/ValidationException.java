/**
 * The Logic Lab
 * @author jpk
 * Apr 13, 2008
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.List;

import com.tll.client.msg.Msg;
import com.tll.client.msg.Msg.MsgLevel;

/**
 * ValidationException
 * @author jpk
 */
public class ValidationException extends Exception implements IValidationFeedback {

	private final List<Msg> msgs;

	/**
	 * Constructor
	 * @param msg
	 */
	public ValidationException(Msg msg) {
		super();
		msgs = new ArrayList<Msg>(1);
		msgs.add(msg);
	}

	/**
	 * Constructor
	 * @param msgs
	 */
	public ValidationException(List<Msg> msgs) {
		super();
		this.msgs = msgs;
	}

	/**
	 * Constructor
	 * @param errorMessage
	 */
	public ValidationException(String errorMessage) {
		this(new Msg(errorMessage, MsgLevel.ERROR));
	}

	public List<Msg> getValidationMessages() {
		return msgs;
	}

}
