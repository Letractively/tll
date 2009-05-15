/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.bind.FieldModelBinding;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.common.model.Model;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class SwitchInterfacePanel extends AbstractInterfacePanel {

	private final ParamsPanel paramsPanel;
	private final DisclosurePanel dpParams;
	private final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
	private final OptionRenderer optionRenderer;

	/**
	 * Constructor
	 */
	public SwitchInterfacePanel() {
		super();
		paramsPanel = new ParamsPanel();
		dpParams = new DisclosurePanel("Parameters", false);
		dpParams.add(paramsPanel);
		optionRenderer = new OptionRenderer(true, dpParams, cmpsr);
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new InterfaceFieldProvider()).getFieldGroup();

		// the switch option
		fg.addField("options[0]", (new OptionFieldProvider(true)).getFieldGroup());

		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@SuppressWarnings("synthetic-access")
			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidgetByName("intf" + Model.NAME_PROPERTY));
				final IFieldWidget<?> fw = fg.getFieldWidgetByName("optnDefault");
				fw.setLabelText("On");
				cmpsr.stopFlow();
				cmpsr.addField(fw);
				cmpsr.resetFlow();
				cmpsr.addField(fg.getFieldWidgetByName("intfCode"));
				cmpsr.addField(fg.getFieldWidgetByName("intfDescription"));

				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidgetByName("intf" + Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidgetByName("intf" + Model.DATE_MODIFIED_PROPERTY));
				cmpsr.resetAlignment();

				cmpsr.newRow();
				cmpsr.addWidget(createAvailabilityWidget(fg));

				optionRenderer.render(widget, fg);

			}
		};
	}

	@Override
	public void setBinding(FieldModelBinding binding) {
		super.setBinding(binding);
		// add additional bindings to propagate the interface name/code/desc to the
		// single switch option
		final FieldGroup fg = getFieldGroup();
		getBinding().addBinding("options[0].name", fg.getFieldWidgetByName("intfname"));
		getBinding().addBinding("options[0].code", fg.getFieldWidgetByName("intfCode"));
		getBinding().addBinding("options[0].description", fg.getFieldWidgetByName("intfDescription"));
	}
}
