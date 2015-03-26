/*
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 * Authors:: María Arias de Reyna (mailto:delawen@gmail.com)
 */

package com.emergya.ohiggins.cmis;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.commons.io.IOUtils;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;

import com.emergya.ohiggins.cmis.bean.ADocument;
import com.emergya.ohiggins.cmis.bean.ADocumentFilter;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.criteria.AndCriteria;
import com.emergya.ohiggins.cmis.criteria.Criteria;
import com.emergya.ohiggins.cmis.criteria.EqualsCriteria;
import com.emergya.ohiggins.cmis.criteria.InFolderCriteria;
import com.emergya.ohiggins.cmis.criteria.InValuesCriteria;
import com.emergya.ohiggins.cmis.criteria.JoinCriteria;
import com.emergya.ohiggins.cmis.criteria.NotCriteria;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CMISConnectorImpl<DOCUMENT extends ADocument, FILTER extends ADocumentFilter> 
	implements CMISConnector<DOCUMENT, FILTER> {

	private Random r = new Random();
	private static final int RETRASO = 1000;

	Logger log = LoggerFactory.getLogger(this.getClass());

	private static final int MAX_NUM_INTENTOS = 20;

	private String folder;
	private String store;
	private String url;
	private String user;
	private String password;
	private Session session = null;
	private final Set<String> propertyFilter;

	public CMISConnectorImpl(String url, String user, String password,
			String folder, String store) {
		this();
		this.url = url;
		this.user = user;
		this.password = password;
		this.folder = folder;
		this.store = store;

	}

	public CMISConnectorImpl() {
		Set<String> tempPropertyFilter = Sets.newHashSet();

		this.propertyFilter = Collections.unmodifiableSet(tempPropertyFilter);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * @param folder
	 *            the folder to set
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}

	/**
	 * @param store
	 *            the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}

        @Override
	public List<DOCUMENT> getPagedUserRequests(String u, Long from, Integer max,
			String colOrder, ASortOrder ascDesc) {
		List<DOCUMENT> res = new LinkedList<DOCUMENT>();

		if (from == null || from < 0)
			from = 0l;

		if (max == null || max <= 0)
			max = Integer.MAX_VALUE;

		ItemIterable<QueryResult> query_ = getSolicitudesQuery(u, colOrder,
				ascDesc, max);

		ItemIterable<QueryResult> queryResult = query_// .getPage(((int)
														// from.longValue())/max);
				.skipTo(from).getPage();
		
		Class<DOCUMENT> documentClass = getDocumentClass();

		for (QueryResult item : queryResult) {
			DOCUMENT document = createModelDocument(item, documentClass);
			res.add(document);
		}

		return res;
	}
	
	private Class<DOCUMENT> getDocumentClass() {
		 return ((Class<DOCUMENT>)((ParameterizedType)this.getClass().
			       getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	private DOCUMENT createDocumentInstance(Class<DOCUMENT> documentClass) {
		
		if(documentClass ==null) {
			documentClass = getDocumentClass();
		}
		
		try {
			return documentClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

        @Override
	public ObjectId createDocument(DOCUMENT document, File f) {
		byte[] buf = new byte[0];
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(f);
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int length;
			while ((length = is.read(buffer)) != -1) {
				baos.write(buffer, 0, length);
			}
			buf = baos.toByteArray();

		} catch (FileNotFoundException e) {
			log.error("Not found the Publicacion file", e);
		} catch (IOException e) {
			log.error("Error reading the Publicacion file", e);
		} finally {
			if (baos != null)
				try {
					baos.close();
				} catch (IOException e) {
					log.error("Error cerrando ByteArrayOutputStream", e);
				}
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					log.error("Error cerrando inputStream", e);
				}
		}

		String mimeType = getMimeType(f);

		ByteArrayInputStream input = new ByteArrayInputStream(buf);
		ContentStream contentStream = getSession().getObjectFactory()
				.createContentStream(document.getName(), buf.length,
						mimeType, input);

		Map<String, Object> properties = document.toAlfrescoProperties();
                
                if (document.getIdentifier() == null) {
			document.setIdentifier(System.currentTimeMillis() + "_OHG_"
					+ r.nextInt());
		}

		log.trace(String.format("New document: '%s' with name '%s'",
                        document.getIdentifier(),
                        document.getName()));
                        
                
		properties.put(PropertyIds.NAME, document.getIdentifier());

		properties.put(PropertyIds.OBJECT_TYPE_ID, 
                        "cmis:document,"+ document.getDocumentType());

		Folder alfrescofolder = (Folder) getSession().getObject(this.folder);

		ObjectId id = alfrescofolder.createDocument(properties, contentStream,
				VersioningState.MAJOR);

		try {
			input.close();
		} catch (IOException e) {
			log.error("Error cerrando stream de fichero", e);
		}

		return id;
	}

	private String getMimeType(File f) {
		String mimeType = URLConnection.guessContentTypeFromName(f.getName());

		Detector[] detectors = new Detector[] { new DefaultDetector() };

		Metadata metadata = new Metadata();
		metadata.add(TikaMetadataKeys.RESOURCE_NAME_KEY, f.getName());
		for (Detector d : detectors) {
			if (mimeType == null || mimeType.equals("")
					|| mimeType.equals("application/octet-stream")) {
				FileInputStream istream = null;
				BufferedInputStream bistream = null;
				try {
					istream = new FileInputStream(f);
					bistream = new BufferedInputStream(istream);
					mimeType = d.detect(bistream, metadata).toString();
				} catch (IOException e) {
					log.error("Error detectando mimetype", e);
				} finally {
					if (istream != null)
						try {
							istream.close();
						} catch (IOException e) {
							log.error("Error cerrando streaming", e);
						}
					if (bistream != null)
						try {
							bistream.close();
						} catch (IOException e) {
							log.error("Error cerrando streaming", e);
						}
				}
			}

		}

		InputStream stream = null;
		if (mimeType == null || mimeType.equals("")
				|| mimeType.equals("application/octet-stream")) {
			InputStream is = null;

			try {
				is = new BufferedInputStream(stream);
				mimeType = URLConnection.guessContentTypeFromStream(is);
			} catch (IOException io) {
				log.error("Error checking mimetype", io);
			} finally {
				try {
					if(is!=null){is.close();}
				} catch (IOException e) {
					log.error("Error closing stream", e);
				}
				if (mimeType == null)
					mimeType = "application/octet-stream";
			}
		}
		return mimeType;
	}

        @Override
	public List<DOCUMENT> getPagedPendingRequests(Long from, Integer pageSize,
			String colOrder, ASortOrder ascDesc) {

		if (pageSize == null || pageSize <= 0)
			pageSize = 10;

		if (from == null || from < 0)
			from = 0l;

		List<DOCUMENT> res = new LinkedList<DOCUMENT>();
		
		Class<DOCUMENT> documentClass = getDocumentClass();

		ItemIterable<QueryResult> query_ = getPendientesQuery(
				documentClass,
				colOrder, ascDesc, pageSize);

		ItemIterable<QueryResult> queryResult = query_.skipTo(from).getPage();
		
		for (QueryResult item : queryResult) {
			DOCUMENT document = createModelDocument(item, documentClass);
			res.add(document);
		}

		return res;
	}

        @Override
	public String getMimeType(String id) {
		DOCUMENT p = createDocumentInstance(null);
		p.setIdentifier(id);

		ObjectId identificador = findObjectId(p);

		CmisObject object = getSession().getObject(identificador);

		if (object != null) {
			Document d = (Document) object;
			return d.getContentStream().getMimeType();
		}

		return null;
	}

        @Override
	public DOCUMENT getDocument(String identificador) {
		DOCUMENT p = createDocumentInstance(null);
		p.setIdentifier(identificador);
		ObjectId id = findObjectId(p);

		CmisObject document = getSession().getObject(id);

		LinkedList<PropertyData<?>> prop = new LinkedList<PropertyData<?>>();
		for (PropertyData<?> pd : document.getProperties()) {
			prop.add(pd);
		}

		p.copyFromProperties(prop);
		return p;
	}

        @Override
	public File downloadDocument(String id) {
		DOCUMENT p = createDocumentInstance(null);
		p.setIdentifier(id);

		ObjectId identificador = findObjectId(p);

		File f = null;
		InputStream stream = null;

		BufferedOutputStream bos = null;
		try {
			f = File.createTempFile("cmis", "ohg");
			f.deleteOnExit();

			CmisObject object = getSession().getObject(identificador);
			Document document = (Document) object;
			stream = document.getContentStream().getStream();

			bos = new BufferedOutputStream(new FileOutputStream(f));
			IOUtils.copy(stream, bos);
			bos.flush();

		} catch (IOException io) {
			log.error(
					"Error creating a temp file. Check disk space and permissions. ",
					io);
		} finally {
			try {
				if (stream != null)
					stream.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				log.error("Error closing streamings", e);
			}

		}
		return f;
	}

        @Override
	public ObjectId updateDocument(DOCUMENT p) {
		ObjectId id = findObjectId(p);
		CmisObject document = getSession().getObject(id);
		Map<String, Object> properties = p.toAlfrescoProperties();
		ObjectId updateProperties = document.updateProperties(properties, true);

		return updateProperties;
	}

        @Override
	public Boolean deleteDocument(DOCUMENT p) {

		ObjectId id = findObjectId(p);
		getSession().getObject(id).delete(true);
		getSession().removeObjectFromCache(id);
		return true;
	}

	private ObjectId findObjectId(DOCUMENT publicacion) {

		ObjectId res = null;

		ObjectType type = getSession().getTypeDefinition(publicacion.getDocumentType());

		PropertyDefinition<?> objectIdPropDef = type.getPropertyDefinitions()
				.get(PropertyIds.OBJECT_ID);
		String objectIdQueryName = objectIdPropDef.getQueryName();

		String query = "SELECT D.*, O.* FROM cmis:document AS D JOIN "
				+ type.getQueryName() + " AS O ON D." + objectIdQueryName
				+ " = O." + objectIdQueryName + " WHERE O." + PropertyIds.NAME
				+ " = '" + publicacion.getIdentifier()
				+ "' AND in_folder(O, '" + this.folder + "')";

		ItemIterable<QueryResult> queryResult = getSession()
				.query(query, false).getPage(1);

		for (QueryResult item : queryResult) {
			res = getSession().createObjectId(
					item.getPropertyValueByQueryName("O." + objectIdQueryName)
							.toString());
			break;
		}

		getSession().removeObjectFromCache(res);

		return res;
	}

	private Session getSession() {

		if (session != null) {
			// session.clear();
			return session;
		}

		SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(SessionParameter.USER, this.user);
		parameters.put(SessionParameter.PASSWORD, this.password);

		parameters.put(SessionParameter.ATOMPUB_URL, this.url);
		parameters.put(SessionParameter.BINDING_TYPE,
				BindingType.ATOMPUB.value());

		// Set the alfresco object factory
		parameters.put(SessionParameter.OBJECT_FACTORY_CLASS,
				"org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");

		parameters.put(SessionParameter.REPOSITORY_ID, this.store);

		session = sessionFactory.createSession(parameters);

		// session.getDefaultContext().setCacheEnabled(false);

		return session;
	}

	private DOCUMENT createModelDocument(QueryResult item, Class<DOCUMENT> documentClass) {
		DOCUMENT p = createDocumentInstance(documentClass);
		List<PropertyData<?>> pr = item.getProperties();

		log.trace("Recreating: " + item.getPropertyValueById(PropertyIds.NAME));
		p.setIdentifier(item.getPropertyValueById(PropertyIds.NAME)
				.toString());

		p.copyFromProperties(pr);
		return p;
	}

	

        @Override
	public Long countUserRequests(String usuario) {
		ItemIterable<QueryResult> query = getSolicitudesQuery(usuario, null,
				null, 10).getPage(Integer.MAX_VALUE);
		// return query.getPageNumItems();
		// No implementado al 100% en alfresco. Descomentar en futuras
		// versiones:
		// return query.getTotalNumItems();
		Long v = null;

		boolean correcto = false;
		while (!correcto) {
			try {
				v = query.getPageNumItems();
				correcto = true;
			} catch (Exception e) {
				correcto = false;
				try {
					Thread.sleep(RETRASO);
				} catch (Exception ee) {
				}
			}
		}
		return v;
	}

        @Override
	public Long countPendingRequests() {
		ItemIterable<QueryResult> query = getPendientesQuery(null, null, null, 10)
				.getPage(Integer.MAX_VALUE);
		// return query.getPageNumItems();
		// No implementado al 100% en alfresco. Descomentar en futuras
		// versiones:
		// return query.getTotalNumItems();
		Long v = null;

		boolean correcto = false;
		while (!correcto) {
			try {
				v = query.getPageNumItems();
				correcto = true;
			} catch (Exception e) {
				correcto = false;
				try {
					Thread.sleep(RETRASO);
				} catch (Exception ee) {
				}
			}
		}
		return v;
	}

	private ItemIterable<QueryResult> getPendientesQuery(
			Class<DOCUMENT> documentClass,
			String orderCol, ASortOrder orderAsc, Integer pageSize) {
		
		DOCUMENT dummyDocument = createDocumentInstance(documentClass);
		
		ObjectType type = getSession().getTypeDefinition(
				dummyDocument.getDocumentType());
		
		PropertyDefinition<?> estadoPropDef = type.getPropertyDefinitions()
				.get(dummyDocument.getPrefix()+":"+dummyDocument.getStateProperty());
		String estadoQueryName = estadoPropDef.getQueryName();

		String query = "SELECT D.*, O.* FROM cmis:document AS D JOIN "
				+ type.getQueryName()
				+ " AS O ON D.cmis:objectId = O.cmis:objectId WHERE O."
				+ estadoQueryName + " like '" + ADocumentState.PENDIENTE
				+ "' AND in_folder(O, '" + this.folder + "')";

		if (orderCol != null && orderAsc != null) {
			query += " ORDER BY O."+dummyDocument.getPrefix()+":" + orderCol;
			if (orderAsc ==  ASortOrder.DESC)
				query += " DESC";
			else
				query += " ASC";
		}
		Session s = getSession();
		OperationContext context = s.createOperationContext();
		context.setMaxItemsPerPage(pageSize);
		context.setCacheEnabled(true);
		context.setFilter(this.propertyFilter);

		ItemIterable<QueryResult> query_ = getSession().query(query, false,
				context);
		return query_;
	}

	private ItemIterable<QueryResult> getSolicitudesQuery(String u,
			String orderCol, ASortOrder order, int pageSize) {
		
		DOCUMENT dummyDoc = createDocumentInstance(null);
		
		ObjectType type = getSession().getTypeDefinition(
				dummyDoc.getDocumentType());
                
                
                JoinCriteria conditions = new JoinCriteria(JoinCriteria.AND);
                conditions.setPrefix("O."+dummyDoc.getPrefix());
                
                conditions.add(new EqualsCriteria(dummyDoc.getUserProperty(), u));
                conditions.add(new InFolderCriteria(folder));
                conditions.add(new NotCriteria(
                        new InValuesCriteria(dummyDoc.getStateProperty(),
                        new Object[]{ADocumentState.LEIDA,ADocumentState.PRIVATE})));
                                

		
		String query = "SELECT O.* FROM "
				+ type.getQueryName()
				+ " AS O WHERE "
				+ conditions.toString();

		if (orderCol != null && order != null) {
			query += " ORDER BY "+dummyDoc.getPrefix()+":" + orderCol;
			if (order == ASortOrder.DESC)
				query += " DESC";
			else
				query += " ASC";
		}

		OperationContext context = getSession().createOperationContext();
		context.setMaxItemsPerPage(pageSize);

		ItemIterable<QueryResult> query_ = getSession().query(query, false,
				context);
		return query_;
	}

	

	@Override
	public List<DOCUMENT> search(Criteria criteria, Long from, Integer max,
			String orderCol, ASortOrder order) {
		
		Class<DOCUMENT> documentClass = getDocumentClass();
		DOCUMENT dummyDoc = createDocumentInstance(documentClass);
		ObjectType type = getSession().getTypeDefinition(
				dummyDoc.getDocumentType());

		String query = "SELECT D.*, O.* FROM cmis:document AS D JOIN "
				+ type.getQueryName()
				+ " AS O ON D.cmis:objectId = O.cmis:objectId";

		if (criteria != null) {
			Criteria inFolder = new InFolderCriteria(this.folder);
			Criteria and = new AndCriteria(criteria, inFolder);
			and.setPrefix("O");
			and.initializeCriteria(type);
			query += " WHERE " + and.toString();
		}

		if (orderCol != null && order != null) {
			query += " ORDER BY O."+dummyDoc.getPrefix()+":" + orderCol;
			if (order == ASortOrder.DESC)
				query += " DESC";
			else
				query += " ASC";
		}

		System.out.println(query);

		ItemIterable<QueryResult> query_ = getSession().query(query, false);

		List<DOCUMENT> res = new LinkedList<DOCUMENT>();

		for (QueryResult item : query_) {
			res.add(createModelDocument(item, documentClass));
		}

		return res;
	}

	/**
	 * Total de elementos para la consulta del buscador de gestion de estudios
	 * 
	 * @return
	 */
        @Override
	public Long countFilteredDocuments(FILTER estudioFilter,
			ADocumentState[] estados) {
		ItemIterable<QueryResult> query = getGestionEstudiosQuery(
				estudioFilter, null, null, estados, 10).getPage(
				Integer.MAX_VALUE);
		Long v = null;

		boolean correcto = false;
		int numIntentos = 0;
		while (!correcto && numIntentos++ < MAX_NUM_INTENTOS) {

			try {
				v = query.getPageNumItems();
				correcto = true;
			} catch (Exception e) {
				correcto = false;
				try {
					Thread.sleep(RETRASO);
				} catch (Exception ee) {
				}
			}
		}

		return v;
		// No implementado al 100% en alfresco. Descomentar en futuras
		// versiones:
		// return query.getTotalNumItems();
	}

	/**
	 * Consulta para gestion de estudios
	 * 
	 * @param from
	 * @param pageSize
	 * @param colOrder
	 * @param ascDesc
	 * @return
	 */
        @Override
	public List<DOCUMENT> getFilteredDocuments(FILTER estudioFilter,
			Long from, Integer pageSize, String colOrder, ASortOrder ascDesc,
			ADocumentState[] estados) {

		if (pageSize == null || pageSize <= 0)
			pageSize = Integer.MAX_VALUE;

		if (from == null || from < 0)
			from = 0l;

		List<DOCUMENT> res = new LinkedList<DOCUMENT>();

		ItemIterable<QueryResult> query_ = getGestionEstudiosQuery(
				estudioFilter, colOrder, ascDesc, estados, pageSize);

		ItemIterable<QueryResult> queryResult = query_.skipTo(from).getPage();

		Class<DOCUMENT> documentClass = getDocumentClass();
		for (QueryResult item : queryResult) {
			res.add(createModelDocument(item, documentClass));
		}

		return res;
	}

	/**
	 * Consulta para el buscador de gestios estudios No muestra las pendientes.
	 * 
	 * @param estudioFilter
	 * @param orderCol
	 * @param order
	 * @return
	 */
	private ItemIterable<QueryResult> getGestionEstudiosQuery(
			FILTER estudioFilter, String orderCol, ASortOrder order,
			ADocumentState[] estados, Integer pageSize) {

		DOCUMENT dummyDoc = createDocumentInstance(null);
		
		// Publicacion
		ObjectType type = getSession().getTypeDefinition(dummyDoc.getDocumentType());

		
					// Sql base
		StringBuilder query = new StringBuilder(
				"SELECT D.*, O.* FROM cmis:document AS D JOIN "
						+ type.getQueryName()
						+ " AS O ON D.cmis:objectId = O.cmis:objectId ");

		JoinCriteria conditions = new JoinCriteria("AND");
		conditions.setPrefix("O."+dummyDoc.getPrefix());
		
		// estados
		if (estados != null && estados.length > 0) {
			conditions.add(new InValuesCriteria(dummyDoc.getStateProperty(), estados));
		}

		if (estudioFilter != null) {			
			estudioFilter.addToConditions(conditions);					
		}

		InFolderCriteria folderCriteria = new InFolderCriteria(this.folder);
		conditions.add(folderCriteria);                
		
		query.append(" WHERE ");
		query.append(conditions.toString());		

		// Order
		if (orderCol != null && order != null) {
			query.append(" ORDER BY O.").append(dummyDoc.getPrefix()).append(":").append(orderCol);
			if(order ==  ASortOrder.ASC){
				query.append(" ASC");
			} else {
				query.append(" DESC");
			}			
		}
		
		Session s = getSession();
		OperationContext context = s.createOperationContext();
		context.setMaxItemsPerPage(pageSize);
		context.setCacheEnabled(true);
		context.setFilter(this.propertyFilter);
		context.setIncludePolicies(false);
		context.setIncludeAcls(false);
		context.setIncludeAllowableActions(false);

		ItemIterable<QueryResult> query_ = getSession().query(query.toString(),
				false, context);
		return query_;
	}



	/**
	 * Funcion ilike para consultas
	 * 
	 * @param campo
	 * @param nombreparametro
	 * @return
	 */
	public String ilikep2(String campo, String nombreparametro) {
		/*
		 * String funcion = " UPPER(" + campo +") like '%" +
		 * nombreparametro.toUpperCase() + "%' "; return funcion;
		 */
		/*
		 * String funcion = " UPPER(translate(trim(" + campo
		 * +"), 'áéíóúÁÉÍÓÚ', 'aeiouAEIOU')) like '%" +
		 * nombreparametro.toUpperCase() + "%' "; return funcion;
		 */
		String funcion = campo + " like '%" + nombreparametro + "%' ";
		return funcion;
	}

	/**
	 * Metodo que sustituye una cadena por otra
	 * 
	 * @param cadena
	 * @return
	 */
	public String sustituir(String cadena, String quitar, String poner) {
		String resultado = cadena;

		if (cadena == null || quitar == null || poner == null)
			return resultado;

		if (!cadena.equals("") && !quitar.equals("")) {
			int posicion = cadena.indexOf(quitar);
			if (posicion != -1) {
				int tamanio = quitar.length();
				String resto = cadena.substring(posicion + tamanio);
				resultado = cadena.substring(0, posicion) + poner
						+ sustituir(resto, quitar, poner);
			}
		}

		return resultado;
	}

}
