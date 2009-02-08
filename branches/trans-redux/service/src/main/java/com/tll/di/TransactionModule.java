/**
 * The Logic Lab
 * @author jpk
 * Feb 7, 2009
 */
package com.tll.di;

import javax.transaction.UserTransaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;
import org.springframework.transaction.jta.JtaTransactionManager;


/**
 * TransactionModule
 * @author jpk
 */
public final class TransactionModule extends GModule {

	@Override
	protected void configure() {
		final UserTransaction userTransaction = new com.atomikos.icatch.jta.UserTransactionImp();

		// use the jta strategy for the PlatformTransactionManager impl
		final JtaTransactionManager ptm = new JtaTransactionManager(userTransaction);
		ptm.afterPropertiesSet(); // initialize it

		// required for AspectJ weaving of Spring's @Transactional annotation
		// (must be invoked PRIOR to an @Transactional method call
		// TODO try and declare this elsewhere as this is service layer specific
		AnnotationTransactionAspect.aspectOf().setTransactionManager(ptm);

		// PlatformTransactionManager
		bind(PlatformTransactionManager.class).toInstance(ptm);
	}
}
