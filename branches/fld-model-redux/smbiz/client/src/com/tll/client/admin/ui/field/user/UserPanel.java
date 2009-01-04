/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field.user;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.bind.IBindable;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.client.util.GlobalFormat;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel<M extends IBindable> extends FieldPanel<M> {

	private final FlowPanel canvas = new FlowPanel();

	private TextField name;
	private DateField[] timestamps;
	private TextField emailAddress;
	private CheckboxField locked;
	private CheckboxField enabled;
	private DateField expires;

	private ViewRequestLink lnkAccount;

	private AddressPanel<M> addressPanel;

	/**
	 * Constructor
	 */
	public UserPanel() {
		super();
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		name = FieldFactory.entityNameField();
		timestamps = FieldFactory.entityTimestampFields();
		emailAddress = FieldFactory.femail("emailAddress", "Email Address", "Email Address", 30);
		emailAddress.setReadOnly(true);
		locked = FieldFactory.fcheckbox("locked", "Locked", "Locked");
		enabled = FieldFactory.fcheckbox("enabled", "Enabled", "Enabled");
		expires = FieldFactory.fdate("expires", "Expires", "Expires", GlobalFormat.DATE);

		addressPanel = new AddressPanel<M>();

		fields.addField(name);
		fields.addFields(timestamps);
		fields.addField(emailAddress);
		fields.addField(locked);
		fields.addField(enabled);
		fields.addField(expires);
		fields.addField(addressPanel.getFieldGroup());
	}

	/*
	private void setParentAccountViewLink(IFieldGroupModelBinding bindingDef) {
		// set the parent account view link
		Model accountModel = bindingDef.resolveModel(EntityType.ACCOUNT);
		Model parentAccount;
		try {
			parentAccount = accountModel.relatedOne("account").getModel();
			RefKey par = parentAccount == null ? null : parentAccount.getRefKey();
			lnkAccount.setText(par.getName());
			lnkAccount.setViewRequest(new EditViewRequest(this, AccountEditView.klas, par));
		}
		catch(PropertyPathException e) {
			lnkAccount.setText("-");
		}
	}
	*/

	@Override
	protected void draw() {
		final FlowFieldPanelComposer cmpsr = new FlowFieldPanelComposer();
		cmpsr.setCanvas(canvas);

		// first row
		cmpsr.addField(name);
		cmpsr.addField(emailAddress);
		cmpsr.addField(locked);
		cmpsr.stopFlow();
		cmpsr.addField(enabled);
		cmpsr.resetFlow();
		cmpsr.addField(expires);

		// parent account ref link
		lnkAccount = new ViewRequestLink();
		cmpsr.addWidget("Account", lnkAccount);

		cmpsr.addField(timestamps[0]);
		cmpsr.stopFlow();
		cmpsr.addField(timestamps[1]);
		cmpsr.resetFlow();

		// third row
		cmpsr.newRow();
		cmpsr.addWidget(addressPanel);
	}
}
