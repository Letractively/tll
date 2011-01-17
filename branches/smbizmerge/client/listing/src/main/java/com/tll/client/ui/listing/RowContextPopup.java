package com.tll.client.ui.listing;

import com.tll.client.listing.IRowOptionsDelegate;
import com.tll.client.ui.option.Option;
import com.tll.client.ui.option.OptionEvent;
import com.tll.client.ui.option.OptionsPopup;

/**
 * RowContextPopup - The {@link Option}s panel pop-up.
 * @author jpk
 */
public final class RowContextPopup extends OptionsPopup {

	/**
	 * The bound {@link IRowOptionsDelegate}
	 */
	private final IRowOptionsDelegate delegate;

	/**
	 * The row index for this row context.
	 */
	private int rowIndex = -1;

	/**
	 * Constructor
	 * @param duration the time in mili-seconds to show the popup or
	 *        <code>-1</code> meaning it is shown indefinitely.
	 * @param delegate the required row ops delegate
	 */
	public RowContextPopup(int duration, IRowOptionsDelegate delegate) {
		super(duration);
		if(delegate == null) throw new IllegalArgumentException("Null delegate");
		this.delegate = delegate;
	}

	@Override
	public void onOptionEvent(OptionEvent event) {
		if(delegate == null) throw new IllegalStateException("No row op delegate set");
		//Log.debug("RowContextPopup - onOptionEvent event: " + event.toDebugString());
		super.onOptionEvent(event);
		if(event.getOptionEventType() == OptionEvent.EventType.SELECTED) {
			delegate.handleOptionSelection(event.optionText, rowIndex);
		}
	}

}