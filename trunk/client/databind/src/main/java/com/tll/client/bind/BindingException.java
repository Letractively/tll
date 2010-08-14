package com.tll.client.bind;

/**
 * BindingException
 * @author jpk
 */
@SuppressWarnings("serial")
public class BindingException extends RuntimeException {

	public BindingException(String message, Throwable cause) {
		super(message, cause);

	}

	public BindingException(String message) {
		super(message);

	}

	public BindingException(Throwable cause) {
		super(cause);

	}

}
