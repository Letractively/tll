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
 * ScalarError - An error with one or more error messages.
 * @author jpk
 */
public class ScalarError implements IError {
	
	private final List<Msg> errorMsgs;

	@Override
	public Type getType() {
		return Type.SCALAR;
	}

	/**
	 * Constructor
	 * @param errorMsg
	 */
	public ScalarError(String errorMsg) {
		errorMsgs = new ArrayList<Msg>();
		errorMsgs.add(new Msg(errorMsg, MsgLevel.ERROR));
	}

	/**
	 * Constructor
	 * @param errorMsgs
	 */
	public ScalarError(List<Msg> errorMsgs) {
		this.errorMsgs = errorMsgs;
	}

	/**
	 * @return the error messages list.
	 */
	public List<Msg> getMessages() {
		return errorMsgs;
	}
}
