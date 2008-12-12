/**
 * The Logic Lab
 * @author jpk Dec 6, 2007
 */
package com.tll.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tll.client.event.IOptionListener;
import com.tll.client.event.ISourcesOptionEvents;
import com.tll.client.event.type.OptionEvent;

/**
 * OptionsPanel - Panel containing a vertical list of options that are
 * selectable via mouse and keyboard.
 * @author jpk
 */
public class OptionsPanel extends FocusPanel implements KeyboardListener, MouseListener, ISourcesOptionEvents {

	private static final String STYLE_OPTIONS = "options";

	protected final List<Option> options = new ArrayList<Option>();
	private final VerticalPanel vp = new VerticalPanel();
	private int crntIndx = -1;
	private OptionListenerCollection optionListeners;

	/**
	 * Constructor
	 */
	public OptionsPanel() {
		super();
		setWidget(vp);
		addKeyboardListener(this);
		setStyleName(STYLE_OPTIONS);
	}

	public void addOptionListener(IOptionListener listener) {
		if(optionListeners == null) {
			optionListeners = new OptionListenerCollection();
		}
		optionListeners.add(listener);
	}

	public void removeOptionListener(IOptionListener listener) {
		if(optionListeners != null) {
			optionListeners.remove(listener);
		}
	}

	/**
	 * Adds a single Option
	 * @param option The Option to add
	 */
	private void addOption(Option option) {
		options.add(option);
		vp.add(option);
		option.addMouseListener(this);
	}

	/**
	 * Removes an Option.
	 * @param option The Option to remove from the UI panel
	 * @param rmv Remove from {@link #options}?
	 */
	private void removeOption(Option option, boolean rmv) {
		option.removeMouseListener(this);
		vp.remove(option);
		if(rmv) options.remove(option);
	}

	private void clearOptions() {
		crntIndx = -1;
		for(Option option : options) {
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
			for(Option element2 : options) {
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
		options.get(index).getElement().getParentElement().setClassName(CSS.ACTIVE);
		this.crntIndx = index;

		if(fireCurrentOptionChanged && optionListeners != null) {
			optionListeners.fireOnCurrentChanged(new OptionEvent(this, options.get(crntIndx).getText()));
		}
	}

	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
		switch(keyCode) {
			case KeyboardListener.KEY_UP:
				setCurrentOption(crntIndx - 1, true);
				break;
			case KeyboardListener.KEY_DOWN:
				setCurrentOption(crntIndx + 1, true);
				break;
			case KeyboardListener.KEY_ENTER:
				if(crntIndx >= 0 && optionListeners != null) {
					optionListeners.fireOnSelected(new OptionEvent(this, options.get(crntIndx).getText()));
				}
				break;
		}
	}

	public void onKeyPress(Widget sender, char keyCode, int modifiers) {
	}

	public void onKeyUp(Widget sender, char keyCode, int modifiers) {
	}

	public void onMouseDown(Widget sender, int x, int y) {
		final int index = options.indexOf(sender);
		if(index >= 0) {
			setCurrentOption(index, false);
			if(optionListeners != null) {
				optionListeners.fireOnSelected(new OptionEvent(this, ((Option) sender).getText()));
			}
		}
	}

	public void onMouseEnter(Widget sender) {
		final int index = options.indexOf(sender);
		if(index >= 0) {
			setCurrentOption(index, false);
			if(optionListeners != null) {
				optionListeners.fireOnCurrentChanged(new OptionEvent(this, ((Option) sender).getText()));
			}
		}
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseUp(Widget sender, int x, int y) {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		clearCurrentOption();
	}

}
