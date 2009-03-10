package com.tll.client.validate;

import com.tll.client.ui.IWidgetRef;

/**
 * IErrorHandler - Definition for handling {@link IError}s.
 * @author jpk
 */
public interface IErrorHandler {

	/**
	 * Attrib - Attributes that tells the handler "how" to handle the error.
	 * @author jpk
	 */
	public static enum Attrib {
		/**
		 * Indicates that <em>no</em> errors should be displayed at all.
		 */
		NONE(0),
		/**
		 * Indicates errors should be displayed "globally".
		 */
		GLOBAL(1),
		/**
		 * Indicates errors should be displayed "locally".
		 */
		LOCAL(1 << 1);

		private final int flag;

		/**
		 * Constructor
		 * @param flag
		 */
		private Attrib(int flag) {
			this.flag = flag;
		}
		
		public int flag() {
			return flag;
		}

		public static boolean isNone(int flags) {
			return flags == 0;
		}

		public static boolean isGlobal(int flags) {
			return ((flags & GLOBAL.flag) == flags);
		}

		public static boolean isLocal(int flags) {
			return ((flags & LOCAL.flag) == flags);
		}
	}
	
	/**
	 * Handles a validation error.
	 * @param source the error source
	 * @param error the error
	 * @param attribs bit flag with unioned {@link Attrib} flags indicating how to
	 *        handle the error.
	 *        <p>
	 *        E.g.:
	 *        <code>setHandlingAttribs(Attrib.LOCAL.flag & Attrib.GLOBAL.flag)</code>
	 *        tells the handler to display error feedback both locally and
	 *        globally.
	 */
	void handleError(IWidgetRef source, IError error, int attribs);

	/**
	 * Resolves (clears) all validation errors for the given source.
	 * @param source
	 */
	void resolveError(IWidgetRef source);
	
	/**
	 * Life-cycle provision to clear out <em>all</em> errors and internal state.
	 */
	void clear();
}
