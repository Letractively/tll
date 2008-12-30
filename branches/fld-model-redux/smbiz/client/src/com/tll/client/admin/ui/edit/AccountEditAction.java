/**
 * The Logic Lab
 * @author jpk
 * Dec 29, 2008
 */
package com.tll.client.admin.ui.edit;

import java.util.List;

import com.tll.client.admin.ui.field.account.AccountPanel;
import com.tll.client.bind.AbstractModelEditAction;
import com.tll.client.bind.Binding;
import com.tll.client.model.Model;

/**
 * AccountEditAction
 * @author jpk
 */
public class AccountEditAction extends AbstractModelEditAction<AccountPanel<Model>> {

	@Override
	public void execute() {
	}

	public void setBindable(AccountPanel<Model> ap) {
		final Model m = ap.getModel();
		final List<Binding> children = binding.getChildren();

		children.add(new Binding(m, ap, Model.NAME_PROPERTY));
		children.add(new Binding(m, ap, Model.DATE_CREATED_PROPERTY));
		children.add(new Binding(m, ap, Model.DATE_MODIFIED_PROPERTY));
		children.add(new Binding(m, ap, "parent.name"));
		children.add(new Binding(m, ap, "status"));
		children.add(new Binding(m, ap, "dateCancelled"));
		children.add(new Binding(m, ap, "currency.id"));
		children.add(new Binding(m, ap, "billingModel"));
		children.add(new Binding(m, ap, "billingCycle"));
		children.add(new Binding(m, ap, "dateLastCharged"));
		children.add(new Binding(m, ap, "nextChargeDate"));
		children.add(new Binding(m, ap, "persistPymntInfo"));
		children.add(new Binding(m, ap, "paymentInfo"));
		children.add(new Binding(m, ap, "addresses"));

	}
}
