/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.common.model.Model;

/**
 * AccountSwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class AccountSwitchInterfacePanel extends AbstractAccountInterfacePanel {

	private final AccountParamsPanel paramsPanel = new AccountParamsPanel("options[0].parameters");

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = new FieldGroup("Account Switch Interface");

		// the switch option
		fg.addField(FieldFactory.fcheckbox("subscribed", "options[0].subscribed", "Subscribed?", "Subscribed?"));
		fg.addField(FieldFactory.ftext("setUpPrice", "options[0].setUpPrice", "Set Up Price", "Set Up Price", 8));
		fg.addField(FieldFactory.ftext("monthlyPrice", "options[0].monthlyPrice", "Monthly Price", "Monthly Price", 8));
		fg.addField(FieldFactory.ftext("annualPrice", "options[0].annualPrice", "Annual Price", "Annual Price", 8));

		fg.addField(paramsPanel.getFieldGroup());

		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				final Model m = getModel();
				assert m != null;

				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidget("subscribed"));
				cmpsr.addField(fg.getFieldWidget("setUpPrice"));
				cmpsr.addField(fg.getFieldWidget("monthlyPrice"));
				cmpsr.addField(fg.getFieldWidget("annualPrice"));

				if(m.relatedMany("options[0].parameters").size() > 0) {
					cmpsr.newRow();
					cmpsr.addWidget(paramsPanel);
				}
			}
		};
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { paramsPanel };
	}
}
