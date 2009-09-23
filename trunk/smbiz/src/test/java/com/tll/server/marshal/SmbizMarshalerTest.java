/**
 * The Logic Lab
 */
package com.tll.server.marshal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.common.model.SmbizEntityType;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.di.Db4oDbShellModule;
import com.tll.di.SmbizDb4oDaoModule;
import com.tll.di.SmbizEGraphModule;
import com.tll.di.SmbizModelModule;
import com.tll.model.EntityBeanFactory;
import com.tll.model.EntityGraph;
import com.tll.model.IEntity;
import com.tll.model.Merchant;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.IMarshalOptionsResolver;
import com.tll.server.rpc.entity.SmbizEntityTypeResolver;
import com.tll.util.CommonUtil;

/**
 * SmbizMarshallerTest - Tests the marshaling of the smbiz defined entities.
 * @author jpk
 */
@Test(groups = {"server", "client-model" })
public class SmbizMarshalerTest extends AbstractDbAwareTest {

	protected static final Map<String, Object> tupleMap = new HashMap<String, Object>();

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new SmbizModelModule());
		modules.add(new SmbizEGraphModule());
		modules.add(new SmbizDb4oDaoModule(getConfig()));
		modules.add(new Db4oDbShellModule());
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IEntityTypeResolver.class).toInstance(new SmbizEntityTypeResolver());
			}
		});
	}

	private Marshaler getMarshaler() {
		return injector.getInstance(Marshaler.class);
	}

	private EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	private EntityGraph getEntityGraph() {
		return injector.getInstance(EntityGraph.class);
	}

	private IMarshalOptionsResolver getMarshalOptionsResolver() {
		return injector.getInstance(IMarshalOptionsResolver.class);
	}

	/**
	 * Simple test to verify basic entity marshaling for all system entities.
	 * @throws Exception Upon failure
	 */
	@Test
	public void testAllEntitiesWithNoHierarchy() throws Exception {
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final Class<? extends IEntity>[] entityClasses =
			CommonUtil.getClasses("com.tll.model", IEntity.class, true, null, new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					// we only want the actual smbiz model classes!
					return (dir.getPath().indexOf("smbiz") > 0) && (dir.getPath().indexOf("classes") > 0)
					&& (dir.getPath().indexOf("test") < 0);
				}
			});
		for(final Class<? extends IEntity> entityClass : entityClasses) {
			final IEntity e = getEntityBeanFactory().getEntityCopy(entityClass, false);
			if(e == null) continue;	// skip
			Assert.assertNotNull(e);
			final Model model = marshaler.marshal(e, MarshalOptions.UNCONSTRAINED_MARSHALING);

			assert model.getEntityType() != null : "The marshaled entity model's ref type was found null";

			final ModelKey refKey = model.getKey();
			assert refKey != null : "The marshaled entity model's ref key was found null";
			assert refKey.isSet() : "The marshaled entity model's ref key was found un-set";

			Assert.assertNotNull(model);
			final IEntity e2 = marshaler.marshal(e.entityClass(), model);
			Assert.assertNotNull(e2);
			Assert.assertEquals(e, e2);
		}
	}

	@SuppressWarnings("null")
	public void testAccountMarshaling() throws Exception {
		final Merchant e = getEntityGraph().getEntitiesByType(Merchant.class).iterator().next();
		Assert.assertTrue(e != null && e.getParent() != null && e.getPaymentInfo() != null && e.getPaymentInfo().getPaymentData() != null);

		final MarshalOptions mo = getMarshalOptionsResolver().resolve(SmbizEntityType.MERCHANT);
		final Model m = getMarshaler().marshal(e, mo);

		final Merchant e2 = getMarshaler().marshal(Merchant.class, m);

		Assert.assertNotNull(e2.getParent());
		Assert.assertEquals(e, e2);
		Assert.assertEquals(e.getVersion(), e2.getVersion());
		Assert.assertEquals(e.getVersion(), e.getVersion());
	}
}
