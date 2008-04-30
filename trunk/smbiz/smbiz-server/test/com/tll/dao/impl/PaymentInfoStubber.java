/**
 * The Logic Lab
 * @author jpk
 * Nov 6, 2007
 */
package com.tll.dao.impl;

import java.util.List;

import org.testng.annotations.Test;

import com.google.inject.Module;
import com.tll.DbTest;
import com.tll.dao.DaoMode;
import com.tll.dao.JpaMode;
import com.tll.guice.DaoModule;
import com.tll.model.impl.PaymentInfo;

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
		super(JpaMode.LOCAL);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		modules.add(new DaoModule(DaoMode.HIBERNATE));
	}

	@Test
	public void stub() throws Exception {
		startNewTransaction();
		final PaymentInfo e = getMockEntityProvider().getEntityCopy(PaymentInfo.class);
		final IPaymentInfoDao dao = injector.getInstance(IPaymentInfoDao.class);
		dao.persist(e);
		setComplete();
		endTransaction();
	}
}
