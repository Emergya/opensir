package com.emergya.ohiggins.web.util;

import java.util.List;

import org.displaytag.properties.SortOrderEnum;

public class PaginatedList<ELEMENT> implements org.displaytag.pagination.PaginatedList {
	private List<ELEMENT> list;
	private int fullSize;
	private int objectsPerPage;
	private int pageNumber;

	public PaginatedList(
			List<ELEMENT> lista, int fullSize, 
			int objectsPerPage, int pageNumber) {
		this.list = lista;
		this.fullSize = fullSize;
		this.objectsPerPage = objectsPerPage;
		this.pageNumber = pageNumber;
	}

	@Override
	public int getFullListSize() {

		return fullSize;
	}

	@Override
	public List<ELEMENT> getList() {
		return list;
	}

	@Override
	public int getObjectsPerPage() {
		return objectsPerPage;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public String getSearchId() {
		return null;
	}

	@Override
	public String getSortCriterion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortOrderEnum getSortDirection() {
		// TODO Auto-generated method stub
		return null;
	}

}
