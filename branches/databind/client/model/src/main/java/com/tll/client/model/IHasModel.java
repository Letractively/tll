/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.model;

import com.tll.common.model.Model;


/**
 * IHasModel
 * @author jpk
 */
public interface IHasModel {

	/**
	 * @return the model.
	 */
	Model getModel();

	/**
	 * Set the model.
	 * @param model
	 */
	void setModel(Model model);
}
