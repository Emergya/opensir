package com.emergya.ohiggins.service;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.emergya.ohiggins.dto.FileColumnDto;
import com.emergya.ohiggins.dto.FileTypeDto;

public interface FileTypeService {
	public FileTypeDto getFileTypeByFileType(String name);

	public List<FileColumnDto> getFileColumnsByFileType(String fileType);

	public void updateDbTable(String tableName,
			List<FileColumnDto> fileColumnsByFileType, InputStream inputStream,
			Long inversionUpdateId);

	/**
	 * Delete trailing empty rows of a Excel Sheet.
	 * 
	 * @param sheet
	 */
	public void cleanTrailingEmptyRows(Sheet sheet);

}
