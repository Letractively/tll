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
import com.tll.di.OrmDaoModule;
import com.tll.model.MockEntityFactory;
import com.tll.model.PaymentInfo;

/**
 * PaymentInfoStubber
 * @author jpk
 */
@Test
public class PaymentInfoStubber extends AbstractDbTest {

	/**
	 * Constructor
	 */
	public PaymentInfoStubber() {
		super(DaoMode.ORM, false, true);
	}

	@Override
	protected void addModules(List<Module> modules) {
		super.addModules(modules);
		//Config.instance().setProperty(DaoModule.ConfigKeys.DAO_MODE_PARAM.getKey(), DaoMode.ORM.toString());
		modules.add(new OrmDaoModule());
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