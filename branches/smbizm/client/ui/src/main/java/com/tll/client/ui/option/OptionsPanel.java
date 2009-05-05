/**
 * The Logic Lab
 * @author jpk Dec 6, 2007
 */
package com.tll.client.ui.option;

import java.util.HashMap;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * OptionsPanel - Panel containing a vertical list of options that are
 * selectable via mouse and keyboard.
 * @author jpk
 */
public class OptionsPanel extends FocusPanel implements KeyDownHandler, MouseDownHandler, MouseOverHandler,
IHasOptionHandlers {

	/**
	 * Styles - (options.css)
	 * @author jpk
	 */
	protected static class Styles {

		public static final String OPTIONS = "options";

		public static final String ACTIVE = "active";
	}

	/**
	 * MRegs
	 * @author jpk
	 */
	static class MRegs {

		final HandlerRegistration out, over;

		public MRegs(HandlerRegistration out, HandlerRegistration over) {
			super();
			this.out = out;
			this.over = over;
		}
	} // MRegs

	protected final HashMap<Option, MRegs> options = new HashMap<Option, MRegs>();
	private final VerticalPanel vp = new VerticalPanel();
	private int crntIndx = -1;

	/**
	 * Constructor
	 */
	public OptionsPanel() {
		super();
		setWidget(vp);
		addKeyDownHandler(this);
		setStyleName(Styles.OPTIONS);
	}

	@Override
	public HandlerRegistration addOptionHandler(IOptionHandler handler) {
		return addHandler(handler, OptionEvent.TYPE);
	}

	/**
	 * Adds a single Option
	 * @param option The Option to add
	 */
	private void addOption(Option option) {
		final MRegs mreg = new MRegs(option.addMouseDownHandler(this), option.addMouseOverHandler(this));
		options.put(option, mreg);
		vp.add(option);
	}

	private void clearOptions() {
		crntIndx = -1;
		MRegs m;
		for(final Option option : options.keySet()) {
			vp.remove(option);
			m = options.get(option);
			m.out.removeHandler();
			m.over.removeHandler();
		}
		options.clear();
	}

	/**
	 * Sets the {@link Option}s in this panel given an array of them. Clears any
	 * existing options before the new ones are added.
	 * @param options Array of {@link Option}s to be set
	 */
	public void setOptions(Option[] options) {
		clearOptions();
		if(options != null) {
			for(final Option element2 : options) {
				addOption(element2);
			}
		}
	}

	private void clearCurrentOption() {
		if(crntIndx != -1) {
			vp.getWidget(crntIndx).getElement().getParentElement().setClassName("");
			crntIndx = -1;
		}
	}

	private void setCurrentOption(int index, boolean fireCurrentOptionChanged) {
		if(crntIndx != -1 && crntIndx == index) {
			return;
		}
		else if(index > options.size() - 1) {
			index = 0;
		}
		else if(index < 0) {
			index = options.size() - 1;
		}

		// unset current
		if(crntIndx != -1) {
			vp.getWidget(crntIndx).getElement().getParentElement().setClassName("");
		}

		// set new current
		final Option crntOption = (Option) vp.getWidget(index);
		crntOption.getElement().getParentElement().setClassName(Styles.ACTIVE);
		this.crntIndx = index;

		if(fireCurrentOptionChanged) {
			fireEvent(new OptionEvent(OptionEvent.EventType.CHANGED, crntOption.getText()));
		}
	}

	public void onKeyDown(KeyDownEvent event) {
		switch(event.getNativeKeyCode()) {
			case KeyCodes.KEY_UP:
				setCurrentOption(crntIndx - 1, true);
				break;
			case KeyCodes.KEY_DOWN:
				setCurrentOption(crntIndx + 1, true);
				break;
			case KeyCodes.KEY_ENTER:
				if(crntIndx >= 0) {
					fireEvent(new OptionEvent(OptionEvent.EventType.SELECTED, ((Option) vp.getWidget(crntIndx)).getText()));
				}
				break;
		}
	}

	public void onMouseDown(MouseDownEvent event) {
		final int index = vp.getWidgetIndex((Option) event.getSource());
		if(index >= 0) {
			setCurrentOption(index, false);
			fireEvent(new OptionEvent(OptionEvent.EventType.SELECTED, ((Option) event.getSource()).getText()));
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		final int index = vp.getWidgetIndex((Option) event.getSource());
		if(index >= 0) {
			setCurrentOption(index, false);
			fireEvent(new OptionEvent(OptionEvent.EventType.CHANGED, ((Option) event.getSource()).getText()));
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		clearCurrentOption();
	}

}
