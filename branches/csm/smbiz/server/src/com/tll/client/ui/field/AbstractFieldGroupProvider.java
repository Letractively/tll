/**
 * The Logic Lab
 * @author jpk
 * Jan 5, 2009
 */
package com.tll.client.ui.field;

/**
 * AbstractFieldGroupProvider - Common base class for all
 * {@link IFieldGroupProvider} impls.
 * @author jpk
 */
public abstract class AbstractFieldGroupProvider implements IFieldGroupProvider {

	public final FieldGroup getFieldGroup() {
		FieldGroup fg = new FieldGroup();
		populateFieldGroup(fg);
		return fg;
	}

	/**
	 * Populates the given field group.
	 * @param fg The field group
	 */
	protected abstract void populateFieldGroup(FieldGroup fg);

	/**
	 * Helper method that adds commonly employed fields corresponding to common
	 * model properties.
	 * @param fg The field group to which fields are added
	 * @param name Add the common model name field?
	 * @param timestamping Add the commoon model timestamping (date created, date
	 *        modified) fields?
	 */
	protected void addModelCommon(FieldGroup fg, boolean name, boolean timestamping) {
		if(name) {
			fg.addField(FieldFactory.entityNameField());
		}
		if(timestamping) {
			fg.addFields(FieldFactory.entityTimestampFields());
		}
	}
}
