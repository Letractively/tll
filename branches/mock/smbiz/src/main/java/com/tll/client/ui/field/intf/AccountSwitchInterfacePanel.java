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
import com.tll.common.model.PropertyPathException;

/**
 * AccountSwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class AccountSwitchInterfacePanel extends AbstractAccountInterfacePanel {

	private final AccountParamsPanel paramsPanel = new AccountParamsPanel("options[0].parameters");
	private final InterfaceOptionSummary ioSmry = new InterfaceOptionSummary();

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

				cmpsr.addWidget(ioSmry);

				cmpsr.newRow();
				cmpsr.addField(fg.getFieldWidget("subscribed"));
				cmpsr.addField(fg.getFieldWidget("setUpPrice"));
				cmpsr.addField(fg.getFieldWidget("monthlyPrice"));
				cmpsr.addField(fg.getFieldWidget("annualPrice"));

				cmpsr.newRow();
				cmpsr.addWidget(paramsPanel);
			}
		};
	}

	@Override
	public void setModel(Model model) {
		super.setModel(model);
		try {
			ioSmry.apply(model.getNestedModel("options[0]"));
		}
		catch(final PropertyPathException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { paramsPanel };
	}
}
