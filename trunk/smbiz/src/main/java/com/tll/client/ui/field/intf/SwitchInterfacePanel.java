/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.bind.Binding;
import com.tll.client.ui.field.DefaultFieldBindingBuilder;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldBindingBuilder;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;

/**
 * SwitchInterfacePanel - One option exists that is either on or off.
 * @author jpk
 */
public final class SwitchInterfacePanel extends AbstractInterfacePanel {

	/**
	 * BindingBuilder - Custom binding impl
	 * @author jpk
	 */
	static class BindingBuilder extends DefaultFieldBindingBuilder {

		@Override
		protected void postCreateBindings(Binding binding) {
			//binding.getChildren().add(createBinding("options[0].name", "intfname"));
			//binding.getChildren().add(createBinding("options[0].code", "intfCode"));
			//binding.getChildren().add(createBinding("options[0].description", "intfDescription"));
		}
	} // Binding

	private final ParamsPanel paramsPanel;
	private final DisclosurePanel dpParams;

	/**
	 * Constructor
	 */
	public SwitchInterfacePanel() {
		paramsPanel = new ParamsPanel("options[0].parameters");
		dpParams = new DisclosurePanel("Parameters");
		dpParams.add(paramsPanel);
	}

	@Override
	protected IFieldBindingBuilder getFieldBindingBuilder() {
		return new BindingBuilder();
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
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidget("intfname"));
				final IFieldWidget<?> fw = fg.getFieldWidget("optnDefault");
				fw.setLabelText("On");
				cmpsr.stopFlow();
				cmpsr.addField(fw);
				cmpsr.resetFlow();
				cmpsr.addField(fg.getFieldWidget("intfCode"));
				cmpsr.addField(fg.getFieldWidget("intfDescription"));

				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidget("intfdateCreated"));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidget("intfdateModified"));
				cmpsr.resetAlignment();

				cmpsr.newRow();
				cmpsr.addWidget(createAvailabilityWidget(fg));

				final OptionRenderer optionRenderer = new OptionRenderer(true, dpParams, cmpsr);
				optionRenderer.render(widget, fg);
			}
		};
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { paramsPanel };
	}
}
