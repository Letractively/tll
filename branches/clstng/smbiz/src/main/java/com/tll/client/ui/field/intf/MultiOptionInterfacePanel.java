/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.bind.IModel;
import com.tll.common.model.Model;
import com.tll.model.SmbizEntityType;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 */
public final class MultiOptionInterfacePanel extends AbstractInterfacePanel<FlowPanel> {

	/**
	 * OptionPanel
	 * @author jpk
	 */
	static final class OptionPanel extends FieldPanel<FlowPanel> {

		FlowPanel canvas = new FlowPanel();

		/**
		 * Constructor
		 */
		public OptionPanel() {
			super();
			initWidget(canvas);
		}

		@Override
		protected FieldGroup generateFieldGroup() {
			return (new OptionFieldProvider()).getFieldGroup();
		}

		@Override
		public IFieldRenderer<FlowPanel> getRenderer() {
			throw new UnsupportedOperationException();
		}
	}

	class OptionRenderer implements IFieldRenderer<FlowPanel> {

		public void render(FlowPanel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(panel);

			// first row
			cmpsr.addField(fg.getFieldWidgetByName(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getFieldWidgetByName("code"));
			cmpsr.addField(fg.getFieldWidgetByName("description"));

			// pricing
			cmpsr.newRow();
			final Grid g = new Grid(2, 3);
			g.setWidget(0, 0, fg.getFieldByName("optnSetUpCost").getWidget());
			g.setWidget(0, 1, fg.getFieldByName("optnMonthlyCost").getWidget());
			g.setWidget(0, 2, fg.getFieldByName("optnAnnualCost").getWidget());
			g.setWidget(1, 0, fg.getFieldByName("optnBaseSetupPrice").getWidget());
			g.setWidget(1, 1, fg.getFieldByName("optnBaseMonthlyPrice").getWidget());
			g.setWidget(1, 2, fg.getFieldByName("optnBaseAnnualPrice").getWidget());
			cmpsr.addWidget(g);

			// cmpsr.newRow();
			// cmpsr.addWidget(paramListing);
		}
	}

	final class OptionsPanel extends TabbedIndexedFieldPanel<OptionPanel> {

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
			//try {
			final IFieldWidget<?> fw = index.getFieldGroup().getFieldWidget(Model.NAME_PROPERTY);
			return fw.getText();
			//}
			//catch(final UnsetPropertyException e) {
			//throw new IllegalStateException(e);
			//}
		}

		@Override
		protected IModel createPrototypeModel() {
			return AuxDataCache.instance().getEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
		}

		@Override
		protected OptionPanel createIndexPanel() {
			return new OptionPanel();
		}

	} // OptionsPanel

	private final FlowPanel canvas = new FlowPanel();

	// private final OptionsPanel optionsPanel = new OptionsPanel();

	/**
	 * Constructor
	 */
	public MultiOptionInterfacePanel() {
		super();
		initWidget(canvas);
	}

	@Override
	protected FieldGroup generateFieldGroup() {
		final FieldGroup fg = (new InterfaceFieldProvider()).getFieldGroup();
		// fg.addField("options", optionsPanel.getFieldGroup());
		return fg;
	}

	@Override
	public IFieldRenderer<FlowPanel> getRenderer() {
		return null;
	}

}