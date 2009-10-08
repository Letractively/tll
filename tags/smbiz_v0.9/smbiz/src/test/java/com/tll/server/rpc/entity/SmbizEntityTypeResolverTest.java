/**
 * The Logic Lab
 * @author jpk
 * @since May 17, 2009
 */
package com.tll.server.rpc.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.tll.common.model.IEntityType;
import com.tll.common.model.SmbizEntityType;
import com.tll.model.EntityBase;
import com.tll.util.CommonUtil;


/**
 * SmbizEntityTypeResolverTest
 * @author jpk
 */
@Test
public class SmbizEntityTypeResolverTest {
	private static final Log log = LogFactory.getLog(SmbizEntityTypeResolver.class);

	@Test
	public void testResolveEntityType() throws Exception {
		final IEntityTypeResolver resolver = new SmbizEntityTypeResolver();
		IEntityType etype;
		final Class<?>[] eclasses = CommonUtil.getClasses("com.tll.model", EntityBase.class, true, null, null);
		for(final Class<?> ec : eclasses) {
			etype = resolver.resolveEntityType(ec);
			assert etype != null;
			log.debug(ec.getName() + " --> " + etype);
		}
	}

	@Test
	public void testResolveEntityClass() throws Exception {
		final IEntityTypeResolver resolver = new SmbizEntityTypeResolver();
		Class<?> ec;
		for(final SmbizEntityType et : SmbizEntityType.values()) {
			ec = resolver.resolveEntityClass(et);
			assert et != null;
			log.debug(et + " --> " + ec.getName());
		}
	}

	@Test
	public void testResolveBad() throws Exception {
		final IEntityTypeResolver resolver = new SmbizEntityTypeResolver();
		try {
			resolver.resolveEntityType(this.getClass());
			Assert.fail("Resolved bad entity class!");
		}
		catch(final IllegalArgumentException e) {
			// expected
		}
		try {
			resolver.resolveEntityClass(new IEntityType() {

				@Override
				public String descriptor() {
					return "bad";
				}
			});
			Assert.fail("Resolved bad entity type!");
		}
		catch(final IllegalArgumentException e) {
			// expected
		}
	}
}
