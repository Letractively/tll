/**
 * The Logic Lab
 * @author jpk
 * Mar 4, 2009
 */
package com.tll.client.validate;

import java.util.ArrayList;
import java.util.List;

import com.tll.client.ui.IWidgetRef;
import com.tll.common.msg.Msg;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * Error - A single error with one or more error messages able to target a
 * widget. Targeting a widget is not required but if it does, it is considered
 * "local" (non-global).
 * @author jpk
 */
public class Error {

	private final ErrorClassifier classifier;
	private IWidgetRef target;
	private final ArrayList<Msg> errorMsgs = new ArrayList<Msg>();

	/**
	 * Constructor
	 * @param classifier
	 * @param target
	 * @param errorMsg
	 */
	public Error(final ErrorClassifier classifier, final IWidgetRef target, final String errorMsg) {
		this.classifier = classifier;
		errorMsgs.add(new Msg(errorMsg, MsgLevel.ERROR));
		this.target = target;
	}

	/**
	 * Constructor
	 * @param classifier
	 * @param target
	 * @param errorMsgs
	 */
	public Error(final ErrorClassifier classifier, final IWidgetRef target, final List<Msg> errorMsgs) {
		this.classifier = classifier;
		this.errorMsgs.addAll(errorMsgs);
		this.target = target;
	}

	public IWidgetRef getTarget() {
		return target;
	}

	public void setTarget(final IWidgetRef target) {
		this.target = target;
	}

	public ErrorClassifier getClassifier() {
		return classifier;
	}

	/**
	 * @return the error messages list.
	 */
	public List<Msg> getMessages() {
		return errorMsgs;
	}
}
