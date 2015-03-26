package com.emergya.ohiggins.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emergya.ohiggins.dao.ExcelJdbDao;
import com.emergya.ohiggins.dao.OhigginsFileColumnDao;
import com.emergya.ohiggins.dao.OhigginsFileTypeDao;
import com.emergya.ohiggins.dto.FileColumnDto;
import com.emergya.ohiggins.dto.FileTypeDto;
import com.emergya.ohiggins.exceptions.XlsException;
import com.emergya.ohiggins.model.FileColumnEntity;
import com.emergya.ohiggins.model.FileTypeEntity;
import com.emergya.ohiggins.service.FileTypeService;
import com.emergya.persistenceGeo.dao.GenericDAO;
import com.emergya.persistenceGeo.service.impl.AbstractServiceImpl;

@Service
public class OhigginsFileTypeService extends
		AbstractServiceImpl<FileTypeDto, FileTypeEntity> implements
		FileTypeService {
	@Resource
	private OhigginsFileTypeDao fileTypeDao;
	@Resource
	private OhigginsFileColumnDao fileColumnDao;
	@Autowired(required = false)
	ExcelJdbDao excelDao;

	@Override
	protected GenericDAO<FileTypeEntity, Long> getDao() {
		return fileTypeDao;
	}

	protected FileColumnDto fileColumnEntityToDto(FileColumnEntity entity) {
		FileColumnDto result = new FileColumnDto();
		result.setCreateDate(entity.getCreateDate());
		result.setDbName(entity.getDbName());
		if (entity.getFileType() != null) {
			result.setFileTypeEntity(entityToDto(entity.getFileType()));
		}
		result.setId(entity.getId());
		result.setName(entity.getName());
		result.setType(entity.getType());
		result.setUpdateDate(entity.getUpdateDate());

		return result;
	}

	@Override
	protected FileTypeDto entityToDto(FileTypeEntity entity) {
		FileTypeDto result = new FileTypeDto();
		result.setCreateDate(entity.getCreateDate());
		result.setId(entity.getId());
		result.setTableName(entity.getTableName());
		result.setTypeName(entity.getTypeName());
		result.setUpdateDate(entity.getUpdateDate());
		return result;
	}

	@Override
	protected FileTypeEntity dtoToEntity(FileTypeDto dto) {
		FileTypeEntity result = new FileTypeEntity();
		result.setCreateDate(dto.getCreateDate());
		result.setId(dto.getId());
		result.setTableName(dto.getTableName());
		result.setTypeName(dto.getTypeName());
		result.setUpdateDate(dto.getUpdateDate());
		return result;
	}

	@Override
	public FileTypeDto getFileTypeByFileType(String name) {
		FileTypeEntity entity = fileTypeDao.getFileTypeByTypeName(name);
		FileTypeDto result = null;
		if (entity != null) {
			result = entityToDto(entity);
		}
		return result;
	}

	@Override
	public List<FileColumnDto> getFileColumnsByFileType(String fileType) {
		List<FileColumnDto> result = new ArrayList<FileColumnDto>();
		List<FileColumnEntity> dbResult = fileColumnDao
				.getFileColumnsOfFileType(fileType);
		for (FileColumnEntity entity : dbResult) {
			result.add(fileColumnEntityToDto(entity));
		}

		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateDbTable(String tableName,
			List<FileColumnDto> fileColumnsByFileType, InputStream inputStream,
			Long inversionUpdateId) {
		
		if (excelDao == null) {
			throw new UnsupportedOperationException(
					"excelDao bean not found. Please inject it to "
							+ this.getClass().getName());
		}

		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			cleanTrailingEmptyRows(sheet);
			excelDao.insertBatchExcel(tableName, fileColumnsByFileType, sheet,
					inversionUpdateId);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			throw new XlsException("Invalid file format", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new XlsException("IOException", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.ohiggins.service.FileTypeService#cleanTrailingEmptyRows(org
	 * .apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	public void cleanTrailingEmptyRows(Sheet sheet) {
		boolean stop = false;
		boolean nonBlankRowFound;
		short c;
		Row lastRow = null;
		Cell cell = null;

		while (stop == false) {
			nonBlankRowFound = false;
			lastRow = sheet.getRow(sheet.getLastRowNum());
			for (c = lastRow.getFirstCellNum(); c <= lastRow.getLastCellNum(); c++) {
				cell = lastRow.getCell(c);
				if (cell != null
						&& lastRow.getCell(c).getCellType() != Cell.CELL_TYPE_BLANK) {
					nonBlankRowFound = true;
				}
			}
			if (nonBlankRowFound == true) {
				stop = true;
			} else {
				sheet.removeRow(lastRow);
			}
		}
	}

}
