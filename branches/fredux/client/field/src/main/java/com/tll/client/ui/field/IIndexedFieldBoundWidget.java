/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui.field;

import java.util.Collection;

import com.tll.client.ui.IBindableWidget;
import com.tll.common.bind.IModel;


/**
 * IIndexedFieldBoundWidget
 * @author jpk
 */
public interface IIndexedFieldBoundWidget extends IBindableWidget<Collection<IModel>>, IFieldBoundWidget {

	/**
	 * @return The property path name identifying the related many model
	 *         collection relative to the root model. The root model is that model
	 *         held in the parent field bound widget that has this widget as a
	 *         child.
	 */
	String getIndexedPropertyName();
}
