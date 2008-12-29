package com.tll.client.ui;

import java.util.Comparator;

import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.tll.client.bind.IBindable;
import com.tll.client.bind.IBindingAction;
import com.tll.client.renderer.IRenderer;

/**
 * IBoundWidget - Common base class for a UI widgets that are bindable.
 * <p>
 * <em><b>IMPT NOTE: </b>This code was originally derived from the <a href="http://gwittir.googlecode.com/">gwittir</a> project.</em> @author
 * jpk
 * @param <B> The "bound" type. That is the value type on the other side of the
 *        binding that binds this bindable.
 * @param <V> The value type or the type this instance internally holds as the
 *        value.
 * @param <M> The model type
 */
public interface IBoundWidget<B, V, M> extends IBindable, SourcesChangeEvents {

	/**
	 * @return The action
	 */
	IBindingAction<IBoundWidget<B, V, M>> getAction();

	/**
	 * Sets the action.
	 * @param action The action to set
	 */
	void setAction(IBindingAction<IBoundWidget<B, V, M>> action);

	/**
	 * @return The employed {@link Comparator} for this bound widget.
	 */
	Comparator<B> getComparator();

	/**
	 * Used in determining whether a given bound value is the same as that
	 * currently held in this widget.
	 * @param comparator
	 */
	void setComparator(Comparator<B> comparator);

	/**
	 * A placeholder for access to the object at the other end of the binding
	 * which for bound widgets usually means a data structure of sorts which we
	 * choose to call model.
	 * @return The model containing the bit of data this widget displays.
	 */
	M getModel();

	/**
	 * Sets the model.
	 * @param model The model to set
	 */
	void setModel(M model);

	/**
	 * Responsible for providing a render ready value for display in the UI.
	 * @return The renderer.
	 */
	IRenderer<V, B> getRenderer();

	/**
	 * Sets the renderer.
	 * @param renderer The renderer to set
	 */
	void setRenderer(IRenderer<V, B> renderer);

	/**
	 * Gets the value in the "native" widget type.
	 * @return The [widget] value
	 */
	V getValue();

	/**
	 * Sets the value.
	 * @param value The "bound" value to set
	 */
	void setValue(B value);
}
