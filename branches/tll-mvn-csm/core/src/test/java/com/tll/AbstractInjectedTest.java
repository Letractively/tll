package com.tll;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * Abstract base class for all test cases within the application.
 * @author jpk
 */
public abstract class AbstractInjectedTest {

	protected static final Log staticLogger = LogFactory.getLog(AbstractInjectedTest.class);

	/**
	 * Builds a Guice Injector from one or more {@link Module}s.
	 * @param modules The {@link Module}s to bind
	 * @return A new {@link Injector}
	 */
	protected static final Injector buildStaticInjector(Module... modules) {
		assert modules != null && modules.length > 0;
		return Guice.createInjector(Stage.DEVELOPMENT, modules);
	}

	protected final Log logger = LogFactory.getLog(this.getClass());

	protected Injector injector;

	/**
	 * Constructor
	 */
	public AbstractInjectedTest() {
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
	 * @return List of {@link Module}s for available for the derived tests.
	 */
	private List<Module> getModules() {
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
		// modules.add(new MockEntitiesModule());
		// base impl no-op
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
	 * Establishes a valid {@link SecurityContext} within the app context.
	 * Necessary for tests that are related to or dependant on Acegi.
	 */
	/*
	protected final void setSecurityContext() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ROLE_ADMINISTRATOR);
		final AnonymousAuthenticationToken token =
				new AnonymousAuthenticationToken("bfgsdf", User.SUPERUSER, new GrantedAuthority[] { auth });
		securityContext.setAuthentication(token);
	}
	*/
}
