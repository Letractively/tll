/**
 * The Logic Lab
 * @author jpk Dec 6, 2007
 */
package com.tll.client.ui.option;

import java.util.ArrayList;
import java.util.List;

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

	protected final List<Option> options = new ArrayList<Option>();
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
		options.add(option);
		vp.add(option);
		option.addMouseDownHandler(this);
		option.addMouseOverHandler(this);
	}

	/**
	 * Removes an Option.
	 * @param option The Option to remove from the UI panel
	 * @param rmv Remove from {@link #options}?
	 */
	private void removeOption(Option option, boolean rmv) {
		//option.removeMouseListener(this);
		vp.remove(option);
		if(rmv) options.remove(option);
	}

	private void clearOptions() {
		crntIndx = -1;
		for(final Option option : options) {
			removeOption(option, false);
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
			options.get(crntIndx).getElement().getParentElement().setClassName("");
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
			options.get(crntIndx).getElement().getParentElement().setClassName("");
		}

		// set new current
		options.get(index).getElement().getParentElement().setClassName(Styles.ACTIVE);
		this.crntIndx = index;

		if(fireCurrentOptionChanged) {
			fireEvent(new OptionEvent(OptionEvent.EventType.CHANGED, options.get(crntIndx).getText()));
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
					fireEvent(new OptionEvent(OptionEvent.EventType.SELECTED, options.get(crntIndx).getText()));
				}
				break;
		}
	}

	public void onMouseDown(MouseDownEvent event) {
		final int index = options.indexOf(event.getSource());
		if(index >= 0) {
			setCurrentOption(index, false);
			fireEvent(new OptionEvent(OptionEvent.EventType.SELECTED, ((Option) event.getSource()).getText()));
		}
	}

	public void onMouseOver(MouseOverEvent event) {
		final int index = options.indexOf(event.getSource());
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
