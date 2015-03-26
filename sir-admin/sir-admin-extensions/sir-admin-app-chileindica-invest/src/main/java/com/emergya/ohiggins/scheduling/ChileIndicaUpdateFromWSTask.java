package com.emergya.ohiggins.scheduling;

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


import com.emergya.siradmin.invest.InvestmentUpdater;
import com.emergya.siradmin.invest.util.LlaveBean;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author lroman
 */
@Component
public class ChileIndicaUpdateFromWSTask {

    private static final Logger LOGGER = Logger.getLogger(ChileIndicaUpdateFromWSTask.class);
    private Integer firstYear;
    
    @Resource
    private InvestmentUpdater updater;

    //@Scheduled(fixedDelay = 5000)
    public void doUpdate() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("");
            LOGGER.info("******************************************************");
            LOGGER.info("Comenzando actualización WS Chile INDICA  a a las "
                    + Calendar.getInstance().getTime());
        }


        Integer lastYear = Calendar.getInstance().get(Calendar.YEAR);


        List<LlaveBean> lista = new ArrayList<LlaveBean>();
        for (int year = getFirstYear(); year <= lastYear; year++) {
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
    }

    /**
     * @return the firstYear
     */
    public Integer getFirstYear() {
        return firstYear;
    }

    /**
     * @param firstYear the firstYear to set
     */
    public void setFirstYear(Integer firstYear) {
        this.firstYear = firstYear;
    }
}
