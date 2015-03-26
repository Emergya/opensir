package com.emergya.ohiggins.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernatespatial.postgis.PostgisDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.emergya.ohiggins.dao.ImportGeoJdbDao;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.importers.ShpImporter;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.security.SecurityUtils;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.utils.ColumnMetadata;
import com.emergya.ohiggins.utils.DatabaseMetadata;
import com.emergya.ohiggins.utils.TableMetadata;
import com.emergya.ohiggins.web.util.SimpleUploadFormLayerUpdate;
import com.emergya.ohiggins.web.validators.MultiFileUploadValidatorLayerUpdate;
import com.emergya.persistenceGeo.dao.DBManagementDao;
import com.emergya.persistenceGeo.importer.shp.IShpImporter;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.BoundingBox;
import com.emergya.persistenceGeo.utils.GeographicDatabaseConfiguration;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.emergya.persistenceGeo.utils.GeometryType;
import com.google.common.base.Strings;

import java.util.Map.Entry;

@Controller
public class LayerUpdateController extends AbstractController {

	private static final Log LOG = LogFactory.getLog(AuthorityLayersController.class);

	public final static String MODULE = "cartografico";
	public final static String SUB_MODULE = "layersAuthority";

	private final static String ADDEDCOLUMNSNUM = "addedColumnsNum";
	private final static String DELETEDCOLUMNSNUM = "deletedColumnsNum";
	private final static String ADDEDCOLUMNS = "addedColumns";
	private final static String DELETEDCOLUMNS = "deletedColumns";
	private final static String ORIGINALTABLEID = "originalTableId";
	private final static String SAMECOLUMNS = "sameColumns";
	private final static String NEWTABLENAME = "newTableName";
	private static final String XLSPATH = "xlsPath";
	private static final String IDINDEX = "idIndex";
	private static final String UNPROCESSEDROWNUM = "unprocesseRowsNum";
	private static final String XLS_COLUMNS_IGNORED = "columnsIgnored";
	private static final String TABLE_COLUMNS_IGNORED = "tableColumnsIgnored";
	private static final String UPDATED_ROWS_NUM = "updatedRowsNum";

	private static final String ID_COLUMN = "ID";
	private static final String METODO = "metodo";
	private static final String SHP = "0";
	private static final String EXCEL_CSV = "1";

	@Resource
	private LayerService layerService;
	
	
	@Resource
	private LayerResourceService layerResourceService;
	
	@Resource
	private DBManagementDao dbManagementDao;
	
	@Resource
	private GeoserverService geoserverService;
	@Autowired
	private UserAdminService userService;
	@Autowired
	private ImportGeoJdbDao importGeoJdbDao;	
	@Autowired
	private ShpImporter shpImporter;
	@Autowired
	private IShpImporter commandLineImporter;
	@Autowired
	private MultiFileUploadValidatorLayerUpdate fileUploadValidatorLayerUpdate;
	@Autowired
	@Qualifier("dataSourceHibernate")
	private DataSource datasource;
	@Autowired(required = false)
	private GeographicDatabaseConfiguration dbConfig;

	/**
	 * Muestra el formulario para mostrar la actualizacion de capa
	 * 
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@RequestMapping(value = "/cartografico/editarLayer/{id}")
	public String actualizarLayerAuthority(@PathVariable long id, Model model,
			WebRequest webRequest) {

		isLogate(webRequest);

		LayerDto layer = (LayerDto) layerService.getById(id);
		model.addAttribute("id", layer.getId());// SHP ZIP
		model.addAttribute("layerLabel", layer.getLayerLabel());

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, getActiveSubModule());

		// Valor por defecto
		model.addAttribute(METODO, SHP);// SHP ZIP

		return "cartografico/actualizarCapa";
	}

	/**
	 * Muestra el formulario para mostrar la actualizacion de capa
	 * 
	 * @param model
	 * @param webRequest
	 * @return
	 */
	@RequestMapping(value = "/cartografico/actualizaCapaAuthority")
	public String actualizaCapaAuthority(
			@ModelAttribute("uploadForm") SimpleUploadFormLayerUpdate uploadForm,
			@RequestParam("id") long id, @RequestParam("metodo") String metodo,
			BindingResult result, Model model, WebRequest webRequest) {

		isLogate(webRequest);
		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, getActiveSubModule());
		
		
		LayerDto layer = (LayerDto) layerService.getById(id);
		model.addAttribute("layerLabel", layer.getLayerLabel());
		
