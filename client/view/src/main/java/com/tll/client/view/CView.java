package com.tll.client.view;

import com.tll.client.ui.view.ViewContainer;

/**
 * CView - Simple encapsulation of a view and its initializer ensuring that the
 * view key in the view equals that in the initializer.
 * @author jpk
 */
final class CView extends AbstractViewKeyProvider {

	final ViewContainer vc;
	final IViewInitializer init;

	/**
	 * Constructor
	 * @param vc
	 * @param init
	 */
	public CView(final ViewContainer vc, final IViewInitializer init) {
		if(vc == null || init == null) throw new IllegalArgumentException("Null view and/or init.");
		if(!vc.getViewKey().equals(init.getViewKey())) {
			throw new IllegalArgumentException("Un-equal view keys");
		}
		this.vc = vc;
		this.init = init;
	}

	@Override
	public ViewKey getViewKey() {
		return vc.getViewKey();
	}

	@Override
	public String toString() {
		return "CView[" + init.getViewKey() + "]";
	}
}