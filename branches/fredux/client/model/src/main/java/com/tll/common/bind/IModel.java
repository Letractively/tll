/**
 * The Logic Lab
 * @author jpk
 * Mar 5, 2009
 */
package com.tll.common.bind;

import com.tll.IDescriptorProvider;
import com.tll.common.model.IEntityType;
import com.tll.model.schema.IPropertyMetadataProvider;

/**
 * IModel - Definition for a bindable model implementation. These extended
 * methods primarily serve to support binding of indexed properties.
 * @author jpk
 */
public interface IModel extends IBindable, IPropertyMetadataProvider, IDescriptorProvider {
	
	IEntityType getEntityType();

	/**
	 * Has this model not yet been persisted?
	 * @return <code>true</code> if this is a non-persisted model instance.
	 */
	boolean isNew();

	/**
	 * @return <code>true</code> if this model is marked as deleted
	 */
	boolean isMarkedDeleted();

	/**
	 * Mark or un-mark as deleted.
	 * @param delete mark deleted if <code>true<code>
	 */
	void setMarkedDeleted(boolean delete);
}
