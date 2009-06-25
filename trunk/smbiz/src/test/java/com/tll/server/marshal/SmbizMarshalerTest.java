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
import com.google.inject.Scopes;
import com.tll.AbstractInjectedTest;
import com.tll.common.model.Model;
import com.tll.common.model.ModelKey;
import com.tll.di.MockDaoModule;
import com.tll.di.MockEntityFactoryModule;
import com.tll.di.ModelModule;
import com.tll.model.IEntity;
import com.tll.model.IEntityGraphBuilder;
import com.tll.model.MockEntityFactory;
import com.tll.model.SmbizEntityGraphBuilder;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.SmbizEntityTypeResolver;
import com.tll.util.CommonUtil;

/**
 * SmbizMarshallerTest - Tests the marshaling of the smbiz defined entities.
 * @author jpk
 */
@Test(groups = {
	"server", "client-model" })
	public class SmbizMarshalerTest extends AbstractInjectedTest {

	protected static final Map<String, Object> tupleMap = new HashMap<String, Object>();

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new ModelModule());
		modules.add(new MockEntityFactoryModule());
		modules.add(new MockDaoModule() {

			@Override
			protected void bindEntityGraphBuilder() {
				bind(IEntityGraphBuilder.class).to(SmbizEntityGraphBuilder.class).in(Scopes.SINGLETON);
			}
		});
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

	private MockEntityFactory getMockEntityFactory() {
		return injector.getInstance(MockEntityFactory.class);
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
			final IEntity e = getMockEntityFactory().getEntityCopy(entityClass, false);
			Assert.assertNotNull(e);
			final Model model = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);

			assert model.getEntityType() != null : "The marshaled entity model's ref type was found null";

			final ModelKey refKey = model.getKey();
			assert refKey != null : "The marshaled entity model's ref key was found null";
			assert refKey.isSet() : "The marshaled entity model's ref key was found un-set";

			Assert.assertNotNull(model);
			final IEntity e2 = marshaler.unmarshalEntity(e.entityClass(), model);
			Assert.assertNotNull(e2);
			Assert.assertEquals(e, e2);
		}
	}
}
