/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field.user;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.mvc.view.account.AccountEditView;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.model.Model;
import com.tll.client.model.PropertyPath;
import com.tll.client.model.RefKey;
import com.tll.client.ui.FlowFieldPanelComposer;
import com.tll.client.ui.ViewRequestLink;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldGroupPanel;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.GlobalFormat;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel extends FieldGroupPanel {

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
	protected Widget draw() {
		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(name);
		canvas.addField(emailAddress);
		canvas.addField(locked);
		canvas.stopFlow();
		canvas.addField(enabled);
		canvas.resetFlow();
		canvas.addField(expires);

		// parent account ref link
		lnkAccount = new ViewRequestLink();
		canvas.addWidget("Account", lnkAccount);

		canvas.addField(timestamps[0]);
		canvas.stopFlow();
		canvas.addField(timestamps[1]);
		canvas.resetFlow();

		// third row
		canvas.newRow();
		canvas.addWidget(addressPanel);

		return canvas.getCanvasWidget();
	}

	@Override
	public void populateFieldGroup() {
		name = createNameEntityField();
		timestamps = createTimestampEntityFields();
		emailAddress = ftext("emailAddress", "Email Address", 30);
		emailAddress.setReadOnly(true);
		locked = fbool("locked", "Locked");
		enabled = fbool("enabled", "Enabled");
		expires = fdate("expires", "Expires", GlobalFormat.DATE);

		addressPanel = new AddressPanel();

		addField(emailAddress);
		addField(locked);
		addField(enabled);
		addField(expires);
		addField(addressPanel.getFields());
	}

	@Override
	protected void applyModel(Model model) {
		// set the parent account view link
		Model parentAccount = model.relatedOne(new PropertyPath("account")).getModel();
		RefKey par = parentAccount == null ? null : parentAccount.getRefKey();
		lnkAccount.setText(par.getName());
		lnkAccount.setViewRequest(new EditViewRequest(this, AccountEditView.klas, par));
	}
}