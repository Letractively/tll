/**
 * The Logic Lab
 * @author jpk
 * Feb 21, 2008
 */
package com.tll.client.ui.field.user;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.mvc.view.EditViewInitializer;
import com.tll.client.mvc.view.account.AccountEditView;
import com.tll.client.ui.field.AddressFieldsRenderer;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.view.ViewLink;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;

/**
 * UserPanel
 * @author jpk
 */
public class UserPanel extends FlowFieldPanel {

	class UserFieldsRenderer implements IFieldRenderer<FlowPanel> {

		@SuppressWarnings("synthetic-access")
		public void render(FlowPanel pnl, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(pnl);

			// first row
			cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getFieldWidgetByName("userEmailAddress"));
			cmpsr.addField(fg.getFieldWidgetByName("userExpires"));

			// parent account ref link
			try {
				final Model parentAccount = getModel().getNestedModel("account");
				final String paName = parentAccount.asString(Model.NAME_PROPERTY);
				lnkAccount.setViewNames(paName, paName);
				lnkAccount.setViewInitializer(new EditViewInitializer(AccountEditView.klas, parentAccount));
				cmpsr.addWidget("Account", lnkAccount);
			}
			catch(final PropertyPathException e) {
				Log.warn("No parent account for user", e);
			}

			cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_CREATED_PROPERTY));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_MODIFIED_PROPERTY));
			cmpsr.resetFlow();

			// second row
			cmpsr.newRow();
			cmpsr.addField(fg.getFieldWidgetByName("userLocked"));
			cmpsr.stopFlow();
			cmpsr.addField(fg.getFieldWidgetByName("userEnabled"));
			cmpsr.resetFlow();

			// third row
			cmpsr.newRow();
			final FlowPanel fp = new FlowPanel();
			final AddressFieldsRenderer afr = new AddressFieldsRenderer();
			afr.render(fp, fg);
			dpAddress.add(fp);
			cmpsr.addWidget(dpAddress);
		}
	}

	private final ViewLink lnkAccount = new ViewLink();
	private final DisclosurePanel dpAddress = new DisclosurePanel("Address", false);

	@Override
	protected FieldGroup generateFieldGroup() {
		return (new UserFieldsProvider()).getFieldGroup();
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new UserFieldsRenderer();
	}
}
