/**
 * The Logic Lab
 */
package com.tll.server.marshal;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.AbstractInjectedTest;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.Model;
import com.tll.common.model.RefKey;
import com.tll.dao.DaoMode;
import com.tll.dao.mock.EntityGraphBuilder;
import com.tll.di.DaoModule;
import com.tll.model.Account;
import com.tll.model.Asp;
import com.tll.model.CreditCardType;
import com.tll.model.EntityUtil;
import com.tll.model.IEntity;
import com.tll.model.IScalar;
import com.tll.model.PaymentInfo;
import com.tll.model.mock.EntityGraph;
import com.tll.model.mock.MockEntityFactory;
import com.tll.util.CommonUtil;

/**
 * EntityMarshallerTest
 * @author jpk
 */
@Test(groups = "server.rpc")
public class MarshalerTest extends AbstractInjectedTest {

	/**
	 * Constructor
	 */
	public MarshalerTest() {
		super();
	}

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		final DaoModule daoModule = new DaoModule(DaoMode.MOCK);
		modules.add(daoModule);
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
						return dir.getPath().indexOf("smbiz") > 0 && dir.getPath().indexOf("classes") > 0;
					}
				});
		for(final Class<? extends IEntity> entityClass : entityClasses) {
			final IEntity e = getMockEntityFactory().getEntityCopy(entityClass, false);
			Assert.assertNotNull(e);
			final Model model = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);

			assert model.getEntityType() != null : "The marshaled entity model's ref type was found null";
			assert model.getEntityType().equals(EntityUtil.entityTypeFromClass(e.entityClass()).name()) : "The marshaled entity model's ref type did not match the sourcing entities' entity type";
			final RefKey refKey = model.getRefKey();
			assert refKey != null : "The marshaled entity model's ref key was found null";
			assert refKey.isSet() : "The marshaled entity model's ref key was found un-set";

			Assert.assertNotNull(model);
			final IEntity e2 = marshaler.unmarshalEntity(e.entityClass(), model);
			Assert.assertNotNull(e2);
			Assert.assertEquals(e, e2);
		}
	}

	/**
	 * Tests marshaler handling of entity circular referencing.
	 * @throws Exception Upon failure
	 */
	@Test
	public void testCircularEntity() throws Exception {
		final EntityGraphBuilder entityGraphBuilder = new EntityGraphBuilder(getMockEntityFactory());
		EntityGraph entityGraph = entityGraphBuilder.buildEntityGraph();
		final Asp asp = entityGraph.getEntityByType(Asp.class);
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final Model model = marshaler.marshalEntity(asp, MarshalOptions.UNCONSTRAINED_MARSHALING);
		assert model != null;
		final Asp reasp = marshaler.unmarshalEntity(Asp.class, model);
		assert reasp != null;
		assert asp.equals(reasp);
	}

	/**
	 * Tests the special PaymentInfo entity case
	 * @throws Exception Upon failure
	 */
	@Test
	public void testPaymentInfo() throws Exception {
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final IEntity e = getMockEntityFactory().getEntityCopy(PaymentInfo.class, false);
		Assert.assertNotNull(e);

		final Model model = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertNotNull(model);
		final IEntity e2 = marshaler.unmarshalEntity(e.entityClass(), model);
		Assert.assertNotNull(e2);
		Assert.assertEquals(e, e2);
	}

	protected static final Map<String, Object> tupleMap = new HashMap<String, Object>();

	static {
		tupleMap.put("string", "a string");
		tupleMap.put("date", new Date());
		tupleMap.put("integer", new Integer(1));
		tupleMap.put("character", new Character('c'));
		tupleMap.put("enum", CreditCardType.VISA);
		tupleMap.put("boolean", new Boolean(true));
	}

	/**
	 * Tests basic marshaling of IScalar instances.
	 * @throws Exception Upon failure.
	 */
	@Test
	public void testScalarMarshaling() throws Exception {
		final IScalar scalar = new IScalar() {

			public Map<String, Object> getTupleMap() {
				return tupleMap;
			}

			public String getPropertyPath(String propertyName) {
				return null;
			}

			public Class<? extends IEntity> getRefType() {
				return Account.class;
			}
		};

		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final Model model = marshaler.marshalScalar(scalar, MarshalOptions.UNCONSTRAINED_MARSHALING);

		final Iterator<IModelProperty> itr = model.iterator();
		while(itr.hasNext()) {
			final IModelProperty prop = itr.next();
			assert prop != null;
			final Object val = tupleMap.get(prop.getPropertyName());
			assert val != null;
		}

	}

	/**
	 * Test to ensure that a related many property is created in the the
	 * client-bound marshaled construct when there are no actual related many
	 * entities present.
	 * @throws Exception
	 */
	@Test
	public void testEmptyRelatedMany() throws Exception {
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final Account e = getMockEntityFactory().getEntityCopy(Account.class, false);
		assert e != null;
		e.setAddresses(null);
		Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		assert m != null;
		IModelProperty mp = m.get("addresses");
		assert mp != null;
	}
}
