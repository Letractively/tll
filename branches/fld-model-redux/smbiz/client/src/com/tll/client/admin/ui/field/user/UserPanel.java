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
import com.tll.client.field.FieldModelBinding;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.ui.ViewRequestLink;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowFieldPanelComposer;
import com.tll.client.ui.field.TextField;
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
		name = FieldFactory.createNameEntityField();
		timestamps = FieldFactory.createTimestampEntityFields();
		emailAddress = FieldFactory.ftext("emailAddress", "Email Address", 30);
		emailAddress.setReadOnly(true);
		locked = FieldFactory.fbool("locked", "Locked");
		enabled = FieldFactory.fbool("enabled", "Enabled");
		expires = FieldFactory.fdate("expires", "Expires", GlobalFormat.DATE);

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
	public void applyModel(Model model) {
		// set the parent account view link
		Model parentAccount = model.relatedOne("account").getModel();
		RefKey par = parentAccount == null ? null : parentAccount.getRefKey();
		lnkAccount.setText(par.getName());
		lnkAccount.setViewRequest(new EditViewRequest(this, AccountEditView.klas, par));
	}

	@Override
	public void setFieldBindings(Model model, FieldModelBinding bindings) {
		bindings.addBinding(name, model, Model.NAME_PROPERTY);
		bindings.addBinding(timestamps[0], model, Model.DATE_CREATED_PROPERTY);
		bindings.addBinding(timestamps[1], model, Model.DATE_MODIFIED_PROPERTY);
		bindings.addBinding(emailAddress, model, "emailAddress");
		bindings.addBinding(locked, model, "locked");
		bindings.addBinding(enabled, model, "enabled");
		bindings.addBinding(expires, model, "expires");
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
