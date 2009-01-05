/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field.user;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.admin.ui.field.AddressFieldsProvider;
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.client.util.GlobalFormat;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel<M extends IBindable> extends FieldPanel<M> {

	static class UserFieldsProvider implements IFieldGroupProvider {

		public FieldGroup getFieldGroup() {
			FieldGroup fg = new FieldGroup();

			fg.addField(FieldFactory.entityNameField());
			fg.addFields(FieldFactory.entityTimestampFields());
			fg.addField(FieldFactory.femail("emailAddress", "Email Address", "Email Address", 30));
			fg.getField("emailAddress").setReadOnly(true);

			fg.addField(FieldFactory.fcheckbox("locked", "Locked", "Locked"));
			fg.addField(FieldFactory.fcheckbox("enabled", "Enabled", "Enabled"));
			fg.addField(FieldFactory.fdate("expires", "Expires", "Expires", GlobalFormat.DATE));

			// address
			AddressFieldsProvider afp = new AddressFieldsProvider();
			fg.addField("address", afp.getFieldGroup());

			return fg;
		}

	}

	class UserFieldsRenderer implements IFieldRenderer {

		public void render(Panel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(canvas);

			// first row
			cmpsr.addField(fg.getField("name"));
			cmpsr.addField(fg.getField("emailAddress"));
			cmpsr.addField(fg.getField("locked"));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField("enabled"));
			cmpsr.resetFlow();
			cmpsr.addField(fg.getField("expires"));

			// parent account ref link
			lnkAccount = new ViewRequestLink();
			cmpsr.addWidget("Account", lnkAccount);

			cmpsr.addField(fg.getField(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField(Model.DATE_MODIFIED_PROPERTY));
			cmpsr.resetFlow();

			// third row
			cmpsr.newRow();
			FlowPanel fp = new FlowPanel();
			AddressFieldsRenderer afr = new AddressFieldsRenderer();
			afr.render(fp, (FieldGroup) fg.getField("address"));
			cmpsr.addWidget(fp);
		}

	}

	private final FlowPanel canvas = new FlowPanel();

	private ViewRequestLink lnkAccount;

	/**
	 * Constructor
	 */
	public UserPanel() {
		super();
		initWidget(canvas);
		setRenderer(new UserFieldsRenderer());
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		return (new UserFieldsProvider()).getFieldGroup();
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

}
