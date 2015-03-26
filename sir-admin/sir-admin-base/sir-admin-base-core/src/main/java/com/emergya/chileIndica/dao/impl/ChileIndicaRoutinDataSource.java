package com.emergya.chileIndica.dao.impl;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class ChileIndicaRoutinDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ChileIndicaRegionsDatabasesContextHolder.getChileIndicaRegionDatabase();
	}

}
