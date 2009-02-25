package com.tll;

import com.tll.util.StringUtil;

/**
 * Common system wide exception class for unexpected application errors. "System
 * errors" do not have to be declared and should generally fall through going
 * unchecked.
 * @author jpk
 */
public class SystemError extends RuntimeException {

	private static final long serialVersionUID = -7923540576836805926L;

	private static final String[] NO_VARS = new String[] {};

	public SystemError(String message) {
		this(message, NO_VARS);
	}

	public SystemError(String message, String var) {
		this(message, var, null);
	}

	public SystemError(String message, String[] vars) {
		this(message, vars, null);
	}

	public SystemError(String message, Throwable t) {
		this(message, NO_VARS, t);
	}

	public SystemError(String message, String variable, Throwable t) {
		this(message, new String[] { variable }, t);
	}

	public SystemError(String message, String[] variables, Throwable t) {
		super(StringUtil.replaceVariables(message, variables), t);
	}

}
