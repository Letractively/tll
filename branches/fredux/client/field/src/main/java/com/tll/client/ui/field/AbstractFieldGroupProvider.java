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
public abstract class AbstractFieldGroupProvider extends FieldFactory implements IFieldGroupProvider {

	public final FieldGroup getFieldGroup() {
		final FieldGroup fg = new FieldGroup(getFieldGroupName());
		populateFieldGroup(fg);
		return fg;
	}

	/**
	 * @return The name to ascribe to the provided field group.
	 */
	protected abstract String getFieldGroupName();

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
	protected final void addModelCommon(FieldGroup fg, boolean name, boolean timestamping) {
		if(name) {
			fg.addField(entityNameField());
		}
		if(timestamping) {
			fg.addFields(entityTimestampFields());
		}
	}
}
