/**
 * The Logic Lab
 * @author jpk
 * Nov 6, 2007
 */
package com.tll;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.dao.DaoMode;
import com.tll.dao.IEntityDao;
import com.tll.dao.JpaMode;
import com.tll.di.DaoModule;
import com.tll.model.PaymentInfo;
import com.tll.model.mock.MockEntityFactory;

/**
 * PaymentInfoStubber
 * @author jpk
 */
@Test
public class PaymentInfoStubber extends DbTest {

	/**
	 * Constructor
	 */
	public PaymentInfoStubber() {
		super(JpaMode.LOCAL, false);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.ORM));
	}

	@Test
	public void stub() throws Exception {
		startNewTransaction();
		final MockEntityFactory mep = injector.getInstance(MockEntityFactory.class);
		final PaymentInfo e = mep.getEntityCopy(PaymentInfo.class, false);
		final IEntityDao dao = injector.getInstance(IEntityDao.class);
		dao.persist(e);
		setComplete();
		endTransaction();
	}
}
