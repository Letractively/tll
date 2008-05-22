/**
 * The Logic Lab
 * @author jpk
 * Feb 24, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.Grid;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.field.IField;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.FieldLabel;
import com.tll.client.ui.field.FieldPanel;
import com.tll.model.EntityType;

/**
 * AbstractInterfacePanel
 * @author jpk
 */
public abstract class AbstractInterfacePanel extends InterfaceRelatedPanel {

	protected CheckboxField isAvailableAsp;
	protected CheckboxField isAvailableIsp;
	protected CheckboxField isAvailableMerchant;
	protected CheckboxField isAvailableCustomer;

	protected CheckboxField isRequiredAsp;
	protected CheckboxField isRequiredIsp;
	protected CheckboxField isRequiredMerchant;
	protected CheckboxField isRequiredCustomer;

	/**
	 * Constructor
	 * @param propName
	 */
	public AbstractInterfacePanel(String propName) {
		super(propName, "Interface");
	}

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		super.neededAuxData(auxDataRequest);
		// let's get all interface prototype models upfront
		auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_SWITCH);
		auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_SINGLE);
		auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_MULTI);
		auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_OPTION);
		auxDataRequest.requestEntityPrototype(EntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
	}

	@Override
	protected void configure() {
		super.configure();

		isAvailableAsp = fbool("isAvailableAsp", null);
		isAvailableIsp = fbool("isAvailableIsp", null);
		isAvailableMerchant = fbool("isAvailableMerchant", null);
		isAvailableCustomer = fbool("isAvailableCustomer", null);

		isRequiredAsp = fbool("isRequiredAsp", null);
		isRequiredIsp = fbool("isRequiredIsp", null);
		isRequiredMerchant = fbool("isRequiredMerchant", null);
		isRequiredCustomer = fbool("isRequiredCustomer", null);

		fields.addField(isAvailableAsp);
		fields.addField(isAvailableIsp);
		fields.addField(isAvailableMerchant);
		fields.addField(isAvailableCustomer);

		fields.addField(isRequiredAsp);
		fields.addField(isRequiredIsp);
		fields.addField(isRequiredMerchant);
		fields.addField(isRequiredCustomer);

		FieldPanel frow, fcol;

		// first row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);
		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		frow.add(fcol);
		fcol.add(name);
		fcol.add(code);
		frow.add(description);

		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		frow.add(fcol);
		Grid g = new Grid(3, 5);
		fcol.add(g);
		g.setWidget(0, 1, new FieldLabel("Asp"));
		g.setWidget(0, 2, new FieldLabel("Isp"));
		g.setWidget(0, 3, new FieldLabel("Mrc"));
		g.setWidget(0, 4, new FieldLabel("Cst"));
		g.setWidget(1, 0, new FieldLabel("Available?"));
		g.setWidget(1, 1, isAvailableAsp);
		g.setWidget(1, 2, isAvailableIsp);
		g.setWidget(1, 3, isAvailableMerchant);
		g.setWidget(1, 4, isAvailableCustomer);
		g.setWidget(2, 0, new FieldLabel("Required?"));
		g.setWidget(2, 1, isRequiredAsp);
		g.setWidget(2, 2, isRequiredIsp);
		g.setWidget(2, 3, isRequiredMerchant);
		g.setWidget(2, 4, isRequiredCustomer);

		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		frow.add(fcol);
		fcol.add(dateCreated);
		fcol.add(dateModified);
	}

}
