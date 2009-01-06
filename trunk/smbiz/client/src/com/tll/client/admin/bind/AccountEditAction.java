package com.tll.client.admin.bind;

import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldBindingHelper;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;

/**
 * AccountEditAction
 * @param <M> The model type
 * @param <FP> The {@link FieldPanel} type
 * @author jpk
 */
public class AccountEditAction<M extends IBindable, FP extends FieldPanel<M>> extends AbstractModelEditAction<M, FP> {

	@Override
	protected void populateBinding(FP fp) {
		final M m = fp.getModel();
		final FieldGroup fg = fp.getFieldGroup();
		FieldBindingHelper.addBinding(binding, m, fg, Model.NAME_PROPERTY);
		FieldBindingHelper.addBinding(binding, m, fg, Model.DATE_CREATED_PROPERTY);
		FieldBindingHelper.addBinding(binding, m, fg, Model.DATE_MODIFIED_PROPERTY);
		FieldBindingHelper.addBinding(binding, m, fg, "parent.name");
		FieldBindingHelper.addBinding(binding, m, fg, "status");
		FieldBindingHelper.addBinding(binding, m, fg, "dateCancelled");
		FieldBindingHelper.addBinding(binding, m, fg, "currency.id");
		FieldBindingHelper.addBinding(binding, m, fg, "billingModel");
		FieldBindingHelper.addBinding(binding, m, fg, "billingCycle");
		FieldBindingHelper.addBinding(binding, m, fg, "dateLastCharged");
		FieldBindingHelper.addBinding(binding, m, fg, "nextChargeDate");
		FieldBindingHelper.addBinding(binding, m, fg, "persistPymntInfo");
		FieldBindingHelper.addBinding(binding, m, fg, "paymentInfo");
		FieldBindingHelper.addBinding(binding, m, fg, "addresses");
	}
}