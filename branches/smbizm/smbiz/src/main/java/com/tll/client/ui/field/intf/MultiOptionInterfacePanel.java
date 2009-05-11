/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 */
public final class MultiOptionInterfacePanel extends AbstractInterfacePanel {

	/**
	 * OptionPanel
	 * @author jpk
	 */
	static final class OptionPanel extends FlowFieldPanel {

		private final ParamsPanel paramsPanel;
		private final DisclosurePanel dpParams;

		/**
		 * Constructor
		 */
		public OptionPanel() {
			paramsPanel = new ParamsPanel();
			dpParams = new DisclosurePanel("Parameters", false);
			dpParams.add(paramsPanel);
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			final FieldGroup fg = (new OptionFieldProvider()).getFieldGroup();
			fg.addField("parameters", paramsPanel.getFieldGroup());
			return fg;
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			return new OptionRenderer(false, dpParams, new FlowPanelFieldComposer());
		}

		@Override
		public IIndexedFieldBoundWidget[] getIndexedChildren() {
			return new IIndexedFieldBoundWidget[] { paramsPanel };
		}
	}

	/**
	 * OptionsPanel
	 * @author jpk
	 */
	static final class OptionsPanel extends TabbedIndexedFieldPanel<OptionPanel> {

		/**
		 * Constructor
		 */
		public OptionsPanel() {
			super("Options", "options", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Option";
		}

		@Override
		protected String getInstanceName(OptionPanel index) {
			return index.getFieldGroup().getFieldWidget(Model.NAME_PROPERTY).getText();
		}

		@Override
		protected Model createPrototypeModel() {
			return ModelAssembler.assemble(SmbizEntityType.INTERFACE_OPTION);
		}

		@Override
		protected OptionPanel createIndexPanel() {
			return new OptionPanel();
		}

	} // OptionsPanel

	final OptionsPanel optionsPanel = new OptionsPanel();
	final DisclosurePanel dpOptions = new DisclosurePanel("Options", true);

	/**
	 * Constructor
	 */
	public MultiOptionInterfacePanel() {
		dpOptions.add(optionsPanel);
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new InterfaceFieldProvider()).getFieldGroup();
		fg.addField("options", optionsPanel.getFieldGroup());
		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);

				// first row
				cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidgetByName("intfCode"));
				cmpsr.addField(fg.getFieldWidgetByName("intfDescription"));

				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidgetByName(Model.DATE_MODIFIED_PROPERTY));
				cmpsr.resetAlignment();

				// second row
				cmpsr.newRow();
				cmpsr.addWidget(createAvailabilityWidget(fg));

				// third row
				cmpsr.addWidget(dpOptions);
			}
		};
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { optionsPanel };
	}
}
