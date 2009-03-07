/**
 * The Logic Lab
 * @author jpk
 * Mar 6, 2009
 */
package com.tll.client.ui;

import java.util.Collection;

import com.tll.client.convert.IConverter;

/**
 * AbstractBindableCollectionWidget
 * @param <V> the value type
 * @param <R> the render value type
 * @author jpk
 */
public abstract class AbstractBindableCollectionWidget<V, R> extends AbstractBindableWidget<Collection<V>> implements
		IBindableCollectionWidget<V, R> {

	private IConverter<R, V> renderer;

	@Override
	public IConverter<R, V> getRenderer() {
		return renderer;
	}

	@Override
	public void setRenderer(IConverter<R, V> renderer) {
		this.renderer = renderer;
	}
}
