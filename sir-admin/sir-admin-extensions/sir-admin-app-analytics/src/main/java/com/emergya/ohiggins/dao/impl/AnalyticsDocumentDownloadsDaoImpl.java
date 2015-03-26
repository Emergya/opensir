package com.emergya.ohiggins.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.AnalyticsDataDownloadsDao;
import com.emergya.ohiggins.model.AnalyticsDataDownloadsEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

@Repository
public class AnalyticsDocumentDownloadsDaoImpl 
	extends GenericHibernateDAOImpl<AnalyticsDataDownloadsEntity, Long>
	implements AnalyticsDataDownloadsDao {

	@Override
	public AnalyticsDataDownloadsEntity getByIdentifier(String identifier) {
		
		Session s = this.getSession();
		Criteria c = s.createCriteria(AnalyticsDataDownloadsEntity.class);
		c.add(Restrictions.eq("identifier", identifier));
		
		return (AnalyticsDataDownloadsEntity) c.uniqueResult();
	}

}
