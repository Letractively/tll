/**
 * The Logic Lab
 * @author jpk
 * Mar 6, 2009
 */
package com.tll.client.ui;

import java.util.Collection;

import com.tll.client.convert.IConverter;

/**
 * IBindableCollectionWidget
 * @author jpk
 * @param <V> the value type
 * @param <R> the render type
 */
public interface IBindableCollectionWidget<V, R> extends IBindableWidget<Collection<V>> {

	/**
	 * @return the converter that converts &lt;V&gt; types to &lt;R&gt; types.
	 */
	IConverter<R, V> getRenderer();

  /**
	 * Set the collection element converter.
	 * @param renderer
	 */
	void setRenderer(IConverter<R, V> renderer);
}
