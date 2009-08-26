/**
 * The Logic Lab
 * @author jpk
 * Nov 6, 2007
 */
package com.tll;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.config.Config;
import com.tll.dao.IEntityDao;
import com.tll.di.JdoDaoModule;
import com.tll.model.EntityBeanFactory;
import com.tll.model.PaymentInfo;

/**
 * PaymentInfoStubber
 * @author jpk
 */
@Test
public class PaymentInfoStubber extends AbstractDbAwareTest {

	private final Config config;

	/**
	 * Constructor
	 */
	public PaymentInfoStubber() {
		super();
		this.config = Config.load();
	}

	@Override
	protected void addModules(List<Module> modules) {
		modules.add(new JdoDaoModule(config));
	}

	@Test
	public void stub() throws Exception {
		final DbTestSupport dbSupport = getDbSupport();
		dbSupport.startNewTransaction();
		final EntityBeanFactory mep = injector.getInstance(EntityBeanFactory.class);
		final PaymentInfo e = mep.getEntityCopy(PaymentInfo.class, false);
		final IEntityDao dao = injector.getInstance(IEntityDao.class);
		dao.persist(e);
		dbSupport.setComplete();
		dbSupport.endTransaction();
	}
}
