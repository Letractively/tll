/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.ui.field.user;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.field.AddressFieldsRenderer;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.view.ViewRequestLink;
import com.tll.common.model.Model;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel extends FieldPanel<FlowPanel> {

	class UserFieldsRenderer implements IFieldRenderer<FlowPanel> {

		@SuppressWarnings("synthetic-access")
		public void render(FlowPanel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(canvas);

			// first row
			cmpsr.addField(fg.getFieldWidgetByName("userName"));
			cmpsr.addField(fg.getFieldWidgetByName("userEmailAddress"));
			cmpsr.addField(fg.getFieldWidgetByName("userLocked"));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldWidgetByName("userEnabled"));
			cmpsr.resetFlow();
			cmpsr.addField(fg.getFieldWidgetByName("userExpires"));

			// parent account ref link
			lnkAccount = new ViewRequestLink();
			cmpsr.addWidget("Account", lnkAccount);

			cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_MODIFIED_PROPERTY));
			cmpsr.resetFlow();

			// third row
			cmpsr.newRow();
			final FlowPanel fp = new FlowPanel();
			final AddressFieldsRenderer afr = new AddressFieldsRenderer();
			afr.render(fp, null);
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
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		return (new UserFieldsProvider()).getFieldGroup();
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new UserFieldsRenderer();
	}

	/*
	private void setParentAccountViewLink(IFieldGroupModelBinding bindingDef) {
		// set the parent account view link
		Model accountModel = bindingDef.resolveModel(SmbizEntityType.ACCOUNT);
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
