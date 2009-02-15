/**
 * The Logic Lab
 * @author jpk Dec 11, 2007
 */
package com.tll.client.ui.option;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.tll.client.ui.TimedPopup;

/**
 * OptionsPopup
 * @author jpk
 */
public abstract class OptionsPopup extends TimedPopup implements MouseDownHandler, MouseOverHandler, MouseOutHandler,
		IOptionHandler,
		IHasOptionHandlers {

	protected final OptionsPanel optionsPanel = new OptionsPanel();

	//private final KeyboardListenerCollection keyboardListeners = new KeyboardListenerCollection();

	/**
	 * Constructor
	 * @param duration
	 */
	public OptionsPopup(int duration) {
		super(true, false, duration);
		optionsPanel.addOptionHandler(this);
		addHandler(optionsPanel, KeyDownEvent.getType());
		setWidget(optionsPanel);
	}

	public void setOptions(Option[] options) {
		optionsPanel.setOptions(options);
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		if(!event.isCanceled()) {
			switch(event.getTypeInt()) {
				case Event.ONKEYDOWN: {
					final int key = event.getNativeEvent().getKeyCode();
					switch(key) {
						case KeyCodes.KEY_UP:
						case KeyCodes.KEY_DOWN:
						case KeyCodes.KEY_ENTER:
							//keyboardListeners.fireKeyboardEvent(this, event);
							fireEvent(event);
							event.cancel(); // TODO or consume?
						case KeyCodes.KEY_ESCAPE:
							hide();
							event.cancel(); // TODO or consume?
					}
				}
			}
		}
	}

	public void onMouseDown(MouseDownEvent event) {
		final Element elm = event.getNativeEvent().getTarget();
		// need to UN-offset the positioning of the focus panel (the sender)
		setPopupPosition(event.getClientX() + 13 + elm.getAbsoluteLeft(), event.getClientY() - 5 + elm.getAbsoluteTop());
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		startTimer();
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		cancelTimer();
	}

	@Override
	public HandlerRegistration addOptionHandler(IOptionHandler handler) {
		return optionsPanel.addOptionHandler(handler);
	}

	@Override
	public void onOptionEvent(OptionEvent event) {
		assert (event.getSource() == optionsPanel);
		switch(event.getOptionEventType()) {
			case CHANGED:
				cancelTimer();
				break;
			case SELECTED:
				hide();
				break;
		}
	}
}
