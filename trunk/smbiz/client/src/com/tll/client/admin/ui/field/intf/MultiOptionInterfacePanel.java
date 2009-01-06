/**
 * The Logic Lab
 * @author jkirton
 * May 21, 2008
 */
package com.tll.client.admin.ui.field.intf;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.bind.IBindable;
import com.tll.client.model.Model;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FieldPanel;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.IFieldRenderer;
import com.tll.client.ui.field.IndexedFieldPanel;

/**
 * MultiOptionInterfacePanel - Interface panel for interfaces where more than
 * one option is allowed.
 * @author jpk
 * @param <M>
 */
public final class MultiOptionInterfacePanel<M extends IBindable> extends AbstractInterfacePanel<M> {

	/**
	 * OptionPanel
	 * @author jpk
	 */
	static final class OptionPanel<M extends IBindable> extends FieldPanel<M> {

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

	class OptionRenderer implements IFieldRenderer {

		public void render(Panel panel, FieldGroup fg) {
			final FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
			cmpsr.setCanvas(panel);

			// first row
			cmpsr.addField(fg.getField(Model.NAME_PROPERTY));
			cmpsr.addField(fg.getField("code"));
			cmpsr.addField(fg.getField("description"));

			// pricing
			cmpsr.newRow();
			Grid g = new Grid(2, 3);
			g.setWidget(0, 0, (Widget) fg.getField("setUpCost"));
			g.setWidget(0, 1, (Widget) fg.getField("monthlyCost"));
			g.setWidget(0, 2, (Widget) fg.getField("annualCost"));
			g.setWidget(1, 0, (Widget) fg.getField("baseSetupPrice"));
			g.setWidget(1, 1, (Widget) fg.getField("baseMonthlyPrice"));
			g.setWidget(1, 2, (Widget) fg.getField("baseAnnualPrice"));
			cmpsr.addWidget(g);

			// cmpsr.newRow();
			// cmpsr.addWidget(paramListing);
		}
	}

	final class OptionsPanel extends IndexedFieldPanel<M> implements TabListener {

		private final TabPanel tabOptions = new TabPanel();

		/**
		 * Constructor
		 */
		public OptionsPanel() {
			super("options", new OptionFieldProvider());
			tabOptions.addTabListener(this);
			initWidget(tabOptions);
			setRenderer(new IFieldRenderer() {

				public void render(Panel panel, FieldGroup fg) {
					FlowPanelFieldComposer cmpsr = new FlowPanelFieldComposer();
					cmpsr.setCanvas(panel);

					cmpsr.addField(fg.getField(Model.NAME_PROPERTY));
					cmpsr.addField(fg.getField("code"));
					cmpsr.addField(fg.getField("description"));
					cmpsr.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
					cmpsr.addField(fg.getField(Model.DATE_CREATED_PROPERTY));
					cmpsr.stopFlow();
					cmpsr.addField(fg.getField(Model.DATE_MODIFIED_PROPERTY));
					cmpsr.resetAlignment();

					// availability
					cmpsr.newRow();
					cmpsr.addWidget(createAvailabilityGrid(fg));

					// options
					cmpsr.newRow();
					cmpsr.addWidget(optionsPanel);
				}
			});
		}

		public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
			// TODO re-impl
			// OptionPanel op = (OptionPanel) tabOptions.getWidget(tabIndex);
			// op.paramListing.refresh();
			return true;
		}

		public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
		}

	} // OptionsPanel

	private final FlowPanel canvas = new FlowPanel();

	private final OptionsPanel optionsPanel = new OptionsPanel();

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
		fg.addField("options", optionsPanel.getFieldGroup());
		return fg;
	}
}
