/**
 * 
 */
package com.tll.dao.impl.hibernate;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.criterion.Criterion;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tll.criteria.Criteria;
import com.tll.criteria.IComparatorTranslator;
import com.tll.criteria.InvalidCriteriaException;
import com.tll.dao.IDbDialectHandler;
import com.tll.dao.hibernate.TimeStampEntityDao;
import com.tll.dao.impl.IInterfaceDao;
import com.tll.model.impl.Interface;
import com.tll.model.impl.InterfaceOption;
import com.tll.model.impl.InterfaceOptionParameterDefinition;
import com.tll.model.key.INameKey;

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

	public Interface load(INameKey<? extends Interface> nameKey) {
		try {
			final Criteria<Interface> nc = new Criteria<Interface>(Interface.class);
			nc.getPrimaryGroup().addCriterion(nameKey, false);
			return findEntity(nc);
		}
		catch(final InvalidCriteriaException e) {
			throw new PersistenceException("Unable to load entity from name key: " + e.getMessage(), e);
		}
	}

}
