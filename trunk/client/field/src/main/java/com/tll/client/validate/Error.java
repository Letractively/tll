/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.List;

import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * Error - A single error with one or more error messages.
 * @author jpk
 */
public class Error implements IError {

	private final ErrorClassifier sourcing;
	private final List<Msg> errorMsgs;

	@Override
	public Type getType() {
		return Type.SINGLE;
	}

	/**
	 * Constructor
	 * @param classifier
	 * @param errorMsg
	 */
	public Error(ErrorClassifier classifier, String errorMsg) {
		this.sourcing = classifier;
		errorMsgs = new ArrayList<Msg>();
		errorMsgs.add(new Msg(errorMsg, MsgLevel.ERROR));
	}

	/**
	 * Constructor
	 * @param sourcing
	 * @param errorMsgs
	 */
	public Error(ErrorClassifier sourcing, List<Msg> errorMsgs) {
		this.sourcing = sourcing;
		this.errorMsgs = errorMsgs;
	}

	@Override
	public ErrorClassifier getClassifier() {
		return sourcing;
	}

	/**
	 * @return the error messages list.
	 */
	public List<Msg> getMessages() {
		return errorMsgs;
	}
}
