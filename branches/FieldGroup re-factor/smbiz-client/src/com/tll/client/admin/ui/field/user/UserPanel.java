/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field.user;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.tll.client.admin.mvc.view.account.AccountEditView;
import com.tll.client.admin.ui.field.AddressPanel;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.field.FieldGroup;
import com.tll.client.model.IndexedProperty;
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
import com.tll.model.EntityType;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel extends FieldGroupPanel {

	protected TextField emailAddress;
	protected CheckboxField locked;
	protected CheckboxField enabled;
	protected DateField expires;

	protected ViewRequestLink lnkAccount;

	protected CheckboxField[] authorities;

	// protected FieldPanel authorityPanel;

	protected AddressPanel addressPanel;

	/**
	 * Constructor
	 */
	public UserPanel() {
		super("User");
	}

	@Override
	public void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestEntityList(EntityType.AUTHORITY);
	}

	@Override
	protected Widget doInit() {
		final TextField fname = createNameEntityField();
		final DateField[] ftimestamps = createTimestampEntityFields();
		emailAddress = ftext("emailAddress", "Email Address", 30);
		emailAddress.setReadOnly(true);
		locked = fbool("locked", "Locked");
		enabled = fbool("enabled", "Enabled");
		expires = fdate("expires", "Expires", GlobalFormat.DATE);

		addressPanel = new AddressPanel();
		addressPanel.init();

		fields.addField(emailAddress);
		fields.addField(locked);
		fields.addField(enabled);
		fields.addField(expires);
		fields.addField(addressPanel.getFields());

		FlowFieldPanelComposer canvas = new FlowFieldPanelComposer();

		// first row
		canvas.addField(fname);
		canvas.addField(emailAddress);
		canvas.addField(locked);
		canvas.stopFlow();
		canvas.addField(enabled);
		canvas.resetFlow();
		canvas.addField(expires);

		// parent account ref link
		lnkAccount = new ViewRequestLink();
		canvas.addWidget("Account", lnkAccount);

		canvas.addField(ftimestamps[0]);
		canvas.stopFlow();
		canvas.addField(ftimestamps[1]);
		canvas.resetFlow();

		// second row (authorities)
		canvas.newRow();
		List<Model> authList = AuxDataCache.instance().getEntityList(EntityType.AUTHORITY);
		authorities = new CheckboxField[authList.size()];
		// int i = 0;
		for(Model auth : authList) {
			String role = auth.asString("authority");
			CheckboxField cbAuth = fcheckbox("authority", role, role, "");
			FieldGroup fg = new FieldGroup(/*PropertyPath.indexed("authoritys", ++i),*/role, cbAuth);
			fields.addField(fg);
			fg.addField(cbAuth);
			canvas.addField(cbAuth);
		}

		// third row
		canvas.newRow();
		canvas.addWidget(addressPanel);

		return canvas.getWidget();
	}

	@Override
	protected void onBeforeBind(Model model) {
		super.onBeforeBind(model);

		final PropertyPath path = new PropertyPath();

		// set the parent account view link
		lnkAccount.setText(model.asString("account.name"));
		path.parse("account");
		Model account = model.relatedOne(path).getModel();
		RefKey accountRef = account.getRefKey();
		EditViewRequest vr = new EditViewRequest(this, AccountEditView.klas, accountRef);
		lnkAccount.setViewRequest(vr);

		// check the authorities bound to the user
		path.parse("authoritys");
		Iterator<IndexedProperty> itr = model.relatedMany(path).iterator();
		if(itr != null) {
			while(itr.hasNext()) {
				IndexedProperty auth = itr.next();
				CheckboxField cbAuth = (CheckboxField) fields.getField(auth.getPropertyName() + ".authority");
				cbAuth.setChecked(true);
			}
		}
	}
}
