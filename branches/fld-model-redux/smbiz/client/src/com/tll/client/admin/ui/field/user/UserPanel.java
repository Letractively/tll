/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field.user;

import com.google.gwt.user.client.ui.Panel;
import com.tll.client.admin.mvc.view.account.AccountEditView;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.TextField;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.client.util.GlobalFormat;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel extends FieldPanel {

	private TextField name;
	private DateField[] timestamps;
	private TextField emailAddress;
	private CheckboxField locked;
	private CheckboxField enabled;
	private DateField expires;

	private ViewRequestLink lnkAccount;

	private AddressPanel addressPanel;

	/**
	 * Constructor
	 */
	public UserPanel() {
		super("User");
	}

	@Override
	public void populateFieldGroup(FieldGroup fields) {
		name = entityNameField();
		timestamps = entityTimestampFields();
		emailAddress = ftext("emailAddress", "Email Address", 30);
		emailAddress.setReadOnly(true);
		locked = fbool("locked", "Locked");
		enabled = fbool("enabled", "Enabled");
		expires = fdate("expires", "Expires", GlobalFormat.DATE);

		addressPanel = new AddressPanel();

		fields.addField(name);
		fields.addFields(timestamps);
		fields.addField(emailAddress);
		fields.addField(locked);
		fields.addField(enabled);
		fields.addField(expires);
		fields.addField(addressPanel.getFieldGroup());
	}

	@Override
	public void applyModel() {
		Model model = bindingDef.getModel(modelPropPath);
		// set the parent account view link
		Model parentAccount = model.relatedOne("account").getModel();
		RefKey par = parentAccount == null ? null : parentAccount.getRefKey();
		lnkAccount.setText(par.getName());
		lnkAccount.setViewRequest(new EditViewRequest(this, AccountEditView.klas, par));
	}

	@Override
	protected void drawInternal(Panel canvas) {
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
