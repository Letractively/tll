package com.tll.client.admin.bind;

import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
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
		addBinding(fp, Model.NAME_PROPERTY);
		addBinding(fp, Model.DATE_CREATED_PROPERTY);
		addBinding(fp, Model.DATE_MODIFIED_PROPERTY);
		addBinding(fp, "parent.name");
		addBinding(fp, "status");
		addBinding(fp, "dateCancelled");
		addBinding(fp, "currency.id");
		addBinding(fp, "billingModel");
		addBinding(fp, "billingCycle");
		addBinding(fp, "dateLastCharged");
		addBinding(fp, "nextChargeDate");
		addBinding(fp, "persistPymntInfo");
		addBinding(fp, "paymentInfo");
		addBinding(fp, "addresses");
	}
}