		// We create a session key for this particular upload so we don't have
		// problems with updates in several browser tabs.
		String sessionKey = UUID.randomUUID().toString();
		model.addAttribute("sessionKey", sessionKey);

		// Validamos el fichero subido y si hay algún error redirigimos al
		// formulario de subida mostrando los mensajes de error
		fileUploadValidatorLayerUpdate.setMetodo(metodo);
		fileUploadValidatorLayerUpdate.validate(uploadForm, result);
		SimpleUploadFormLayerUpdate form = (SimpleUploadFormLayerUpdate) uploadForm;
		try {
			if (result.hasErrors()) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Se han encontrados errores al validar el archivo enviado para actualizar iniciativas de inversi\u00f3n");

				}
				model.addAttribute("correcto", false);
				model.addAttribute("id", id);
				model.addAttribute(METODO,
						(metodo != null && SHP.equals(metodo) ? SHP : EXCEL_CSV));
				return "cartografico/actualizarCapa";

			} else if (metodo.equals(SHP)) {
				// Update from SHP file.
				return checkAndLoadSHP(id, uploadForm, model, result, webRequest, sessionKey);
			} else if (metodo.equals(EXCEL_CSV)) {
				// Update from XLS file or CSV
				return checkAndLoadExcel(id, uploadForm, model, result, webRequest, sessionKey);
			}

		} catch (IOException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("actualizaCapaAuthority. Error al intentar leer el fichero de subida.");
			}
			model.addAttribute("correcto", false);
			return "cartografico/actualizarCapa";
		} catch (InvalidFormatException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("actualizaCapaAuthority. Error al intentar leer el fichero EXCEL de subida. Compruebe que el fichero no está corrupto.");
			}
			model.addAttribute("correcto", false);
			return "cartografico/actualizarCapa";
		} catch (SQLException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("actualizaCapaAuthority. Error al intentar crear la tabla temporal en base de datos.");
			}
			model.addAttribute("correcto", false);
			return "cartografico/actualizarCapa";
		} catch (Exception e) {
			result.rejectValue(form.getFilePropertyName(),
					"uploadForm.shpNotFound",
					e.getMessage());
			if (LOG.isDebugEnabled()) {
				LOG.debug("Se han encontrados errores al validar el archivo enviado para actualizar iniciativas de inversi\u00f3n. Fichero SHP no v\u00e1lido.");
			}
			model.addAttribute("correcto", false);
			model.addAttribute("id", id);
			model.addAttribute(METODO,
					(metodo != null && SHP.equals(metodo) ? SHP : EXCEL_CSV));
			return "cartografico/actualizarCapa";
		}

		return "cartografico/actualizarCapa";
	}

	private String checkAndLoadSHP(long id, SimpleUploadFormLayerUpdate uploadForm, Model model, BindingResult result,
			WebRequest webRequest, String sessionKey) throws Exception {
		
	    File zipFile = File.createTempFile("shp_imp", ".zip");
		uploadForm.getFile().transferTo(zipFile);
		zipFile.deleteOnExit();
		File shpFile = shpImporter.unzipAndLookForShp(zipFile);

		if (shpFile == null || !commandLineImporter.checkIfAllFilesExist(shpFile
				.getParentFile().getAbsolutePath(), shpFile.getName()
				.replace(".shp", ""))) {
			throw new Exception("Para subir un archivo SHP debe crear y seleccionar un archivo ZIP que contenga al menos los archivos .SHP, .DBF y .SHX que forman el shapefile");

		}

		// Si el fichero subido cumple con el formato y contiene los
		// ficheros asociados al SHP:
		String tableName = layerResourceService.generateNameNotYetUsed();
		boolean imported = commandLineImporter.importShpToDb(
				shpFile.getAbsolutePath(), tableName, false);

		if (!imported) {
			throw new Exception("No se pudo importar a base de datos.");
		}
		Map<String, ColumnMetadata> columns = getTableColumns(tableName);
		List<String> columnsName = new ArrayList<String>();
		boolean idOK = false;

		// Buscamos un campo con nombre ID y de tipo integer.
		for (Entry<String,ColumnMetadata> columnInfo : columns.entrySet()) {
			ColumnMetadata column = columnInfo.getValue();
			if (ID_COLUMN.equalsIgnoreCase(column.getName()) && importGeoJdbDao.isFieldNumeric(column)) {
				idOK = true;
			}
			columnsName.add(column.getName());
		}

		if (!idOK) {
			throw new Exception(
					"No se ha encontrado ningún campo de nombre 'ID' y tipo Numérico dentro del ZIP");
		}

		// Obtenemos la capa original de base de datos.
		LayerDto ld = (LayerDto) layerService.getById(new Long(id));
		if(Strings.isNullOrEmpty(ld.getTableName())) {
		    throw new Exception("La capa a actualizar no tiene tabla asociada en base de datos.");
		}
		
		
		Map<String, ColumnMetadata> columnsOriginal = getTableColumns(ld.getTableName());

		List<String> addedColumns = new ArrayList<String>();
		List<String> columnOriginalStr = getTableNames(columnsOriginal);

		// Comprobamos que la tabla a actualizar contiene un campo id
		String tableIdField = this.getTableIdField(columnOriginalStr);
		if (Strings.isNullOrEmpty(tableIdField)) {
			throw new Exception(
					"No se ha encontrado ningún campo de nombre 'ID' dentro de los atributos de la capa a actualizar.");
		} else {
			columnOriginalStr.remove(tableIdField);
		}

		for (String columnName : columnsName) {
			if (!columnOriginalStr.contains(columnName)) {
				if (!columnName.equalsIgnoreCase(ID_COLUMN)) {
					addedColumns.add(columnName);
				}
			} else {
				columnOriginalStr.remove(columnName);
			}
		}

		List<String> deletedColumns = new ArrayList<String>();
		deletedColumns.addAll(columnOriginalStr);

		model.addAttribute(ADDEDCOLUMNS, addedColumns);
		model.addAttribute(DELETEDCOLUMNS, deletedColumns);
		model.addAttribute("correcto", true);
		model.addAttribute("id", id);
		model.addAttribute(METODO, SHP);
		model.addAttribute("layerLabel", ld.getLayerLabel());
		
		addToSession(webRequest, sessionKey,ADDEDCOLUMNSNUM, addedColumns.size());
		addToSession(webRequest, sessionKey,DELETEDCOLUMNSNUM, deletedColumns.size());
		addToSession(webRequest, sessionKey,NEWTABLENAME, tableName);

		return "cartografico/actualizarCapa2";
	}

	private String checkAndLoadExcel(
			Long layerId, SimpleUploadFormLayerUpdate uploadForm,
			Model model, BindingResult result, WebRequest webRequest, String sessionKey)
			throws Exception {

		InputStream is;
		MultipartFile mpFile = uploadForm.getFile();
		File xslFile;
		// Leemos el XLS de subida.
		xslFile = File.createTempFile("xls_imp", ".xls");
		is = mpFile.getInputStream();
		mpFile.transferTo(xslFile);
		xslFile.deleteOnExit();
		String tmpFilePath = xslFile.getAbsolutePath();

		Workbook workbook = WorkbookFactory.create(is);
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(sheet.getFirstRowNum());

		List<String> excelColumns = new ArrayList<String>();
		String columnName;
		boolean hasID = false;
		int idIndex = 0;

		// Extraemos los nombres de columnas de XLS a importar.
		List<Integer> columnsIgnored = new ArrayList<Integer>();
		int columnsNumber = row.getPhysicalNumberOfCells();
		
		for (int i = 0; i < columnsNumber; i++) {
			columnName = "";
			if (row.getCell(i) != null) {
				columnName = GeoserverUtils.sanitizeColumnName(row.getCell(i).getStringCellValue());
			}

			if (StringUtils.isNotBlank(columnName)) {
				if (!hasID && columnName.equalsIgnoreCase(ID_COLUMN)) {
					hasID = true;
					idIndex = i;
				}
				excelColumns.add(columnName);
			} else {
				columnsIgnored.add(new Integer(i));
			}
		}

		// Comprobamos que el XLS a importar posee un campo id
		if (!hasID) {
			throw new Exception(
					"Para subir un archivo XLS éste debe contener una columna con nombre 'ID' y datos de tipo numérico.");
		}

		List<String> addedColumns = new ArrayList<String>();
		List<String> sameColumns = new ArrayList<String>();

		// Extraemos los nombres de columnas de la tabla de base de
		// datos.
		LayerDto ld = (LayerDto) layerService.getById(new Long(layerId));
		if(Strings.isNullOrEmpty(ld.getTableName())) {
		    throw new Exception("La capa a actualizar no tiene tabla asociada en base de datos.");
		}
		
		
		
		Map<String, ColumnMetadata> columnsOriginal = getTableColumns(ld.getTableName());
		List<String> columnOriginalStr = getTableNames(columnsOriginal);

		// Comprobamos que la tabla a actualizar contiene un campo id
		String tableIdField = getTableIdField(columnOriginalStr);
		if (Strings.isNullOrEmpty(tableIdField)) {
			throw new Exception(
					"No se ha encontrado ningún campo de nombre 'ID' dentro de los atributos de la capa a actualizar.");
		} else {			
			// We don't remove the table's id.
			columnOriginalStr.remove(tableIdField);
		}

		// Obtenemos los nombres de columnas a eliminar, añadir y
		// actualizar.
		for (String excelColumn : excelColumns) {
			if (!columnOriginalStr.contains(excelColumn)) {
				if (!excelColumn.equalsIgnoreCase(ID_COLUMN)) {
					addedColumns.add(excelColumn);
				}
			} else {
				if (!excelColumn.equalsIgnoreCase(ID_COLUMN)) {
					sameColumns.add(excelColumn);
				}
				columnOriginalStr.remove(excelColumn);
			}
		}
		
		List<String> deletedColumns = new ArrayList<String>(columnOriginalStr);
		deletedColumns.remove("geom");
		
		List<Map<String,Object>> constraints = importGeoJdbDao.getColumnsConstraints(ld.getTableName());
		// If any column has a constraint we don't remove or update it.
		for(Map<String,Object> constraintInfo : constraints) {
			String constrainedColumnName = (String) constraintInfo.get("column_name");
			deletedColumns.remove(constrainedColumnName);
		}

		// Construimos la respuesta a la vista.
		model.addAttribute(ADDEDCOLUMNS, addedColumns);
		model.addAttribute(DELETEDCOLUMNS, deletedColumns);
		model.addAttribute(SAMECOLUMNS, sameColumns);
		model.addAttribute("correcto", true);
		model.addAttribute("id", layerId);
		model.addAttribute(METODO, EXCEL_CSV);
		model.addAttribute("layerLabel", ld.getLayerLabel());

		addToSession(webRequest, sessionKey, ADDEDCOLUMNS, addedColumns);
		addToSession(webRequest, sessionKey, DELETEDCOLUMNS, deletedColumns);
		addToSession(webRequest, sessionKey, XLSPATH, tmpFilePath);
		addToSession(webRequest, sessionKey, IDINDEX, idIndex);
		addToSession(webRequest, sessionKey, XLS_COLUMNS_IGNORED, columnsIgnored);
		addToSession(webRequest, sessionKey, TABLE_COLUMNS_IGNORED, columnOriginalStr);
		addToSession(webRequest, sessionKey, ORIGINALTABLEID, tableIdField);

		return "cartografico/actualizarCapa2";
	}

	private void addToSession(WebRequest request, String sessionKey, String parameterName, Object parameterValue) {
		request.setAttribute(parameterName + "_" + sessionKey, parameterValue, RequestAttributes.SCOPE_SESSION);
	}

	private Object getFromSession(WebRequest request, String sessionKey, String parameterName) {
		return request.getAttribute(parameterName + "_" + sessionKey, RequestAttributes.SCOPE_SESSION);
	}

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/cartografico/actualizaCapaAuthorityXls")
	public String actualizaCapaAuthorityXls(
			@ModelAttribute("uploadForm") SimpleUploadFormLayerUpdate uploadForm,
			@RequestParam("id") long id,
			@RequestParam("sessionKey") String sessionKey,
			BindingResult result, Model model, WebRequest webRequest) {

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, getActiveSubModule());
		
		model.addAttribute("id", id);

		List<String> addedColumns = (List<String>) getFromSession(webRequest, sessionKey, ADDEDCOLUMNS);
		List<String> deletedColumns = (List<String>) getFromSession(webRequest, sessionKey, DELETEDCOLUMNS);
		String xlsPath = (String) getFromSession(webRequest, sessionKey, XLSPATH);
		Integer idIndex = (Integer) getFromSession(webRequest, sessionKey, IDINDEX);
		String tableIdField = (String) getFromSession(webRequest, sessionKey, ORIGINALTABLEID);
		List<Integer> xlsColumnsIgnored = (List<Integer>) getFromSession(webRequest,sessionKey, XLS_COLUMNS_IGNORED);
		List<String> tableColumnsIgnored = (List<String>) getFromSession(webRequest,sessionKey, TABLE_COLUMNS_IGNORED);

		int unprocesseRowsNum = 0;

		try {
			LayerDto ld = (LayerDto) layerService.getById(new Long(id));
			model.addAttribute("layerLabel", ld.getLayerLabel());

			// Eliminamos todas las columnas de la tabla que no existen en el
			// XLS excepto la geométrica y el id.
			for (String column : deletedColumns) {
				importGeoJdbDao.deleteTableColumns(ld.getTableName(), column);
			}

			// Insertamos las columnas nuevas del XLS en la tabla
			for (String column : addedColumns) {
				importGeoJdbDao.createNewColumn(ld.getTableName(), column, "String");
			}

			// Recopilamos los nombres de columnas actualizados
			Map<String, ColumnMetadata> columnsTable = getTableColumns(ld.getTableName());
			
			for(String ignoredTableColumn : tableColumnsIgnored){
			    if(columnsTable.containsKey(ignoredTableColumn)) {
				columnsTable.remove(ignoredTableColumn);
			    }			    
			}
			
						
			ColumnMetadata tableIdColumnInfo = columnsTable.remove(tableIdField);

			InputStream is = new FileInputStream(xlsPath);
			Workbook workbook = WorkbookFactory.create(is);
			Sheet sheet = workbook.getSheetAt(0);
			int startIndex = sheet.getFirstRowNum() + 1;

			int processedRowsNum = 0;
			for (int i = startIndex; i < sheet.getPhysicalNumberOfRows(); i++) {
				String excelId = new Double(sheet.getRow(i).getCell(idIndex).getNumericCellValue()).toString();
				// Comprobamos que existe una fila con el id del excel en la tabla
				List<Map<String, Object>> tableDataById = importGeoJdbDao.getTableDataById(					    
					    ld.getTableName(), tableIdColumnInfo,
					    excelId);
				if (tableDataById != null && tableDataById.size() > 0) {
					// Actualizamos la fila
					importGeoJdbDao.updateTableRow(
						ld.getTableName(), columnsTable,
						getExcelColumnsvalues(sheet.getRow(i), idIndex, xlsColumnsIgnored), 
						tableIdColumnInfo, excelId);
					processedRowsNum++;
				} else {
					unprocesseRowsNum++;
				}
			}

			updateLayer(id, null);

			model.addAttribute(ADDEDCOLUMNSNUM, addedColumns.size());
			model.addAttribute(DELETEDCOLUMNSNUM, deletedColumns.size());
			model.addAttribute(UNPROCESSEDROWNUM, unprocesseRowsNum);
			model.addAttribute(UPDATED_ROWS_NUM,processedRowsNum);
			model.addAttribute(METODO, EXCEL_CSV);
			model.addAttribute("correcto", true);

		} catch (SQLException e) {
			SimpleUploadFormLayerUpdate form = (SimpleUploadFormLayerUpdate) uploadForm;
			result.rejectValue(form.getFilePropertyName(), "inversion.cannotUpdate",
					"Se ha producido un error al intentar añadir/eliminar columnas de la capa.");
			result.reject(
					"inversion.cannotUpdate",
					"Error en la actualización de la capa.");

			if (LOG.isDebugEnabled()) {
				LOG.debug("Se han encontrados errores al intentar publicar la capa.");

			}
			model.addAttribute("correcto", false);
			return "cartografico/actualizarCapa2";
		} catch (Exception e) {
			SimpleUploadFormLayerUpdate form = (SimpleUploadFormLayerUpdate) uploadForm;
			result.rejectValue(form.getFilePropertyName(), "inversion.cannotUpdate",
					"Se ha producido un error al intentar publicar la capa.");
			result.reject(
					"inversion.cannotUpdate",
					"Error en la actualización de la capa.");

			LOG.error("Error updating row", e);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Se han encontrados errores al intentar publicar la capa.");

			}
			model.addAttribute("correcto", false);
			return "cartografico/actualizarCapa2";
		}

		return "cartografico/actualizarCapa3";
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/cartografico/actualizaCapaAuthorityShp")
	public String actualizaCapaAuthorityShp(
			@ModelAttribute("uploadForm") SimpleUploadFormLayerUpdate uploadForm,
			@RequestParam("id") long id,
			@RequestParam("sessionKey") String sessionKey,
			BindingResult result, Model model, WebRequest webRequest) {

		rolUser(model);
		model.addAttribute(MODULE_KEY, getActiveModule());
		model.addAttribute(SUBMODULE_KEY, getActiveSubModule());
		model.addAttribute("id", id);

		String newTableName = (String) getFromSession(webRequest, sessionKey,NEWTABLENAME);
		int addedColumnsNum = (Integer) getFromSession(webRequest, sessionKey,ADDEDCOLUMNSNUM);
		int deletedColumnsNum = (Integer) getFromSession(webRequest, sessionKey,DELETEDCOLUMNSNUM);

		try {
			updateLayer(id, newTableName);

			model.addAttribute(ADDEDCOLUMNSNUM, addedColumnsNum);
			model.addAttribute(DELETEDCOLUMNSNUM, deletedColumnsNum);
			model.addAttribute("correcto", true);
			model.addAttribute(METODO, SHP);

			LayerDto layer = (LayerDto) layerService.getById(id);
			model.addAttribute("layerLabel", layer.getLayerLabel());

		} catch (Exception e) {
			SimpleUploadFormLayerUpdate form = (SimpleUploadFormLayerUpdate) uploadForm;
			result.rejectValue(form.getFilePropertyName(), "inversion.cannotUpdate",
					"Se ha producido un error al intentar publicar la capa.");
			result.reject(
					"uploadForm.cannotUpdate",
					"Error en la actualización de la capa.");

			if (LOG.isDebugEnabled()) {
				LOG.debug("Se han encontrados errores al intentar publicar la capa.");

			}
			model.addAttribute("correcto", false);
			return "cartografico/actualizarCapa2";
		}

		return "cartografico/actualizarCapa3";
	}

	private List<String> getExcelColumnsvalues(Row row, int idIndex, List<Integer> columnsIgnored) {
		List<String> values = new ArrayList<String>();
		String value;
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum() ; i++) {
		    if(i == idIndex ||columnsIgnored.contains(new Integer(i))) {
			continue;
		    }
		    
		    Cell cell = row.getCell(i);
		    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			    value = new Double(cell.getNumericCellValue()).toString();
		    } else {
			    value = cell.getStringCellValue();
		    }
		    values.add(value);
		}
		return values;
	}

	private List<String> getTableNames(Map<String, ColumnMetadata> metadaList) {
		List<String> columnsStr = new ArrayList<String>();
		for (ColumnMetadata column : metadaList.values()) {
			columnsStr.add(column.getName());
		}
		return columnsStr;
	}

	private String getTableIdField(List<String> metadaList) {
		String originalTableId = "";
		boolean contains = false;
		for (int i = 0; i < metadaList.size() && !contains; i++) {
			if (metadaList.get(i).equalsIgnoreCase(ID_COLUMN)) {
				originalTableId = metadaList.get(i);
				contains = true;
			}
		}
		return originalTableId;
	}

	private void updateLayer(Long layerId, String tableName) throws Exception {
		// Actualizamos la capa el geoserver
		LayerDto ld = (LayerDto) layerService.getById(layerId);
		String originalLayerName = ld.getNameWithoutWorkspace();
		if (StringUtils.isNotBlank(tableName)) {
			ld.setTableName(tableName);
		} else {
			tableName = ld.getTableName();
		}
		ld.setUpdateDate(new Date());

		OhigginsUserDetails usuario = (OhigginsUserDetails) SecurityUtils
				.getPrincipal();

		String userName = null;
		if (usuario != null) {
			userName = usuario.getUsername();
		}

		String workspaceName = userService.getWorkspaceName(userName);
		BoundingBox bbox = dbManagementDao.getTableBoundingBox(tableName);
		GeometryType type = dbManagementDao.getTableGeometryType(tableName);

		if (geoserverService.unpublishGsDbLayer(workspaceName, originalLayerName)) {
			geoserverService.publishGsDbLayer(workspaceName, tableName,
					originalLayerName, ld.getLayerLabel(), bbox, type);
			LOG.info("Layer " + originalLayerName + "update in Geoserver!");
			layerService.update(ld);
		} else {
			throw new Exception("Error al intentar despublicar la capa " + originalLayerName);
		}
	}

	private Map<String, ColumnMetadata> getTableColumns(String tableName)
			throws SQLException {
		Connection conn = datasource.getConnection();
		DatabaseMetadata dbMeta = new DatabaseMetadata(conn, new PostgisDialect());

		TableMetadata tableMeta = dbMeta.getTableMetadata(tableName,
				dbConfig.getSchema(), "%", true);
		Map<String, ColumnMetadata> columns = tableMeta.getColumns();
		
		conn.close();

		return columns;
	}

	@Override
	protected void copyDefaultModel(boolean update, Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getAllSubTabs() {
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		return 0;
	}

	@Override
	protected String getDefaultPaginationUrl() {
		return "cartografico/layersAuthority";
	}

	@Override
	protected String getActiveModule() {
		return MODULE;
	}

	@Override
	protected String getActiveSubModule() {
		return SUB_MODULE;
	}

	private void rolUser(Model model) {
		model.addAttribute("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
	}

}
