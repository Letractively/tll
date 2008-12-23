/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.ui;

/**
 * IRenderer - Simple contract for rendering.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em>
 * @author jpk
 * @param <O> The output type
 * @param <I> The input type
 */
public interface IRenderer<O, I> {

	/**
	 * Render.
	 * @param o The object to render
	 * @return The rendered object
	 */
	O render(I o);
}
