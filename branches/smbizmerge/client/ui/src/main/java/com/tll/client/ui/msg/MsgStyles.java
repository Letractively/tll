package com.tll.client.ui.msg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.tll.common.msg.Msg.MsgLevel;

/**
 * @author jpk
 */
public class MsgStyles {

	public interface MsgCss extends CssResource {

		String msg();
		
		String cmsg();
		
		String gmsg();
		
		String container();
		
		String title();
	}
	
	public interface MsgResources extends ClientBundle {

		//@Import({Reset.class, Base.class})
		@Source("msg.css")
		@NotStrict
		MsgCss css();

		/**
		 * info
		 * @return the image prototype
		 */
		@Source(value = "info.gif")
		ImageResource info();

		/**
		 * warn
		 * @return the image prototype
		 */
		@Source(value = "warn.gif")
		ImageResource warn();

		/**
		 * error
		 * @return the image prototype
		 */
		@Source(value = "error.gif")
		ImageResource error();

		/**
		 * fatal
		 * @return the image prototype
		 */
		@Source(value = "fatal.gif")
		ImageResource fatal();
	}
	
	/**
	 * Provides a new {@link Image} containing the associated msg level icon.
	 * @param level The message level
	 * @return Image
	 */
	public static ImageResource getMsgLevelImage(MsgLevel level) {
		switch(level) {
			case WARN:
				return resources.warn();
			case ERROR:
				return resources.error();
			case FATAL:
				return resources.fatal();
			default:
			case INFO:
				return resources.info();
		}
	}
	
	private static MsgResources resources;
	
	static {
    resources = GWT.create(MsgResources.class);
    resources.css().ensureInjected();
	}
	
  public static MsgCss css() {
    return resources.css();
  }

  public static MsgResources resources() {
    return resources;
  }
}
