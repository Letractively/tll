package com.tll;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.tll.dao.IDaoFactory;
import com.tll.dao.jdbc.DbShell;
import com.tll.di.MockEntitiesModule;
import com.tll.di.DbShellModule.TestDb;
import com.tll.listhandler.SortColumn;
import com.tll.listhandler.Sorting;
import com.tll.model.BusinessKeyNotDefinedException;
import com.tll.model.EntityAssembler;
import com.tll.model.IEntity;
import com.tll.model.MockEntityProvider;
import com.tll.model.impl.Authority;
import com.tll.model.impl.User;
import com.tll.model.key.BusinessKey;
import com.tll.server.rpc.Marshaler;

/**
 * Abstract base class for all test cases within the application.
 * @author jpk
 */
public abstract class TestBase {

	protected static final Log staticLogger = LogFactory.getLog(TestBase.class);

	protected final Log logger = LogFactory.getLog(this.getClass());

	protected static final String LENGTH_64_STRING = "1234567890123456789012345678901234567890123456789012345678901234";

	protected static final String LENGTH_65_STRING = LENGTH_64_STRING + "5";

	protected static final String LENGTH_128_STRING = LENGTH_64_STRING + LENGTH_64_STRING;

	protected static final String LENGTH_129_STRING = LENGTH_64_STRING + LENGTH_65_STRING;

	protected static final String LENGTH_256_STRING = LENGTH_128_STRING + LENGTH_128_STRING;

	protected static final String LENGTH_257_STRING = LENGTH_128_STRING + LENGTH_129_STRING;

	protected Sorting simpleIdSorting = new Sorting(new SortColumn(IEntity.PK_FIELDNAME));

	protected Injector injector;

	/**
	 * Constructor
	 */
	public TestBase() {
		super();
	}

	/**
	 * Builds the Guice injector for this test calling on {@link #getModules()}.
	 */
	protected final void buildInjector() {
		assert injector == null : "The injector was already built";
		final List<Module> modules = getModules();
		if(modules != null && modules.size() > 0) {
			this.injector = buildStaticInjector(modules.toArray(new Module[modules.size()]));
		}
	}

	/**
	 * Builds a Guice Injector from one or more {@link Module}s.
	 * @param modules The {@link Module}s to bind
	 * @return A new {@link Injector}
	 */
	protected static final Injector buildStaticInjector(Module... modules) {
		assert modules != null && modules.length > 0;
		return Guice.createInjector(Stage.DEVELOPMENT, modules);
	}

	/**
	 * @return List of {@link Module}s for available for the derived tests.
	 */
	protected final List<Module> getModules() {
		final List<Module> list = new ArrayList<Module>();
		addModules(list);
		return list;
	}

	/**
	 * Sub-classes should override this method to add the necessary Guice!!
	 * modules.
	 * @param modules
	 */
	protected void addModules(List<Module> modules) {
		modules.add(new MockEntitiesModule());
	}

	/**
	 * Before class hook.
	 */
	protected void beforeClass() {
		buildInjector();
	}

	/**
	 * After class hook.
	 */
	protected void afterClass() {
		// no-op
	}

	/**
	 * Before method hook
	 */
	protected void beforeMethod() {
		// no-op
	}

	/**
	 * After method hook.
	 */
	protected void afterMethod() {
		// no-op
	}

	/**
	 * <strong>NOTE: </strong>The {@link IDaoFactory} is should only be used for
	 * testing purposes. It must be added via {@link #addModules(List)}.
	 * @return The injected {@link IDaoFactory}
	 */
	protected final IDaoFactory getDaoFactory() {
		return injector.getInstance(IDaoFactory.class);
	}

	/**
	 * <strong>NOTE: </strong>The {@link MockEntityProvider} is not available by
	 * default. It must be added via {@link #addModules(List)}.
	 * @return The injected {@link MockEntityProvider}
	 */
	protected final MockEntityProvider getMockEntityProvider() {
		return injector.getInstance(MockEntityProvider.class);
	}

	/**
	 * <strong>NOTE: </strong>The {@link EntityAssembler} is not available by
	 * default. It must be added via {@link #addModules(List)}.
	 * @return The injected {@link EntityAssembler}
	 */
	protected final EntityAssembler getEntityAssembler() {
		return injector.getInstance(EntityAssembler.class);
	}

