/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import java.util.Set;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.tll.client.model.ModelAssembler;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.client.validate.IValidator;
import com.tll.client.validate.ValidationException;
import com.tll.common.model.Model;
import com.tll.common.model.SmbizEntityType;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 */
public final class MultiOptionInterfacePanel extends AbstractInterfacePanel {

	/**
	 * Selector for obtaining all the option default fields.
	 */
	private static final String OPTION_DEFAULTS_REGEX = "options\\[\\d\\]\\.default";

	/**
	 * OptionPanel
	 * @author jpk
	 */
	final class OptionPanel extends FlowFieldPanel {

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

		@SuppressWarnings("unchecked")
		@Override
		protected FieldGroup generateFieldGroup() {
			final FieldGroup fg = (new OptionFieldProvider(false)).getFieldGroup();
			if(single) {
				// add click handler to the default field to un-check other option's
				// default property
				final IFieldWidget<Boolean> thisone = ((IFieldWidget<Boolean>) fg.getFieldWidget("default"));
				thisone.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						if(event.getValue() == Boolean.TRUE) {
							final Set<IFieldWidget<?>> set =
								MultiOptionInterfacePanel.this.getFieldGroup().getFieldWidgets(OPTION_DEFAULTS_REGEX);
							if(set.size() > 0) {
								for(final IFieldWidget<?> fw : set) {
									if(fw != thisone) ((IFieldWidget<Boolean>) fw).setValue(Boolean.FALSE);
								}
							}
						}
					}
				});
			}
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
	final class OptionsPanel extends TabbedIndexedFieldPanel<OptionPanel> {

		/**
		 * Constructor
		 */
		public OptionsPanel() {
			super("Options", "options", true, true);
			// add group level validation for ensuring a default option exists
			getFieldGroup().addValidator(new IValidator() {

				@SuppressWarnings("unchecked")
				@Override
				public Object validate(Object value) throws ValidationException {
					final Set<IFieldWidget<?>> set =
						MultiOptionInterfacePanel.this.getFieldGroup().getFieldWidgets(OPTION_DEFAULTS_REGEX);
					if(set.size() > 0) {
						boolean one = false;
						for(final IFieldWidget<?> fw : set) {
							if(((IFieldWidget<Boolean>) fw).getValue() == Boolean.TRUE) {
								one = true;
								break;
							}
						}
						if(!one) throw new ValidationException("One option must be set as the default.");
					}
					return null;
				}
			});
		}

		@Override
		protected String getIndexTypeName() {
			return "Option";
		}

		@Override
		protected String getInstanceName(OptionPanel index) {
			return index.getFieldGroup().getFieldWidgetByName("optn" + Model.NAME_PROPERTY).getText();
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

	final boolean single;
	final OptionsPanel optionsPanel;
	final DisclosurePanel dpOptions;

	/**
	 * Constructor
	 * @param single Single or Multi type interface?
	 */
	public MultiOptionInterfacePanel(boolean single) {
		this.single = single;
		optionsPanel = new OptionsPanel();
		dpOptions = new DisclosurePanel("Options", true);
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
				cmpsr.addField(fg.getFieldWidgetByName("intf" + Model.NAME_PROPERTY));
				cmpsr.addField(fg.getFieldWidgetByName("intfCode"));
				cmpsr.addField(fg.getFieldWidgetByName("intfDescription"));

				cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
				cmpsr.addField(fg.getFieldWidgetByName("intf" + Model.DATE_CREATED_PROPERTY));
				cmpsr.stopFlow();
				cmpsr.addField(fg.getFieldWidgetByName("intf" + Model.DATE_MODIFIED_PROPERTY));
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
