package com.tll.client.ui.msg;

import java.util.Collection;
import java.util.Iterator;

import com.tll.client.ui.Position;

/**
 * MsgOperatorAggregate - A collection of distinct {@link IMsgOperator}s that
 * acts as a single {@link IMsgOperator}.
 * @author jpk
 */
final class MsgOperatorAggregate implements IMsgOperator, Iterable<IMsgOperator> {

	private final Collection<? extends IMsgOperator> operators;

	/**
	 * Constructor
	 * @param operators
	 */
	public MsgOperatorAggregate(Collection<? extends IMsgOperator> operators) {
		super();
		if(operators == null) throw new IllegalArgumentException();
		this.operators = operators;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IMsgOperator> iterator() {
		return (Iterator<IMsgOperator>) operators.iterator();
	}

	@Override
	public void clearMsgs() {
		for(final IMsgOperator o : operators) {
			o.clearMsgs();
		}
	}

	@Override
	public void hide() {
		for(final IMsgOperator o : operators) {
			o.hide();
		}
	}

	@Override
	public void show() {
		for(final IMsgOperator o : operators) {
			o.show();
		}
	}

	@Override
	public void show(Position position, int duration) {
		for(final IMsgOperator o : operators) {
			o.show(position, duration);
		}
	}

	@Override
	public void toggle() {
		for(final IMsgOperator o : operators) {
			o.toggle();
		}
	}

	@Override
	public void showMsgLevelImages(boolean show) {
		for(final IMsgOperator o : operators) {
			o.showMsgLevelImages(show);
		}
	}
}