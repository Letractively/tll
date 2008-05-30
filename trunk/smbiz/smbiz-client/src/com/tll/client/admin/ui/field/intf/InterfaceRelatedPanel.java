/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.tll.client.data.AuxDataRequest;
import com.tll.client.ui.field.NamedTimeStampEntityPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * InterfaceRelatedPanel - Abstract field panel for interface type entities.
 * @author jpk
 */
public abstract class InterfaceRelatedPanel extends NamedTimeStampEntityPanel {

	protected boolean bindCodeAndDescFields = true;
	protected TextField code;
	protected TextAreaField description;

	/**
	 * Constructor
	 * @param propName
	 * @param displayName
	 */
	public InterfaceRelatedPanel(String propName, String displayName) {
		super(propName, displayName);
	}

	public void setBindCodeAndDescFields(boolean bindCodeAndDescFields) {
		this.bindCodeAndDescFields = bindCodeAndDescFields;
	}

	@Override
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected void configure() {
		super.configure();

		if(bindCodeAndDescFields) {
			code = ftext("code", "Code", 20);
			description = ftextarea("description", "Description", 3, 18);

			fields.addField(code);
			fields.addField(description);
		}
	}

}
