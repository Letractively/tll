/**
 * The Logic Lab
 * @author jpk
 * @since Mar 13, 2010
 */
package com.tll.client.ui.field;

import com.tll.client.ui.IBindableWidget;

/**
 * A field widget that supports data binding by implmenting
 * {@link IBindableWidget}.
 * @param <V> field value type
 * @author jpk
 */
public interface IBindableFieldWidget<V> extends IFieldWidget<V>, IBindableWidget<V> {

}
