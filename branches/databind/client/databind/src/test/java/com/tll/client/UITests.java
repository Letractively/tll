package com.tll.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.test.TestFieldPanel;
import com.tll.client.ui.GridRenderer;
import com.tll.client.ui.edit.EditEvent;
import com.tll.client.ui.edit.IEditHandler;
import com.tll.client.ui.edit.ModelEditPanel;
import com.tll.client.ui.field.FieldErrorHandler;
import com.tll.client.ui.field.FieldFactory;
import com.tll.client.ui.field.FieldGroup;
import com.tll.client.ui.field.FlowPanelFieldComposer;
import com.tll.client.ui.field.GridFieldComposer;
import com.tll.client.ui.field.IFieldGroupProvider;
import com.tll.client.ui.field.IFieldWidget;
import com.tll.client.ui.field.RadioGroupField.GridStyles;
import com.tll.client.ui.msg.GlobalMsgPanel;
import com.tll.client.ui.msg.MsgPopupRegistry;
import com.tll.client.ui.test.ModelViewer;
import com.tll.client.util.GlobalFormat;
import com.tll.client.validate.ErrorHandlerBuilder;
import com.tll.client.validate.ErrorHandlerDelegate;
import com.tll.common.model.CopyCriteria;
import com.tll.common.model.Model;
import com.tll.common.model.RelatedManyProperty;
import com.tll.common.model.test.TestModelStubber;

/**
 * UI Tests - GWT module for the sole purpose of verifying the DOM/Style of
 * compiled GWT code.
 */
public final class UITests extends AbstractUITest {

	@Override
	protected String getTestSubjectName() {
		return "client-field module";
	}

	@Override
	protected UITestCase[] getTestCases() {
		return new UITestCase[] {
			new GridFieldComposerTest(), new FlowPanelFieldComposerTest(), new FieldBindingLifecycleTest()
		};
	}

	/**
	 * TestEnum
	 * @author jpk
	 */
	static enum TestEnum {
		ENUM_1,
		ENUM_2,
		ENUM_3,
		ENUM_4,
		ENUM_5,
		ENUM_6,
		ENUM_7,
		ENUM_8;
	}

	/**
	 * FieldProvider
	 * @author jpk
	 */
	static class FieldProvider implements IFieldGroupProvider {

		@Override
		public FieldGroup getFieldGroup() {
			final FieldGroup group = new FieldGroup("Test Fields");

			final Map<String, String> data = new LinkedHashMap<String, String>();
			data.put("valueA", "Key1");
			data.put("valueB", "Key2");
			data.put("valueC", "Key3");
			data.put("valueD", "Key4");
			data.put("valueE", "Key5");
			data.put("valueF", "Key6");
			data.put("valueG", "Key7");
			data.put("valueH", "Key8");
			data.put("valueI", "Key9");
			data.put("valueJ", "Key10");

			IFieldWidget<String> sfw;
			IFieldWidget<Boolean> bfw;
			IFieldWidget<Date> dfw;
			IFieldWidget<TestEnum> efw;
			IFieldWidget<Collection<String>> cfw;

			sfw = FieldFactory.ftext("ftext", "ftext", "TextField", "TextField", 8);
			sfw.setValue("ival");
			sfw.setRequired(true);
			group.addField(sfw);

			sfw = FieldFactory.ftextarea("ftextarea", "ftextarea", "Textarea", "Textarea", 5, 10);
			sfw.setValue("ival");
			sfw.setRequired(true);
			group.addField(sfw);

			sfw = FieldFactory.fpassword("fpassword", "fpassword", "Password", "Password", 8);
			sfw.setValue("ival");
			sfw.setRequired(true);
			group.addField(sfw);

			dfw = FieldFactory.fdate("fdate", "fdate", "DateField", "DateField", GlobalFormat.DATE);
			dfw.setValue(new Date());
			dfw.setRequired(true);
			group.addField(dfw);

			bfw = FieldFactory.fcheckbox("fcheckbox", "fcheckbox", "Checkbox", "Checkbox");
			bfw.setValue(Boolean.TRUE);
			bfw.setRequired(true);
			group.addField(bfw);

			sfw = FieldFactory.fselect("fselect", "fselect", "Select", "Select", data);
			sfw.setValue("valueC");
			sfw.setRequired(true);
			group.addField(sfw);

			cfw = FieldFactory.fmultiselect("fmultiselect", "fmultiselect", "Multi-select", "Multi-select", data);
			final ArrayList<String> ival = new ArrayList<String>();
			ival.add("valueA");
			ival.add("valueE");
			ival.add("valueJ");
			cfw.setValue(ival);
			cfw.setRequired(true);
			group.addField(cfw);

			sfw =
				FieldFactory.fradiogroup("fradiogroup", "fradiogroup", "Radio Group", "Radio Group", data, new GridRenderer(
						3, GridStyles.GRID));
			sfw.setValue("valueB");
			sfw.setRequired(true);
			group.addField(sfw);

			sfw = FieldFactory.fsuggest("fsuggest", "fsuggest", "Suggest", "Suggest", data);
			sfw.setValue("valueB");
			sfw.setRequired(true);
			group.addField(sfw);

			efw =
				FieldFactory.fenumradio("fenumradio", "fenumradio", "Enum Radio", "Enum Radio", TestEnum.class,
						new GridRenderer(3, GridStyles.GRID));
			efw.setValue(TestEnum.ENUM_7);
			efw.setRequired(true);
			group.addField(efw);

			return group;
		}

	} // FieldProvider

