/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui.field;

import java.util.Collection;

import com.tll.client.ui.IBindableWidget;
import com.tll.common.model.Model;

/**
 * IIndexedFieldBoundWidget
 * @author jpk
 */
public interface IIndexedFieldBoundWidget extends IBindableWidget<Collection<Model>>, IFieldBoundWidget {

	/**
	 * @return The property path name identifying the related many model
	 *         collection relative to the root model. The root model is that model
	 *         held in the field bound widget of which this widget is a child.
	 */
	String getIndexedPropertyName();

	/**
	 * Removes all indexed bindable widget elements. For each element, its
	 * bindings are shall be cleared and its field group is remvoved from this
	 * widget's field group. Also, the corres. ui elements are expected to be
	 * removed as well.
	 */
	void clearIndexed();
}