	/**
	 * <strong>NOTE: </strong>The {@link Marshaler} is not available by default.
	 * It must be added via {@link #addModules(List)}.
	 * @return The injected {@link Marshaler}
	 */
	protected final Marshaler getMarshaler() {
		return injector.getInstance(Marshaler.class);
	}

	/**
	 * <strong>NOTE: </strong>The {@link DbShell} is not available by default. It
	 * must be added via {@link #addModules(List)}.
	 * @return The injected {@link DbShell}
	 */
	protected final DbShell getDbShell() {
		return injector.getInstance(Key.get(DbShell.class, TestDb.class));
	}

	/**
	 * Compare a clc of entity ids and entites ensuring the id list is referenced
	 * w/in the entity list
	 * @param ids
	 * @param entities
	 * @return
	 */
	protected static final <E extends IEntity> boolean entitiesAndIdsEquals(Collection<Integer> ids,
			Collection<E> entities) {
		if(ids == null || entities == null) {
			return false;
		}
		if(ids.size() != entities.size()) {
			return false;
		}
		for(final E e : entities) {
			boolean found = false;
			for(final Integer id : ids) {
				if(id.equals(e.getId())) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate the given object is empty. Handles {@link Collection}s and arrays.
	 * @param obj
	 * @throws Exception When the given object is found not empty
	 */
	protected static void validateEmpty(Object obj) throws Exception {
		if(obj == null) return;
		if(obj instanceof Collection) {
			if(((Collection<?>) obj).size() > 0) {
				throw new Exception("Non-empty collection");
			}
		}
		else if(obj.getClass().isArray()) {
			final int length = Array.getLength(obj);
			if(length > 0) {
				throw new Exception("Non-empty array");
			}
		}
	}

	/**
	 * Validate the 2 objects given are equal by class type
	 * @param src
	 * @param tgt
	 * @throws Exception When not of like type
	 */
	protected static void validateEqualTypes(Object src, Object tgt) throws Exception {
		if((src == null && tgt != null) || (src != null && tgt == null)) {
			throw new Exception("Types differ: one is null the other is not");
		}
		if(src != null && !tgt.getClass().equals(src.getClass()))
			throw new Exception("Types differ: src type: " + src.getClass().toString() + ", cpyValue: "
					+ tgt.getClass().toString());
	}

	/**
	 * Validate the 2 objects given are equal. When the given objects are
	 * <code>null</code>, this validation passes.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	protected static void validateEquals(Object src, Object tgt) throws Exception {
		if(!ObjectUtils.equals(src, tgt))
			throw new Exception("Objects do not equal: src: " + (src == null ? "null" : src.toString()) + ", tgt: "
					+ (tgt == null ? "null" : tgt.toString()));
	}

	/**
	 * Validate the 2 objects given are NOTE equal by memory address. This
	 * validation passes when the given objects are <code>null</code>.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	protected static void validateNotEqualByMemoryAddress(Object src, Object tgt) throws Exception {
		if(src != null && src == tgt)
			throw new Exception("Objects are equal by memory address: src: " + src.toString() + ", tgt: " + tgt.toString());
	}

	/**
	 * Validate the 2 objects given are locically equal AND NOT by memory address.
	 * @param src
	 * @param tgt
	 * @throws Exception When the given objects are found unequal
	 */
	protected static void validateEqualsAndNotAddressEquals(Object src, Object tgt) throws Exception {
		if(src != null && src == tgt) {
			throw new Exception("Objects are equal by memory address: src: " + src.toString() + ", tgt: " + tgt.toString());
		}
		validateEquals(src, tgt);
	}

	/**
	 * Makes the given entity unique based on the defined {@link BusinessKey}s for
	 * type of the given entity.
	 * @param e The entity to uniquify
	 * @param n The integer value serving as a way to uniquify individual fields
	 *        that comprise a target {@link BusinessKey}.
	 */
	protected final static <ET extends IEntity> void makeUnique(ET e) {
		try {
			MockEntityProvider.makeBusinessKeyUnique(e);
		}
		catch(final BusinessKeyNotDefinedException e1) {
			// ok
		}
	}

	/**
	 * Establishes a valid {@link SecurityContext} within the app context.
	 * Necessary for tests that are related to or dependant on Acegi.
	 */
	protected final void setSecurityContext() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ROLE_ADMINISTRATOR);
		final AnonymousAuthenticationToken token =
				new AnonymousAuthenticationToken("bfgsdf", User.SUPERUSER, new GrantedAuthority[] { auth });
		securityContext.setAuthentication(token);
	}
}
