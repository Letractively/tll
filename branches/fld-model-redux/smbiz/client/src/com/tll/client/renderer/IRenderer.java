/**
 * The Logic Lab
 * @author jpk
 * Dec 22, 2008
 */
package com.tll.client.renderer;

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
	 * @throws IllegalArgumentException When the given object to render can't be
	 *         rendered.
	 */
	O render(I o) throws IllegalArgumentException;
}
