/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field;

import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.IField;
import com.tll.client.ui.field.NamedTimeStampEntityPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * InterfaceRelatedPanel - Abstract field panel for interface type entities.
 * @author jpk
 */
public abstract class InterfaceRelatedPanel extends NamedTimeStampEntityPanel {

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

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		// none
	}

	@Override
	protected void configure() {
		super.configure();

		code = ftext("code", "Code", IField.LBL_ABOVE, 20);
		description = ftextarea("description", "Description", IField.LBL_ABOVE, 3, 18);

		fields.addField(code);
		fields.addField(description);
	}

}