	/**
	 * GridFieldComposerTest - tests the rendering of fields using the {@link GridFieldComposer}.
	 * @author jpk
	 */
	static final class GridFieldComposerTest extends DefaultUITestCase {

		HorizontalPanel context;
		FlowPanel pfields;
		ValueChangeDisplay vcd;
		MsgPopupRegistry mregistry;
		FieldGroup group;
		Button[] testActions;

		/**
		 * Constructor
		 */
		public GridFieldComposerTest() {
			super("GridFieldComposer Test", "Renders fields using the GridFieldComposer");
		}

		static class ValueChangeDisplay extends Composite {

			final VerticalPanel outer = new VerticalPanel();
			final VerticalPanel events = new VerticalPanel();
			final ScrollPanel sp = new ScrollPanel();
			final Button btnClear = new Button("Clear");

			/**
			 * Constructor
			 */
			public ValueChangeDisplay() {
				super();
				sp.add(events);
				sp.setHeight("300px");
				outer.add(btnClear);
				outer.add(sp);
				initWidget(outer);
				btnClear.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						events.clear();
					}
				});
			}

			private String getRowString(ValueChangeEvent<?> event) {
				final IFieldWidget<?> source = (IFieldWidget<?>) event.getSource();
				String val;
				if(event.getValue() instanceof Collection<?>) {
					final Collection<?> clc = (Collection<?>) event.getValue();
					final StringBuilder sb = new StringBuilder();
					for(final Object e : clc) {
						sb.append(", ");
						sb.append(e.toString());
					}
					val = sb.length() == 0 ? "-" : sb.substring(1);
				}
				else {
					val = event.getValue() == null ? "-" : event.getValue().toString();
				}
				// FORMAT: {source}|{value}
				final StringBuilder sb = new StringBuilder();
				sb.append(source.getName());
				sb.append(" | ");
				sb.append(val);
				return sb.toString();
			}

			public void addRow(ValueChangeEvent<?> event) {
				events.add(new Label(getRowString(event), false));
			}
		}

		@SuppressWarnings("unchecked")
		private void generateFields() {
			group = new FieldProvider().getFieldGroup();
			final ValueChangeHandler vch = new ValueChangeHandler() {

				@Override
				public void onValueChange(ValueChangeEvent event) {
					vcd.addRow(event);
				}
			};
			for(final IFieldWidget<?> fw : group.getFieldWidgets(null)) {
				fw.addValueChangeHandler(vch);
			}

			// set error handler for all fields to test error handling
			mregistry = new MsgPopupRegistry();
			group.setErrorHandler(new FieldErrorHandler(mregistry));
		}

		@Override
		protected Widget getContext() {
			return context;
		}

		@Override
		protected Button[] getTestActions() {
			return testActions;
		}

		private void stubTestActions() {
			assert testActions == null;
			testActions = new Button[] {
				new Button("enable/disable", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						group.setEnabled(!group.isEnabled());
					}
				}), new Button("editable/read-only", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						group.setReadOnly(!group.isReadOnly());
					}
				}), new Button("visible/not visible", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						group.setVisible(!group.isVisible());
					}
				})
			};
		}

		@Override
		public void init() {
			context = new HorizontalPanel();
			context.setSpacing(7);
			context.setBorderWidth(1);
			context.getElement().getStyle().setProperty("margin", "1em");
			context.getElement().getStyle().setProperty("border", "1px solid gray");

			pfields = new FlowPanel();
			vcd = new ValueChangeDisplay();
			context.add(pfields);
			context.add(vcd);

			final GridFieldComposer composer = new GridFieldComposer();
			composer.setCanvas(pfields);
			generateFields();
			for(final IFieldWidget<?> f : group.getFieldWidgets(null)) {
				composer.addField(f);
			}

			stubTestActions();
		}

		@Override
		public void teardown() {
			testActions = null;
			group = null;
			mregistry.clear();
			mregistry = null;
			vcd = null;
			pfields = null;
			context = null;
		}

	} // GridFieldComposerTest

	/**
	 * FlowPanelFieldComposerTest - Renders fields using the {@link FlowPanelFieldComposer}.
	 * @author jpk
	 */
	static class FlowPanelFieldComposerTest extends DefaultUITestCase {

		FlowPanel context;
		MsgPopupRegistry mregistry;
		FieldGroup group;
		Button[] testActions;

		/**
		 * Constructor
		 */
		public FlowPanelFieldComposerTest() {
			super("FlowPanelFieldComposer Test", "Renders fields using the FlowPanelFieldComposer");
		}

		@Override
		protected Button[] getTestActions() {
			return testActions;
		}

		private void stubTestActions() {
			assert testActions == null;
			testActions = new Button[] {
				new Button("enable/disable", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						group.setEnabled(!group.isEnabled());
					}
				}), new Button("editable/read-only", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						group.setReadOnly(!group.isReadOnly());
					}
				}), new Button("visible/not visible", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						group.setVisible(!group.isVisible());
					}
				})
			};
		}

		@Override
		public void init() {
			context = new FlowPanel();
			context.getElement().getStyle().setProperty("margin", "1em");
			context.getElement().getStyle().setProperty("padding", "1em");
			context.getElement().getStyle().setProperty("border", "1px solid gray");

			final FlowPanelFieldComposer composer = new FlowPanelFieldComposer();
			composer.setCanvas(context);

			group = new FieldProvider().getFieldGroup();
			composer.addField(group.getFieldWidget("ftext"));
			composer.addField(group.getFieldWidget("fselect"));
			composer.addField(group.getFieldWidget("fcheckbox"));
			composer.newRow();
			composer.addField(group.getFieldWidget("ftextarea"));
			composer.addField(group.getFieldWidget("fpassword"));
			composer.addField(group.getFieldWidget("fdate"));
			composer.newRow();
			composer.addField(group.getFieldWidget("fmultiselect"));
			composer.addField(group.getFieldWidget("fradiogroup"));
			composer.addField(group.getFieldWidget("fsuggest"));
			composer.addField(group.getFieldWidget("fenumradio"));

			// set error handler for all fields to test error handling
			mregistry = new MsgPopupRegistry();
			group.setErrorHandler(new FieldErrorHandler(mregistry));

			stubTestActions();
		}

		@Override
		public void teardown() {
			testActions = null;
			group = null;
			mregistry.clear();
			mregistry = null;
			context = null;
		}

		@Override
		protected Widget getContext() {
			return context;
		}

	} // FlowPanelFieldComposerTest

	/**
	 * FieldBindingLifecycleTest - Tests field/model binding functionality through
	 * a mocked model field panel and edit panel.
	 * @author jpk
	 */
	static final class FieldBindingLifecycleTest extends UITestCase {

		HorizontalPanel layout;
		VerticalPanel context;
		GlobalMsgPanel gmp;
		TestFieldPanel fp;
		ErrorHandlerDelegate eh;
		ModelEditPanel ep;
		ModelViewer mv, mvchanged;
		Model m;

		@Override
		public String getName() {
			return "Model/Field binding";
		}

		@Override
		public String getDescription() {
			return "Tests model/field binding with handling a nested model collection.";
		}

		@Override
		public void load() {
			layout = new HorizontalPanel();
			layout.setSpacing(7);
			layout.setBorderWidth(1);
			layout.getElement().getStyle().setProperty("margin", "1em");

			gmp = new GlobalMsgPanel();

			mv = new ModelViewer();
			mvchanged = new ModelViewer();
			fp = new TestFieldPanel();
			eh = ErrorHandlerBuilder.build(true, true, new GlobalMsgPanel());
			ep = new ModelEditPanel(fp, false, false, true);
			ep.setErrorHandler(eh, true);

			final CopyCriteria mcrit = CopyCriteria.all();

			ep.addEditHandler(new IEditHandler<Model>() {

				@Override
				public void onEdit(EditEvent<Model> event) {
					String version;

					// mimic model persist life-cycle
					assert m != null;
					final Model mcopy = m.copy(mcrit);
					version = mcopy.getVersion();
					Integer iversion = Integer.valueOf(version);
					mcopy.setVersion(version == null ? null : Integer.toString(iversion.intValue()+1));
					ArrayList<Model> alist = null;
					List<Model> existing;
					final RelatedManyProperty ap = mcopy.relatedMany("addresses");
					existing = ap.getModelList();
					if(existing != null) {
						alist = new ArrayList<Model>();
						for(final Model am : existing) {
							if(!am.isMarkedDeleted()) {
								version = am.getVersion();
								iversion = Integer.valueOf(version);
								am.setVersion(version == null ? null : Integer.toString(iversion.intValue()+1));
								alist.add(am);
							}
						}
						ap.setValue(alist);
					}
					ep.setModel(mcopy);
					mv.setModel(mcopy);
					mvchanged.setModel(event.getContent());
					m = mcopy;
				}
			});

			context = new VerticalPanel();
			context.getElement().getStyle().setProperty("margin", "5px");
			context.add(gmp);
			context.add(ep);

			layout.add(context);
			VerticalPanel vp = new VerticalPanel();
			vp.add(new Label("Model props"));
			vp.add(mv);
			layout.add(vp);
			vp = new VerticalPanel();
			vp.add(new Label("Changed model props"));
			vp.add(mvchanged);
			layout.add(vp);

			RootPanel.get().add(layout);

			m = TestModelStubber.stubAccount(false);
			ep.setModel(m);
			mv.setModel(m);
		}

		@Override
		public void unload() {
			if(layout != null) {
				layout.removeFromParent();
				layout = null;
			}
			context = null;
			gmp = null;
			fp.unbind();
			ep = null;
			mv = null;
			m = null;
			mvchanged = null;
		}

	} // FieldBindingLifecycleTest
}
