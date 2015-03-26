/*
 * Copyright (C) 2014 Emergya
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

package com.emergya.ohiggins.web;

import com.emergya.persistenceGeo.web.RestPersistenceGeoController;
import com.emergya.siradmin.invest.InvestmentUpdater;
import com.emergya.siradmin.invest.util.LlaveBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This controller allows an admin to trigger the update of data from ChileIndica.
 * 
 * @author lroman
 */
@Controller
public class ChileIndicaInversionUpdateController extends RestPersistenceGeoController{
    
    private static final Logger LOGGER = Logger.getLogger(ChileIndicaInversionUpdateController.class);
    
    @Resource
    private InvestmentUpdater updater;
    
    @RequestMapping(value = "/persistenceGeo/chileIndica/doUpdate")
	@ResponseBody
	public Map<String, Object> doUpdate(
            @RequestParam(value="firstYear", defaultValue = "2006") int firstYear) {
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        if (!isAdmin(secureRestRequest)) {
            result.put(SUCCESS, false);
            result.put(ROOT, "Invalid login!");
            result.put(RESULTS, 1);
            return result;
        }        
        
        Integer lastYear = Calendar.getInstance().get(Calendar.YEAR);

        List<LlaveBean> lista = new ArrayList<LlaveBean>();
        for (int year = firstYear; year <= lastYear; year++) {
            lista.addAll(updater.getExistingKeysInChileindica(year, 15));
        }

        updater.getWsDataAndUpdateDB(lista);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("");
            LOGGER.info("******************************************************");
            LOGGER.info("Resultado de la actualización:");
            for (LlaveBean llave : lista) {
                LOGGER.info(llave);
            }
            LOGGER.info("******************************************************");
            LOGGER.info("Actualización finalizada a a las "
                    + Calendar.getInstance().getTime());
        }
        
        result.put(SUCCESS, true);
        result.put(ROOT, lista);
        result.put(RESULTS, lista.size());
        
        return result;
        
                
    }
}
