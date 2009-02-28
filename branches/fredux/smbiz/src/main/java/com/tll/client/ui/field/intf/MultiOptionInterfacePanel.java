/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.TabbedIndexedFieldPanel;
import com.tll.common.bind.IBindable;
import com.tll.common.model.Model;
import com.tll.model.SmbizEntityType;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 * @param <M>
 */
public final class MultiOptionInterfacePanel<M extends IBindable> extends AbstractInterfacePanel<FlowPanel, M> {

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

	}

	class OptionRenderer implements IFieldRenderer<FlowPanel> {

		public void render(FlowPanel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(panel);

			// first row
			cmpsr.addField(fg.getFieldByName(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getFieldByName("code"));
			cmpsr.addField(fg.getFieldByName("description"));

			// pricing
			cmpsr.newRow();
			final Grid g = new Grid(2, 3);
			g.setWidget(0, 0, (Widget) fg.getFieldByName("optnSetUpCost"));
			g.setWidget(0, 1, (Widget) fg.getFieldByName("optnMonthlyCost"));
			g.setWidget(0, 2, (Widget) fg.getFieldByName("optnAnnualCost"));
			g.setWidget(1, 0, (Widget) fg.getFieldByName("optnBaseSetupPrice"));
			g.setWidget(1, 1, (Widget) fg.getFieldByName("optnBaseMonthlyPrice"));
			g.setWidget(1, 2, (Widget) fg.getFieldByName("optnBaseAnnualPrice"));
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
			super("Options", true, true);
		}

		@Override
		protected String getIndexTypeName() {
			return "Option";
		}

		@Override
		protected String getTabLabelText(Index<OptionPanel> index) {
			//try {
			final IFieldWidget<?> fw = (IFieldWidget<?>) index.getFieldPanel().getFieldGroup().getField(Model.NAME_PROPERTY);
			return fw.getText();
			//}
			//catch(final UnsetPropertyException e) {
			//throw new IllegalStateException(e);
			//}
		}

		@SuppressWarnings("unchecked")
		@Override
		protected M createPrototypeModel() {
			return (M) AuxDataCache.instance().getEntityPrototype(SmbizEntityType.INTERFACE_OPTION);
		}

		@Override
		protected OptionPanel createIndexPanel(IBindable model) {
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
}
