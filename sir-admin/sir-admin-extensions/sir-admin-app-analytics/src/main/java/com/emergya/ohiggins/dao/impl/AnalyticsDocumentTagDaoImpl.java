package com.emergya.ohiggins.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.AnalyticsDataTagDao;
import com.emergya.ohiggins.dto.AnalyticsDataTagCountDto;
import com.emergya.ohiggins.model.AnalyticsDataTagEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

@Repository
public class AnalyticsDocumentTagDaoImpl 
	extends GenericHibernateDAOImpl<AnalyticsDataTagEntity, Long>
	implements AnalyticsDataTagDao {

	
	@Override
	public List<String> searchTags(String searchTerm) {
		Criteria c = this.getSession().createCriteria(AnalyticsDataTagEntity.class);
		c.add(Restrictions.like("tagName", searchTerm, MatchMode.ANYWHERE));
		c.setProjection(Projections.groupProperty("tagName").as("name"));
		
		c.addOrder(Order.asc("name"));
		
		@SuppressWarnings("unchecked")
		List<String> result =  c.list();
		return result;
	}

	@Override
	public List<AnalyticsDataTagCountDto> getTagCounts() {
		
		Criteria c = this.getSession().createCriteria(AnalyticsDataTagEntity.class);
		c.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("tagName"))
				.add(Projections.count("tagName").as("count")));
		c.addOrder(Order.desc("count"));
		c.addOrder(Order.asc("tagName"));
		c.setMaxResults(10);
		
		
		List<AnalyticsDataTagCountDto> results = new LinkedList<AnalyticsDataTagCountDto>();		
		@SuppressWarnings("unchecked")
		List<Object[]> searchResult = c.list();
		
		for(Object[] result : searchResult) {
			results.add(new AnalyticsDataTagCountDto((String)result[0], (Long)result[1]));
		}
		
		return results;
	}

}
