package com.tll.client.renderer;

/**
 * ToStringRenderer - Renders an arbitrary {@link Object} to a {@link String}.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 */
public class ToStringRenderer<I> implements IRenderer<String, I> {

	public static final ToStringRenderer<Object> INSTANCE = new ToStringRenderer<Object>();

	/**
	 * Constructor
	 */
	private ToStringRenderer() {
	}

	/**
	 * Translates an arbitrary {@link Object} instance to a non-<code>null</code>
	 * {@link String} instance.
	 * @return A never <code>null</code> {@link String}.
	 */
	public String render(Object o) {
		return o == null ? "" : o.toString();
	}
}
