package com.tll.client.mvc;

import com.tll.client.mvc.view.AbstractViewKeyProvider;
import com.tll.client.mvc.view.IViewInitializer;
import com.tll.client.mvc.view.ViewKey;
import com.tll.client.mvc.view.ViewOptions;
import com.tll.client.ui.view.ViewContainer;

/**
 * ViewAndInit - Simple encapsulation of a view and its initializer ensuring
 * that the view key in the view equals that in the initializer.
 * @author jpk
 */
final class ViewAndInit extends AbstractViewKeyProvider {

	final ViewContainer vc;
	final IViewInitializer init;
	final ViewOptions options;

	/**
	 * Constructor
	 * @param vc
	 * @param init
	 * @param options
	 */
	public ViewAndInit(ViewContainer vc, IViewInitializer init, ViewOptions options) {
		if(vc == null || init == null || options == null) throw new IllegalArgumentException("Null view and/or init.");
		if(!vc.getViewKey().equals(init.getViewKey())) {
			throw new IllegalArgumentException("Un-equal view keys");
		}
		this.vc = vc;
		this.init = init;
		this.options = options;
	}

	public ViewKey getViewKey() {
		return vc.getViewKey();
	}

	@Override
	public String toString() {
		return "ViewAndInit[" + init.getViewKey() + "]";
	}
}