/**
 * The Logic Lab
 * @author jpk
 * Dec 14, 2008
 */
package com.tll.client.field;

import com.tll.client.validate.IValidator;

/**
 * IFieldBinding - Serves to commonize needed methods for a field binding and a group of field bindings. 
 * @author jpk
 */
public interface IFieldBinding extends IValidator {

	void bind();

	void unbind();
}
