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
 * Db4oPagingSearchListHandlerTest
 * @author jpk
 */
@Test(groups = {
	"listhandler", "db4o"
})
public class Db4oPagingSearchListHandlerTest extends AbstractPagingSearchListHandlerTest {

	@Override
	protected Config doGetConfig() {
		return Config.load(new ConfigRef("db4o-config.properties"));
	}
}
