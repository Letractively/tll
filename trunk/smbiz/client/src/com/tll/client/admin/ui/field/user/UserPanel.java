/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.admin.ui.field.user;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.tll.client.admin.ui.field.AddressFieldsRenderer;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.view.ViewRequestLink;

/**
 * UserPanel
 * @author jpk
 * @param <M> the model type
 */
public class UserPanel<M extends IBindable> extends FieldPanel<M> {

	class UserFieldsRenderer implements IFieldRenderer {

		public void render(Panel panel, FieldGroup fg, String parentPropPath) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(canvas);

			// first row
			cmpsr.addField(fg.getField(parentPropPath, "name"));
			cmpsr.addField(fg.getField(parentPropPath, "emailAddress"));
			cmpsr.addField(fg.getField(parentPropPath, "locked"));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField(parentPropPath, "enabled"));
			cmpsr.resetFlow();
			cmpsr.addField(fg.getField(parentPropPath, "expires"));

			// parent account ref link
			lnkAccount = new ViewRequestLink();
			cmpsr.addWidget("Account", lnkAccount);

			cmpsr.addField(fg.getField(parentPropPath, Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getField(parentPropPath, Model.DATE_MODIFIED_PROPERTY));
			cmpsr.resetFlow();

			// third row
			cmpsr.newRow();
			FlowPanel fp = new FlowPanel();
			AddressFieldsRenderer afr = new AddressFieldsRenderer();
			afr.render(fp, (FieldGroup) fg.getField(parentPropPath, "address"), null);
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
