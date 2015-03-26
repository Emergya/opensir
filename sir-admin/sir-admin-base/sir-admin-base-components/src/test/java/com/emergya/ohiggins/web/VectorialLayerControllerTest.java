package com.emergya.ohiggins.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.emergya.persistenceGeo.utils.GeometryType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ohiggins-testApplicationContext.xml" })
@TransactionConfiguration(defaultRollback = false, transactionManager = "transactionManager")
//@Transactional 
public class VectorialLayerControllerTest {

	@Resource
	private VectorialLayerController controller;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateTempLayer() throws JsonProcessingException,
			IOException {
		String layerTitle = "Test layer name";
		GeometryType gt = GeometryType.POINT;
		String response = controller.createTempLayer(layerTitle, gt);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response);
		assertEquals("Se esperaba true en el parámetro success del JSON", true,
				root.path("success").asBoolean());
		JsonNode dataNode = root.path("data");
		assertEquals("Se produjo un error al crear la capa", "success",
				dataNode.path("status").asText());
		assertNotNull("layerName no puede ser null", dataNode.path("layerName").asText());
		assertEquals(layerTitle, dataNode.path("layerTitle").asText());
		

	}

}
