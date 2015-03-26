package com.emergya.ohiggins.dao.impl;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.OhigginsLayerResourceDao;
import com.emergya.persistenceGeo.dao.DBManagementDao;
import com.emergya.persistenceGeo.utils.GeometryType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ohiggins-testModelContext.xml" })
@TransactionConfiguration(defaultRollback = true, transactionManager = "transactionManager")
@Transactional
public class OhigginsLayerResourceDaoHibernateImplTest {

	@Resource
	OhigginsLayerResourceDao layerResourceDao;
	
	@Resource
	DBManagementDao dbManagementDao;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateLayerTable() {
		for (GeometryType t : GeometryType.values()) {
			String tableName = layerResourceDao.generateNameNotYetUsed();
			try {
				dbManagementDao.createLayerTable(tableName, 4326, t);
				dbManagementDao.deleteLayerTable(tableName);
			} catch (Exception e) {
				fail("No se ha podido crear la tabla de " + t.toString() + ": "
						+ e.getMessage());
			}
		}

	}
	
	@Test 
	public void testDuplicateLayerTable() {
		String destinationTable = layerResourceDao.generateNameNotYetUsed();
		try{
			dbManagementDao.duplicateLayerTable("comuna_2002", destinationTable);
		} catch(Exception e) {
			fail("No se ha podido duplicar la tabla: "+ e.getMessage());
		}
	}

}
