package com.tll.client.mock;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.bind.AbstractBindingAction;
import com.tll.client.ui.field.FieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;

/**
 * SimpleBindingAction
 * @author jpk
 */
public class SimpleBindingAction extends AbstractBindingAction<Model, FieldPanel<FlowPanel, Model>> {

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
	}
}