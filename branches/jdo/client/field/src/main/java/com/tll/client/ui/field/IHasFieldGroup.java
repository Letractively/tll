/**
 * The Logic Lab
 * @author jpk
 * @since Mar 10, 2009
 */
package com.tll.client.ui.field;


/**
 * IHasFieldGroup
 * @author jpk
 */
public interface IHasFieldGroup extends IFieldGroupProvider {

	/**
	 * Sets the field group.
	 * @param group
	 */
	void setFieldGroup(FieldGroup group);
}
