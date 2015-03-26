package com.emergya.chileIndica.dao.impl;

import org.springframework.util.Assert;

import com.emergya.chileIndica.dao.ChileIndicaRegionDatabase;

public class ChileIndicaRegionsDatabasesContextHolder {

	private static final ThreadLocal<ChileIndicaRegionDatabase> contextHolder = new ThreadLocal<ChileIndicaRegionDatabase>();
	
	public static void setChileIndicaRegionDatabase (ChileIndicaRegionDatabase chileIndicaRegionDatabase) {
		Assert.notNull(chileIndicaRegionDatabase, "Chile Indica Region Database cannot be null");
		contextHolder.set(chileIndicaRegionDatabase);
	}
	
	public static ChileIndicaRegionDatabase getChileIndicaRegionDatabase() {
		return (ChileIndicaRegionDatabase) contextHolder.get();
	}
	
	public static void clearChileIndicaRegionDatabase() {
		contextHolder.remove();
	}
}
