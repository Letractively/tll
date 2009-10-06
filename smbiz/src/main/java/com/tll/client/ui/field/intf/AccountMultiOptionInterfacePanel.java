/**
 * The Logic Lab
 * @author jpk
 * @since May 13, 2009
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.IIndexedFieldBoundWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.client.util.Fmt;
import com.tll.client.util.GlobalFormat;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;

/**
 * AccountInterfacePanel - Displays account interface option data for multi-type interfaces.
 * @author jpk
 */
public class AccountMultiOptionInterfacePanel extends AbstractAccountInterfacePanel {

	/**
	 * AccountOptionPanel - Manages the interface option to account bindings for a
	 * given interface option and account.
	 * @author jpk
	 */
	static class AccountOptionPanel extends FlowFieldPanel {

		final Label lblDesc = new Label(null, false);
		final Label lblDflt = new Label(null, false);
		final AccountParamsPanel paramsPanel = new AccountParamsPanel("parameters");

		@SuppressWarnings("unchecked")
		@Override
		protected FieldGroup generateFieldGroup() {
			final FieldGroup fg = new FieldGroup("Account Interface Option");
			fg.addField(FieldFactory.fcheckbox("subscribed", "subscribed", "Subscribed?", "Subscribed?"));
			fg.addField(FieldFactory.ftext("setUpPrice", "setUpPrice", "Set Up Price", "Set Up Price", 8));
			fg.addField(FieldFactory.ftext("monthlyPrice", "monthlyPrice", "Monthly Price", "Monthly Price", 8));
			fg.addField(FieldFactory.ftext("annualPrice", "annualPrice", "Annual Price", "Annual Price", 8));
			fg.addField(paramsPanel.getFieldGroup());

			((IFieldWidget<Boolean>) fg.getFieldWidget("subscribed"))
			.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				public void onValueChange(ValueChangeEvent<Boolean> event) {
					paramsPanel.enable(event.getValue());
				}
			});

			return fg;
		}

		@Override
		protected IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel widget, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(widget);

					final Model m = getModel();
					assert m != null;

					cmpsr.addField(fg.getFieldWidget("subscribed"));
					cmpsr.newRow();

					lblDesc.setText(m.asString("description"));
					cmpsr.addWidget("Description", lblDesc);
					cmpsr.newRow();

					try {
						lblDflt.setText(Fmt.format(m.getPropertyValue("default").getValue(), GlobalFormat.BOOL_YESNO));
					}
					catch(final Exception e) {
						throw new IllegalArgumentException(e);
					}
					cmpsr.addWidget("Default?", lblDflt);
					cmpsr.newRow();

					cmpsr.addField(fg.getFieldWidget("setUpPrice"));
					cmpsr.addField(fg.getFieldWidget("monthlyPrice"));
					cmpsr.addField(fg.getFieldWidget("annualPrice"));

					if(m.relatedMany("parameters").size() > 0) {
						cmpsr.newRow();
						cmpsr.addWidget("Parameters", paramsPanel);
					}
				}
			};
		}

		@Override
		public IIndexedFieldBoundWidget[] getIndexedChildren() {
			return new IIndexedFieldBoundWidget[] { paramsPanel };
		}
	} // AccountOptionPanel

	static class OptionsPanel extends TabbedIndexedFieldPanel<AccountOptionPanel> {

		public OptionsPanel() {
			super("Options", "options", false, false);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Option";
		}

		@Override
		protected String getInstanceName(AccountOptionPanel index) {
			String oname;
			try {
				oname = (String) index.getModel().getProperty("name");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}
			return oname;
		}

		@Override
		protected AccountOptionPanel createIndexPanel() {
			return new AccountOptionPanel();
		}

		@Override
		protected Model createPrototypeModel() {
			return null;
		}
	} // OptionsPanel

	final boolean single;
	final OptionsPanel optionsPanel = new OptionsPanel();

	/**
	 * Constructor
	 * @param single
	 */
	public AccountMultiOptionInterfacePanel(boolean single) {
		this.single = single;
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = new FieldGroup("Account Interface");
		fg.addField(optionsPanel.getFieldGroup());
		return fg;
	}

	@Override
	protected IFieldRenderer<FlowPanel> getRenderer() {
		return new IFieldRenderer<FlowPanel>() {

			@Override
			public void render(FlowPanel widget, FieldGroup fg) {
				final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
				cmpsr.setCanvas(widget);
				cmpsr.addWidget(optionsPanel);
			}
		};
	}

	@Override
	public IIndexedFieldBoundWidget[] getIndexedChildren() {
		return new IIndexedFieldBoundWidget[] { optionsPanel };
	}
}
