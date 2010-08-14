/**
 * The Logic Lab
 * @author jpk
 * @since Mar 14, 2010
 */
package com.tll.client.ui.edit;

import com.tll.common.model.Model;


/**
 * Holds model edit content.
 * @author jpk
 */
public interface IModelEditContent {

	/**
	 * @return model data containing only the changed properties.
	 */
	Model getChangedModel();
	
	/**
	 * @return The model data subject to editing.
	 */
	Model getModel();
}
