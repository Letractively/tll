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

import com.google.inject.Module;
import com.tll.AbstractConfigAwareTest;
import com.tll.common.model.IModelProperty;
import com.tll.common.model.Model;
import com.tll.di.TestMarshalModule;
import com.tll.di.test.TestPersistenceUnitModelModule;
import com.tll.model.IEntity;
import com.tll.model.IScalar;
import com.tll.model.test.Account;
import com.tll.model.test.AccountAddress;
import com.tll.model.test.AccountStatus;
import com.tll.model.test.EntityBeanFactory;
import com.tll.model.test.EntityGraph;
import com.tll.model.test.NestedEntity;
import com.tll.model.test.TestPersistenceUnitEntityGraphBuilder;

/**
 * MarshallerTest
 * @author jpk
 */
@Test(groups = { "server", "client-marshal" })
@SuppressWarnings("null")
public class MarshalerTest extends AbstractConfigAwareTest {

	protected static final Map<String, Object> tupleMap = new HashMap<String, Object>();

	static {
		tupleMap.put("string", "a string");
		tupleMap.put("date", new Date());
		tupleMap.put("integer", new Integer(1));
		tupleMap.put("character", new Character('c'));
		tupleMap.put("enum", AccountStatus.OPEN);
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
		modules.add(new TestMarshalModule());
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
		final Iterator<Account> aitr = accounts.iterator();
		final Account account = aitr.next(), parent = aitr.next();
		account.setParent(parent);
		parent.setParent(account);

		final Model model = marshaler.marshalEntity(account, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertTrue(model != null);
		final Account unmarshaled = marshaler.marshalModel(model, Account.class);
		Assert.assertTrue(unmarshaled != null);
		Assert.assertEquals(account, unmarshaled);
	}

	/**
	 * Tests the marshaling of a nested entity.
	 * @throws Exception Upon failure
	 */
	public void testNestedEntity() throws Exception {
		final Marshaler marshaler = getMarshaler();
		Assert.assertTrue(marshaler != null);
		final IEntity e = getEntityBeanFactory().getEntityCopy(NestedEntity.class, false);
		Assert.assertNotNull(e);

		final Model model = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertNotNull(model);
		final IEntity e2 = marshaler.marshalModel(model, e.entityClass());
		Assert.assertNotNull(e2);
		Assert.assertEquals(e, e2);
	}

	/**
	 * Tests basic marshaling of IScalar instances.
	 * @throws Exception Upon failure.
	 */
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
		Assert.assertTrue(marshaler != null);
		final Model model = marshaler.marshalScalar(scalar, MarshalOptions.UNCONSTRAINED_MARSHALING);

		final Iterator<IModelProperty> itr = model.iterator();
		while(itr.hasNext()) {
			final IModelProperty prop = itr.next();
			Assert.assertTrue(prop != null);
			final Object val = tupleMap.get(prop.getPropertyName());
			Assert.assertTrue(val != null);
		}

	}

	/**
	 * Test to ensure that a related many property is created in the the
	 * client-bound marshaled construct when there are no actual related many
	 * entities present.
	 * @throws Exception
	 */
	public void testEmptyRelatedMany() throws Exception {
		final Marshaler marshaler = getMarshaler();
		Assert.assertTrue(marshaler != null);
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		Assert.assertTrue(e != null);
		e.setAddresses(null);
		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertTrue(m != null);
		final IModelProperty mp = m.get("addresses");
		Assert.assertTrue(mp != null);
	}

	public void testEmptyNested() throws Exception {
		final Marshaler marshaler = getMarshaler();
		Assert.assertTrue(marshaler != null);
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		final NestedEntity n = getEntityBeanFactory().getEntityCopy(NestedEntity.class, false);
		Assert.assertTrue(e != null && n != null);
		e.setNestedEntity(n);
		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertTrue(m != null);
		final IModelProperty mp = m.get("nestedEntity");
		Assert.assertTrue(mp != null);
	}

	public void testEmptyNestedTarget() throws Exception {
		final Marshaler marshaler = getMarshaler();
		Assert.assertTrue(marshaler != null);
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		final NestedEntity n = getEntityBeanFactory().getEntityCopy(NestedEntity.class, false);
		Assert.assertTrue(e != null && n != null);
		e.setNestedEntity(n);
		e.getNestedEntity().setNestedData(null);
		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		Assert.assertTrue(m != null);
		final IModelProperty mp = m.get("nestedEntity");
		Assert.assertTrue(mp != null);
	}

	public void testUnmarshalAgainstExistingEntity() throws Exception {
		final Marshaler marshaler = getMarshaler();
		Assert.assertTrue(marshaler != null);
		final Account e = getEntityBeanFactory().getEntityCopy(Account.class, false);
		final AccountAddress aa1 = getEntityBeanFactory().getEntityCopy(AccountAddress.class, false);
		final AccountAddress aa2 = getEntityBeanFactory().getEntityCopy(AccountAddress.class, true);
		e.addAccountAddress(aa1);
		e.addAccountAddress(aa2);

		final Model m = marshaler.marshalEntity(e, MarshalOptions.UNCONSTRAINED_MARSHALING);
		m.indexed("addresses[0]").getModel().setMarkedDeleted(true);

		final Account rea = marshaler.marshalModel(m, e);
		Assert.assertTrue(e == rea);
		Assert.assertTrue(e.equals(rea));
		Assert.assertTrue(e.getAddresses() != null);
		Assert.assertTrue(e.getAddresses().size() == 1);
		Assert.assertTrue(e.getAddresses().iterator().next().equals(aa2));
	}
}
