package com.emergya.chileIndica.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.chileIndica.dao.ChileIndicaInstitucionEntityDao;
import com.emergya.chileIndica.model.ChileIndicaInstitucionEntity;
import com.emergya.ohiggins.model.AuthorityTypeEntity;

@Repository
public class ChileIndicaInstitucionEntityDaoImpl extends
		ChileIndicaDatabaseGenericHibernateDAOImpl<ChileIndicaInstitucionEntity, Long> implements
		ChileIndicaInstitucionEntityDao {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ChileIndicaInstitucionEntity> getAllAuthorityByRegion(
			Long codRegion) {
		setChileIndicaRegionsDatabaseContextHolder(codRegion);
		Criteria criteria = getSession().createCriteria(
				ChileIndicaInstitucionEntity.class);
		criteria.addOrder(Order.asc("cInstitucion"));
		return criteria.list();
	}

}
