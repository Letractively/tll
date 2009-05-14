/**
 * The Logic Lab
 * @author jpk
 * @since May 10, 2009
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.Style;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.common.model.Model;


/**
 * OptionRenderer
 * @author jpk
 */
class OptionRenderer implements IFieldRenderer<FlowPanel> {

	private final FlowPanelFieldComposer cmpsr;
	private final boolean isSwitch;
	private final Widget paramsWidget;

	/**
	 * Constructor
	 * @param isSwitch
	 * @param paramsWidget
	 * @param cmpsr
	 */
	public OptionRenderer(boolean isSwitch, Widget paramsWidget, FlowPanelFieldComposer cmpsr) {
		super();
		this.isSwitch = isSwitch;
		this.paramsWidget = paramsWidget;
		this.cmpsr = cmpsr;
	}

	@Override
	public void render(FlowPanel widget, FieldGroup fg) {
		cmpsr.setCanvas(widget);

		if(!isSwitch) {
			cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getFieldWidgetByName("optnCode"));
			cmpsr.addField(fg.getFieldWidgetByName("optnDefault"));
			cmpsr.newRow();
			cmpsr.addField(fg.getFieldWidgetByName("optnDescription"));
			cmpsr.newRow();
		}
		else {
			final IFieldWidget<?> fw = fg.getFieldWidgetByName("optnCode");
			fw.setLabelText("On by default?");
			cmpsr.addField(fw);
		}

		// pricing
		final FlowPanel fp = new FlowPanel();
		final GridFieldComposer pc = new GridFieldComposer();
		pc.setCanvas(fp);

		pc.addFieldTitle("Cost");
		pc.addField(fg.getFieldWidgetByName("optnSetUpCost"));
		pc.addField(fg.getFieldWidgetByName("optnMonthlyCost"));
		pc.addField(fg.getFieldWidgetByName("optnAnnualCost"));

		pc.addFieldTitle("Pricing");
		pc.addField(fg.getFieldWidgetByName("optnBaseSetupPrice"));
		pc.addField(fg.getFieldWidgetByName("optnBaseMonthlyPrice"));
		pc.addField(fg.getFieldWidgetByName("optnBaseAnnualPrice"));

		cmpsr.addWidget(fp);
		cmpsr.addFieldContainerStyle(fp, Style.GAP_LEFT);

		cmpsr.addWidget(paramsWidget);
		cmpsr.addFieldContainerStyle(paramsWidget, Style.GAP_LEFT);
	}

}
