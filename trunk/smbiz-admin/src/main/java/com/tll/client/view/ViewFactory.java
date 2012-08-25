package com.tll.client.view;

import java.util.LinkedHashMap;

public class ViewFactory implements IViewFactory {
	
	private final IHomeView homeView = new HomeView();
	
	private static final LinkedHashMap<Class<IView>, IView> cache = new LinkedHashMap<Class<IView>, IView>();
	
	static {
		// specify all ViewClass impls here
		ViewClass.addClass(new HomeView.Clz());
	}
	
	private final int capacity;

	/**
	 * Constructor
	 */
	public ViewFactory() {
		this(3);
	}

	/**
	 * Constructor
	 * @param capacity max number of views to hold in cache.
	 */
	public ViewFactory(int capacity) {
		super();
		this.capacity = capacity;
	}

	@Override
	public IView getDefaultView() {
		return homeView;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IView> T getView(Class<T> def) {
		T view = (T) cache.get(def);
		if(view == null) {
			ViewClass vc = ViewClass.findClassByViewName(def.getName());
			view = (T) vc.newView();
			cache.put((Class<IView>) def, view);
			if(cache.size() > capacity) {
				// trim (from head)
				cache.remove(cache.values().iterator().next());
			}
		}
		return view;
	}

}
