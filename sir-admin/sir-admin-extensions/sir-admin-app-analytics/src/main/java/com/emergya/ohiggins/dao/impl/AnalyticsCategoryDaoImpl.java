package com.emergya.ohiggins.dao.impl;

import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.AnalyticsCategoryDao;
import com.emergya.ohiggins.model.AnalyticsCategoryEntity;
import com.emergya.persistenceGeo.dao.impl.GenericHibernateDAOImpl;

/**
 * An hibernate implementation for a dao class that handles the analytics' 
 * module categories.
 * 
 * @author lroman
 *
 */
@Repository
public class AnalyticsCategoryDaoImpl 
	extends GenericHibernateDAOImpl<AnalyticsCategoryEntity, Long>
	implements AnalyticsCategoryDao {

}
