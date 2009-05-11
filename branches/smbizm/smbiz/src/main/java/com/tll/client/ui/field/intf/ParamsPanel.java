package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.client.ui.field.intf.AbstractInterfacePanel.ParamDefFieldProvider;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;

/**
 * ParamsPanel
 * @author jpk
 */
final class ParamsPanel extends TabbedIndexedFieldPanel<ParamsPanel.ParamPanel> {

	/**
	 * ParamPanel
	 * @author jpk
	 */
	static final class ParamPanel extends FlowFieldPanel {

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new ParamDefFieldProvider()).getFieldGroup();
		}

		@Override
		protected IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel widget, FieldGroup fg) {
					final GridFieldComposer cmpsr = new GridFieldComposer();
					cmpsr.setCanvas(widget);

					cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
					cmpsr.addField(fg.getFieldWidgetByName("paramCode"));
					cmpsr.addField(fg.getFieldWidgetByName("paramDescription"));
				}
			};
		}

	}

	/**
	 * Constructor
	 */
	public ParamsPanel() {
		super("Params", "parameters", true, true);
	}

	@Override
	protected String getIndexTypeName() {
		return "Param";
	}

	@Override
	protected String getInstanceName(ParamPanel index) {
		return index.getFieldGroup().getFieldWidget(Model.NAME_PROPERTY).getText();
	}

	@Override
	protected Model createPrototypeModel() {
		return ModelAssembler.assemble(SmbizEntityType.INTERFACE_OPTION_PARAMETER_DEFINITION);
	}

	@Override
	protected ParamPanel createIndexPanel() {
		return new ParamPanel();
	}

}