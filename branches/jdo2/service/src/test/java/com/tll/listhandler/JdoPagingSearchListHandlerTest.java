/**
 * The Logic Lab
 * @author jpk
 * @since Oct 31, 2009
 */
package com.tll.listhandler;

import org.testng.annotations.Test;

import com.tll.config.Config;
import com.tll.config.ConfigRef;


/**
 * JdoPagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = {
	"dao", "jdo"
})
public class JdoPagingSearchListHandlerTest extends AbstractPagingSearchListHandlerTest {

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("jdo-config.properties"));
	}

}
