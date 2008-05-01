/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field;

import java.util.Iterator;
import java.util.List;

import com.tll.client.admin.mvc.view.account.AccountEditView;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.data.AuxDataRequest;
import com.tll.client.event.type.EditViewRequest;
import com.tll.client.field.FieldGroup;
import com.tll.client.field.IField;
import com.tll.client.model.IndexedProperty;
import com.tll.client.model.Model;
import com.tll.client.model.RefKey;
import com.tll.client.ui.ViewRequestLink;
import com.tll.client.ui.field.CheckboxField;
import com.tll.client.ui.field.DateField;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FieldWidget;
import com.tll.client.ui.field.NamedTimeStampEntityPanel;
import com.tll.client.ui.field.TextField;
import com.tll.client.util.GlobalFormat;
import com.tll.model.EntityType;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel extends NamedTimeStampEntityPanel {

	protected TextField emailAddress;
	protected CheckboxField locked;
	protected CheckboxField enabled;
	protected DateField expires;

	protected ViewRequestLink lnkAccount;

	protected CheckboxField[] authorities;

	protected FieldPanel authorityPanel;

	protected AddressPanel addressPanel;

	/**
	 * Constructor
	 * @param propName
	 */
	public UserPanel(String propName) {
		super(propName, "User");
	}

	@Override
	protected void neededAuxData(AuxDataRequest auxDataRequest) {
		auxDataRequest.requestEntityList(EntityType.AUTHORITY);
	}

	@Override
	protected void configure() {
		super.configure();

		emailAddress = ftext("emailAddress", "Email Address", IField.LBL_ABOVE, 30);
		emailAddress.setReadOnly(true);
		locked = fbool("locked", "Locked");
		enabled = fbool("enabled", "Enabled");
		expires = fdate("expires", "Expires", IField.LBL_ABOVE, GlobalFormat.DATE);

		authorityPanel = new FieldPanel(IField.CSS_FIELD_ROW);
		List<Model> authList = AuxDataCache.instance().getEntityList(EntityType.AUTHORITY);
		authorities = new CheckboxField[authList.size()];
		int i = 0;
		for(Model auth : authList) {
			String role = auth.asString("authority");
			CheckboxField cbAuth = fcheckbox("authority", role, role, "");
			FieldGroup fg = new FieldGroup("authoritys[" + (i++) + "]", role, cbAuth);
			fields.addField(fg);
			fg.addField(cbAuth);
			authorityPanel.add(cbAuth);
		}

		addressPanel = new AddressPanel("address");
		addressPanel.configure();

		fields.addField(emailAddress);
		fields.addField(locked);
		fields.addField(enabled);
		fields.addField(expires);
		fields.addField(addressPanel.getFields());

		FieldPanel frow, fcol;

		// first row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);
		frow.add(name);
		frow.add(emailAddress);
		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		fcol.add(locked);
		fcol.add(enabled);
		frow.add(fcol);
		frow.add(expires);

		// parent account ref link
		lnkAccount = new ViewRequestLink();
		frow.add(new FieldWidget("Account", IField.LBL_ABOVE, lnkAccount));

		fcol = new FieldPanel(IField.CSS_FIELD_COL);
		fcol.add(dateCreated);
		fcol.add(dateModified);
		frow.add(fcol);

		// second row (authorities)
		add(authorityPanel);

		// third row
		frow = new FieldPanel(IField.CSS_FIELD_ROW);
		add(frow);
		frow.add(addressPanel);
	}

	@Override
	protected void onBeforeBind(Model model) {
		super.onBeforeBind(model);

		// set the parent account view link
		lnkAccount.setText(model.asString("account.name"));
		Model account = model.relatedOne("account").getModel();
		RefKey accountRef = account.getRefKey();
		EditViewRequest vr = new EditViewRequest(this, AccountEditView.klas, accountRef);
		lnkAccount.setViewRequest(vr);

		// check the authorities bound to the user
		Iterator<IndexedProperty> itr = model.relatedMany("authoritys").iterator();
		if(itr != null) {
			while(itr.hasNext()) {
				IndexedProperty auth = itr.next();
				CheckboxField cbAuth = (CheckboxField) fields.getField(auth.getPropertyName() + ".authority");
				cbAuth.setChecked(true);
			}
		}
	}
}
