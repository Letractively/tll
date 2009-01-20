/**
 * 
 */
package com.tll.dao.impl.hibernate;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.IComparatorTranslator;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IInterfaceDao;
import com.tll.model.Interface;
import com.tll.model.InterfaceOption;
import com.tll.model.InterfaceOptionParameterDefinition;
import com.tll.model.key.NameKey;

/**
 * InterfaceDao
 * @author jpk
 */
public class InterfaceDao extends TimeStampEntityDao<Interface> implements IInterfaceDao {

	/**
	 * Constructor
	 * @param emPrvdr
	 * @param dbDialectHandler
	 * @param comparatorTranslator
	 */
	@Inject
	public InterfaceDao(Provider<EntityManager> emPrvdr, IDbDialectHandler dbDialectHandler,
			IComparatorTranslator<Criterion> comparatorTranslator) {
		super(emPrvdr, dbDialectHandler, comparatorTranslator);
	}

	@Override
	public Class<Interface> getEntityClass() {
		return Interface.class;
	}

	@Override
	protected void propagateTimestamping(Interface e, boolean isCreate, Date date) {
		// apply to options and parameters
		final Collection<InterfaceOption> options = e.getOptions();
		if(options != null && options.size() > 0) {
			for(final InterfaceOption o : options) {
				setTimestamping(o, isCreate, date);
				final Collection<InterfaceOptionParameterDefinition> params = o.getParameters();
				if(params != null && params.size() > 0) {
					for(final InterfaceOptionParameterDefinition p : params) {
						setTimestamping(p, isCreate, date);
					}
				}
			}
		}
	}

	@Override
	public Interface load(NameKey<? extends Interface> nameKey) {
		return (Interface) loadByName(nameKey);
	}
}
