/**
 * The Logic Lab
 * @author jpk
 * Jan 4, 2009
 */
package com.tll.client.admin.ui.field;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;

/**
 * BankFieldsRenderer
 * @author jpk
 */
public class BankFieldsRenderer implements IFieldRenderer {

	public void render(Panel panel, FieldGroup fg, String parentPropPath) {
		final GridFieldComposer cmpsr = new GridFieldComposer();
		cmpsr.setCanvas(panel);

		cmpsr.addField(fg.getField(parentPropPath, "bankName"));
		cmpsr.addField(fg.getField(parentPropPath, "bankAccountNo"));
		cmpsr.addField(fg.getField(parentPropPath, "bankRoutingNo"));
	}

}
