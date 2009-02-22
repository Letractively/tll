package com.tll.client.mock;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;

/**
 * MockBinding
 * @author jpk
 */
public class MockBinding extends AbstractModelEditAction<Model, FieldPanel<FlowPanel, Model>> {

	@Override
	protected void populateBinding(FieldPanel<FlowPanel, Model> fp) throws PropertyPathException {

		addFieldBinding(fp, Model.NAME_PROPERTY);
		addFieldBinding(fp, Model.DATE_CREATED_PROPERTY);
		addFieldBinding(fp, Model.DATE_MODIFIED_PROPERTY);
		addFieldBinding(fp, "parent.name");
		addFieldBinding(fp, "status");
		addFieldBinding(fp, "dateCancelled");
		//addFieldBinding(fp, "currency.id");
		addFieldBinding(fp, "billingModel");
		addFieldBinding(fp, "billingCycle");
		addFieldBinding(fp, "dateLastCharged");
		addFieldBinding(fp, "nextChargeDate");
		addFieldBinding(fp, "persistPymntInfo");

		addNestedFieldBindings(fp, "paymentInfo");

		// addIndexedFieldBinding(fp.getModel(), "addresses", addressesPanel);
	}

	public void execute() {
	}

}