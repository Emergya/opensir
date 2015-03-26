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
 * 
 * Author: Luis Román Gutiérrez (lroman@emergya.com)
 */
package com.emergya.ohiggins.web;

import com.emergya.ohiggins.cmis.bean.AnalyticsData;
import com.emergya.ohiggins.dto.AnalyticsCategoryDto;
import com.emergya.ohiggins.dto.AnalyticsDataDto;
import com.emergya.ohiggins.dto.AnalyticsDataDownloadsDto;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.NivelTerritorialDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.services.AnalyticsCategoryService;
import com.emergya.ohiggins.services.AnalyticsDataDownloadsService;

import javax.annotation.Resource;

import org.springframework.aop.aspectj.AspectJAdviceParameterNameDiscoverer.AmbiguousBindingException;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author lroman
 */
@Repository
public class AnalyticsControllerHelperImpl implements AnalyticsControllerHelper {

    @Resource private UserAdminService userAdminService;
    
    @Resource
    private InstitucionService institutionService;
    @Resource
    private AnalyticsCategoryService analyticsCategoryService;
    
    @Resource
    private AnalyticsDataDownloadsService analyticsDownloadsService;
    
    @Resource
    private NivelTerritorialService geoContextService;

    @Override
    public void addDatumNameFields(AnalyticsData datum) {
        // We set the dto's name properties corresponding to ids.
        if (datum.getCategoryId() != null) {
        	try{
            AnalyticsCategoryDto categoryDto =
                    (AnalyticsCategoryDto) analyticsCategoryService.getById(datum.getCategoryId());
            datum.setCategoryName(categoryDto.getName());
        	}catch(Exception e){
        		datum.setCategoryName(AnalyticsCategoriesController.EMPTY_CATEGORY);
        	}
        } else {
            datum.setCategoryName(null);
        }

        if (datum.getInstitutionId() != null) {
            AuthorityDto authority = (AuthorityDto) institutionService.getById(datum.getInstitutionId());
            datum.setInstitutionName(authority.getAuthority());
        } else {
            datum.setInstitutionName(null);
        }
        
        if(datum.getGeoContextId()!=null) {
        	NivelTerritorialDto geoContextDto = geoContextService.getById(datum.getGeoContextId());
        	datum.setGeoContextName(geoContextDto.getName());
        } else {
        	datum.setGeoContextName(null);
        }
        
        AnalyticsDataDownloadsDto downloadsDto = 
        		analyticsDownloadsService.getByIdentifier(datum.getIdentifier());
        if(downloadsDto!=null) {
        	datum.setDownloads(downloadsDto.getDownloads());
        } else {
        	datum.setDownloads(0L);
        }
    }
    
    @Override
    public void addDatumFormProperties(
            AnalyticsDataDto datumDto, String submodule, Model model, WebRequest request) {
        UsuarioDto currentUser = userAdminService.obtenerUsuarioByUsername(
                request.getUserPrincipal().getName());

        model.addAttribute("IS_ADMIN", currentUser.isAdmin());
        model.addAttribute("module", "analitico");
        model.addAttribute("submodule", submodule);
        model.addAttribute("analyticsDataDto", datumDto);
        model.addAttribute("categories", analyticsCategoryService.getAll());
        model.addAttribute("institutions", institutionService.getAllOrderedByName());
        model.addAttribute("geoContexts", geoContextService.getZonesOrderByTypeDescNameAsc());
    }
    
}
