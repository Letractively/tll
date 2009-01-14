/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;
import com.tll.client.cache.AuxDataCache;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IndexedFieldPanel;
import com.tll.model.EntityType;

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
	static final class OptionPanel<M extends IBindable> extends FieldPanel<FlowPanel, M> {

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
			Grid g = new Grid(2, 3);
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

	final class OptionsPanel extends IndexedFieldPanel<OptionPanel<M>, M> implements TabListener {

		private final TabPanel tabOptions = new TabPanel();

		/**
		 * Constructor
		 */
		public OptionsPanel() {
			super("Options");
			tabOptions.addTabListener(this);
			initWidget(tabOptions);
		}

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			// TODO re-impl
			// OptionPanel op = (OptionPanel) tabOptions.getWidget(tabIndex);
			// op.paramListing.refresh();
			return true;
		}

		@Override
		protected void draw() {
			tabOptions.clear();

			// add the index field panels to the tab panel
			for(OptionPanel<M> p : this) {
				tabOptions.add(p, p.getFieldGroup().getFieldByName(Model.NAME_PROPERTY).getText());
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		protected M createPrototypeModel() {
			return (M) AuxDataCache.instance().getEntityPrototype(EntityType.INTERFACE_OPTION);
		}

		@Override
		protected OptionPanel<M> createIndexPanel(M model) {
			return new OptionPanel<M>();
		}

		public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
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
		FieldGroup fg = (new InterfaceFieldProvider()).getFieldGroup();
		// fg.addField("options", optionsPanel.getFieldGroup());
		return fg;
	}
}
