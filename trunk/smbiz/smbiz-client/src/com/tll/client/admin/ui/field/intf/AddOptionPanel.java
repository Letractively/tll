/**
 * The Logic Lab
 * @author jpk
 * May 22, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.TextAreaField;
import com.tll.client.ui.field.TextField;

/**
 * AddOptionPanel
 * @author jpk
 */
public class AddOptionPanel extends FieldGroupPanel {

	private TextField name;
	private TextField code;
	private TextAreaField description;

	/**
	 * Constructor
	 */
	private AddOptionPanel() {
		super(null, "Add Option");
	}

	@Override
	protected void configure() {
		name = ftext("name", "Name", 30);
		code = ftext("code", "Code", 20);
		description = ftextarea("description", "Description", 3, 18);

		fields.addField(name);
		fields.addField(code);
		fields.addField(description);

		/*
		FieldPanel fcol = new FieldPanel(FieldPanel.CSS_FIELD_COL);
		fcol.add(name);
		fcol.add(code);
		fcol.add(description);
		add(fcol);
		*/
	}

}
