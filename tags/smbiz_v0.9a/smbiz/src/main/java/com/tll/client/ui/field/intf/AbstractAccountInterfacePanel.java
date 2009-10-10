/**
 * The Logic Lab
 * @author jpk
 * @since Sep 11, 2009
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowFieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.client.ui.field.TextField;
import com.tll.common.model.Model;
import com.tll.common.model.PropertyPathException;

/**
 * AbstractAccountInterfacePanel
 * @author jpk
 */
abstract class AbstractAccountInterfacePanel extends FlowFieldPanel {

	/**
	 * AccountParameterPanel - Represents an interface option parameter that
	 * handles account subscribing and un-subscribing.
	 * @author jpk
	 */
	static class AccountParameterPanel extends FlowFieldPanel {

		final Label lblName, lblCode, lblDesc;
		TextField value;

		public AccountParameterPanel() {
			lblName = new Label();
			lblCode = new Label();
			lblDesc = new Label();
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			final FieldGroup fg = new FieldGroup("Account Parameters");

			value = FieldFactory.ftext("value", "value", "Value", "Parameter value", 10);
			fg.addField(value);

			return fg;
		}

		@Override
		protected IFieldRenderer<FlowPanel> getRenderer() {
			return new IFieldRenderer<FlowPanel>() {

				@Override
				public void render(FlowPanel widget, FieldGroup fg) {
					final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(widget);

					cmpsr.addWidget("Name", lblName);
					cmpsr.addWidget("Code", lblCode);
					cmpsr.addWidget("Description", lblDesc);
					cmpsr.addField(value);
				}
			};
		}

		@Override
		public void setModel(Model aio) {
			super.setModel(aio);
			lblName.setText(aio.asString("name"));
			lblName.setText(aio.asString("code"));
			lblDesc.setText(aio.asString("description"));
		}

	} // AccountParameterPanel

	/**
	 * AccountParamsPanel - Renders a collection of
	 * {@link AbstractAccountInterfacePanel.AccountParameterPanel} panels.
	 * @author jpk
	 */
	static class AccountParamsPanel extends TabbedIndexedFieldPanel<AccountParameterPanel> {

		/**
		 * Constructor
		 * @param indexPropPath
		 */
		public AccountParamsPanel(String indexPropPath) {
			super("Parameters", indexPropPath, false, false);
		}

		@Override
		protected String getIndexTypeName() {
			return "Account Option Parameter";
		}

		@Override
		protected String getInstanceName(AccountParameterPanel index) {
			String pname;
			try {
				pname = (String) index.getModel().getProperty("name");
			}
			catch(final PropertyPathException e) {
				throw new IllegalStateException(e);
			}
			return pname;
		}

		@Override
		protected AccountParameterPanel createIndexPanel() {
			return new AccountParameterPanel();
		}

		@Override
		protected Model createPrototypeModel() {
			//return ModelAssembler.assemble(SmbizEntityType.ACCOUNT_INTERFACE_OPTION_PARAMETER);
			throw new UnsupportedOperationException();
		}

	} // ParametersPanel
}
