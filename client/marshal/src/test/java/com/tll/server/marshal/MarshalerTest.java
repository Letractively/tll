/**
 * The Logic Lab
 */
package com.tll.server.marshal;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.Model;
import com.tll.dao.AbstractDbAwareTest;
import com.tll.di.Db4oDaoModule;
import com.tll.di.Db4oDbShellModule;
import com.tll.di.TestPersistenceUnitModelModule;
import com.tll.model.Account;
import com.tll.model.EntityBeanFactory;
import com.tll.model.EntityGraph;
import com.tll.model.FieldEnum;
import com.tll.model.IEntity;
import com.tll.model.IScalar;
import com.tll.model.NestedEntity;
import com.tll.model.TestPersistenceUnitEntityGraphBuilder;
import com.tll.server.rpc.entity.IEntityTypeResolver;
import com.tll.server.rpc.entity.test.TestEntityTypeResolver;

/**
 * MarshallerTest
 * @author jpk
 */
@Test(groups = { "server", "client-marshal" })
public class MarshalerTest extends AbstractDbAwareTest {

	protected static final Map<String, Object> tupleMap = new HashMap<String, Object>();

	static {
		tupleMap.put("string", "a string");
		tupleMap.put("date", new Date());
		tupleMap.put("integer", new Integer(1));
		tupleMap.put("character", new Character('c'));
		tupleMap.put("enum", FieldEnum.OPEN);
		tupleMap.put("boolean", new Boolean(true));
	}

	@BeforeClass(alwaysRun = true)
	public final void onBeforeClass() {
		beforeClass();
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new TestPersistenceUnitModelModule());
		modules.add(new Db4oDbShellModule());
		modules.add(new Db4oDaoModule(getConfig()));
		modules.add(new Module() {

			@Override
			public void configure(Binder binder) {
				binder.bind(IEntityTypeResolver.class).to(TestEntityTypeResolver.class).in(Scopes.SINGLETON);
			}
		});
	}

	private Marshaler getMarshaler() {
		return injector.getInstance(Marshaler.class);
	}

	private EntityBeanFactory getEntityBeanFactory() {
		return injector.getInstance(EntityBeanFactory.class);
	}

	/**
	 * Tests marshaler handling of entity circular referencing.
	 * @throws Exception Upon failure
	 */
	@Test
	public void testCircularEntity() throws Exception {
		final TestPersistenceUnitEntityGraphBuilder entityGraphBuilder =
			new TestPersistenceUnitEntityGraphBuilder(getEntityBeanFactory());
		final EntityGraph entityGraph = new EntityGraph();
		entityGraphBuilder.setEntityGraph(entityGraph);
		entityGraphBuilder.populateEntityGraph();
		final Marshaler marshaler = getMarshaler();

		// wire up a circular entity
		final Collection<Account> accounts = entityGraph.getEntitiesByType(Account.class);
		// NOTE: we expect 3 of them per test-persistence-unit jar
		final Account account = accounts.iterator().next();
		final Account parentAccount = accounts.iterator().next();
		account.setParent(parentAccount);

		final Model model = marshaler.marshalEntity(account, MarshalOptions.UNCONSTRAINED_MARSHALING);
		assert model != null;
		final Account unmarshaled = marshaler.unmarshalEntity(Account.class, model);
		assert unmarshaled != null;
		assert account.equals(unmarshaled);
	}

	/**
	 * Tests the marshaling of a nested entity.
	 * @throws Exception Upon failure
	 */
	@Test
	public void testNestedEntity() throws Exception {
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final IEntity e = getEntityBeanFactory().getEntityCopy(NestedEntity.class, false);
		Assert.assertNotNull(e);

		final Model model = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertNotNull(model);
		final IEntity e2 = marshaler.unmarshalEntity(e.entityClass(), model);
		Assert.assertNotNull(e2);
		Assert.assertEquals(e, e2);
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
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		assert e != null;
		e.setAddresses(null);
		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		assert m != null;
		final IModelProperty mp = m.get("addresses");
		assert mp != null;
	}

	@Test
	public void testEmptyNested() throws Exception {
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		final NestedEntity n = getEntityBeanFactory().getEntityCopy(NestedEntity.class, false);
		assert e != null && n != null;
		e.setNestedEntity(n);
		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		assert m != null;
		final IModelProperty mp = m.get("nestedEntity");
		assert mp != null;
	}

	@Test
	public void testEmptyNestedTarget() throws Exception {
		final Marshaler marshaler = getMarshaler();
		assert marshaler != null;
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		final NestedEntity n = getEntityBeanFactory().getEntityCopy(NestedEntity.class, false);
		assert e != null && n != null;
		e.setNestedEntity(n);
		e.getNestedEntity().setNestedData(null);
		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		assert m != null;
		final IModelProperty mp = m.get("nestedEntity");
		assert mp != null;
	}
}
