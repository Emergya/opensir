/*
 * Copyright (C) 2013 Emergya
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.emergya.ohiggins.scheduling;

import com.emergya.ohiggins.service.SHP2PostgisService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 
 * @author lroman
 */
public class CleanLayerCacheTask {
    
    private static final Log LOG = LogFactory
			.getLog(CleanLayerCacheTask.class);

    private boolean justTest;
    @Autowired
    private SHP2PostgisService migrateSHP;

    /**
     * Clean temporary layers
     */   
    @Scheduled(cron = "0 15 04 * * ? *")
    public void doClean() {
	
	LOG.info("Comencing cleaning of temporal and usused layers"+ (isJustTest()?" (ONLY TEST)":""));
	List<String> removedLayers = migrateSHP.cleanUnusedLayers(isJustTest());
	
	String endMsg = isJustTest()?"Layers choosen for deletion: ":"Removed layers: ";
	if(!removedLayers.isEmpty()){
	    LOG.info(endMsg + StringUtils.join(removedLayers, ", "));
	} else {
	    LOG.info(endMsg + "None");
	}
    }

    /**
     * @return the justTest
     */
    public boolean isJustTest() {
	return justTest;
    }

    /**
     * @param justTest the justTest to set
     */
    public void setJustTest(boolean justTest) {
	this.justTest = justTest;
    }

   
}
