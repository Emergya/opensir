/*
 * CartograficoController
 * 
 * Copyright (C) 2013
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 * Authors:: Luis Román Gutiérrez (mailto:lroman@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.cmis.ASortOrder;
import com.emergya.ohiggins.cmis.AnalyticsDataCMISConnector;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.bean.AnalyticsData;
import com.emergya.ohiggins.cmis.bean.AnalyticsDataFilter;
import com.emergya.ohiggins.dto.AnalyticsDataDownloadsDto;
import com.emergya.ohiggins.dto.AnalyticsDataDto;
import com.emergya.ohiggins.dto.AnalyticsDataTagDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.NivelTerritorialService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.services.AnalyticsCategoryService;
import com.emergya.ohiggins.services.AnalyticsDataDownloadsService;
import com.emergya.ohiggins.services.AnalyticsDataTagService;
import com.emergya.ohiggins.web.util.PaginatedList;
import com.emergya.ohiggins.web.validators.AnalyticsDataDtoValidator;

@Controller
public class AnalyticsDataController extends AbstractController {

    private static final long serialVersionUID = -3066162947438190015L;
    public final static String MODULE = "analitico";
    public final static String SUB_MODULE = "datos";
    
    public final static String TAG_SEPARATOR=",";
    
    @Resource
    private AnalyticsDataCMISConnector cmisConnector;
    
    @Resource
    private InstitucionService institutionService;
    @Resource
    private AnalyticsCategoryService analyticsCategoryService;
    
    
    @Resource
    private NivelTerritorialService geoContextService;
    
    @Resource
    private UserAdminService userService;
    
    @Resource
    private AnalyticsControllerHelper analyticsControllerHelper;
    
    @Resource 
    private AnalyticsDataDownloadsService analyticsDownloadsService;
    
    @Resource
    private AnalyticsDataTagService analyticsTagService;

    @RequestMapping(value = "/admin/analitico/datos")
    public String viewData(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page,
            Model model, WebRequest webRequest) {

        isLogate(webRequest);

        AnalyticsDataFilter filter = new AnalyticsDataFilter();       

        doSearch(filter, page, size, model, webRequest);

        return "admin/analytics/data";
    }
    
    @RequestMapping(value = "/admin/analitico/busquedaDatos")
    public String dataSearch(@Valid AnalyticsDataFilter dataFilter, 
    		@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page,
            Model model, WebRequest webRequest) {
    	
    	isLogate(webRequest);

        doSearch(dataFilter, page, size, model, webRequest);
        
        model.addAttribute("searchFormCtrClass", "");
    	
    	return "admin/analytics/data";    	
    }

    protected List<AnalyticsData> doSearch(AnalyticsDataFilter filter, int page, int size, Model model, WebRequest webRequest) {    	
    	 UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                 webRequest.getUserPrincipal().getName());

         model.addAttribute("IS_ADMIN", currentUser.isAdmin());

         calculatePagination(model);
    	
    	 ADocumentState[] stateFilter;

         if (currentUser.isAdmin()) {
             stateFilter = new ADocumentState[]{ADocumentState.ACEPTADA, ADocumentState.LEIDA};
         } else {
             filter.setInstitutionId(currentUser.getAuthorityId());
             model.addAttribute("disableInstitutionSelect","true");
             stateFilter = new ADocumentState[]{ADocumentState.PRIVATE};
         }
		
    	 Long numElements = cmisConnector.countFilteredDocuments(filter, stateFilter);

         List<AnalyticsData> dataInPage =
                 (List<AnalyticsData>) cmisConnector.getFilteredDocuments(
                 filter, (long) (page - 1) * size, page * size, "name", ASortOrder.ASC, stateFilter);

         for (AnalyticsData datum : dataInPage) {
             analyticsControllerHelper.addDatumNameFields(datum);
         }

         PaginatedList<AnalyticsData> result = new PaginatedList<AnalyticsData>(
                 dataInPage, (int) numElements.longValue(), size, page);

         model.addAttribute("searchFilter", filter);
         model.addAttribute("data", result);
         
         model.addAttribute("categories", analyticsCategoryService.getAll());
         model.addAttribute("institutions", institutionService.getAllOrderedByName());
         model.addAttribute("geoContexts", geoContextService.getZonesOrderByTypeDescNameAsc());
         
         model.addAttribute("searchFormCtrClass", "hidden");
         
         return dataInPage;
	}

	@RequestMapping(value = "/admin/analitico/nuevoDato")
    public String newDatum(Model model, WebRequest webRequest) {
        isLogate(webRequest);

        analyticsControllerHelper.addDatumFormProperties(
                new AnalyticsDataDto(), SUB_MODULE, model, webRequest);

        return "admin/analytics/datumForm";
    }

    @RequestMapping(value = "/admin/analitico/buscaTag")
    @ResponseBody()
    public List<String> searchAnalyticsTags(@RequestParam String searchTerm) {

        return analyticsTagService.searchTags(searchTerm);
    }

   

    @RequestMapping(value = "/admin/analitico/guardarDato", method = RequestMethod.POST)
    public String saveDatum(
			@Valid AnalyticsDataDto datumDto, 
            BindingResult result, Model model, WebRequest request)
            throws InterruptedException {

        isLogate(request);
        
        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                request.getUserPrincipal().getName());
        
        AnalyticsDataDtoValidator validator = new AnalyticsDataDtoValidator(currentUser.isAdmin());
        validator.validate(datumDto, result);
        if (result.hasErrors()) {
        	analyticsControllerHelper.addDatumFormProperties(datumDto, SUB_MODULE, model, request);
            return "admin/analytics/datumForm";
        }

        boolean editing = StringUtils.isNotBlank(datumDto.getIdentifier());

        AnalyticsData datum;
        if (editing) {
        	datum = cmisConnector.getDocument(datumDto.getIdentifier());
            datumDto.toDatum(datum);
            // We just update the document.
            cmisConnector.updateDocument(datum);

            // We wait the changes to "solidify" in alfresco.
            Thread.sleep(3000);
        } else {
            

            datum = new AnalyticsData();
            datumDto.toDatum(datum);

            datum.setInstitutionId(currentUser.getAuthorityId());
            datum.setState(ADocumentState.PRIVATE);

            // We need to handle the file upload.
            File tmpFile;
            try {
                tmpFile = File.createTempFile("uploadedData", ".tmp");
                datumDto.getFile().transferTo(tmpFile);
            } catch (IOException ex) {
                // Shouldn't happen.
                throw new RuntimeException(ex);
            }

            cmisConnector.createDocument(datum, tmpFile);
        }
        
        // We only add tagcloud information for public data (Fix for #86890)
        if(datum.getState().isPublished()) {
            // We save the tags of the document so we can create the cloud tag
        	analyticsTagService.saveTagsForIdentifier(
        			datum.getIdentifier(), datumDto.getTags().split(TAG_SEPARATOR));
        }        

        // We wait until the document is actually avalaible.
        AnalyticsData document = null;
        while (document == null) {
            try {
                document = cmisConnector.getDocument(datum.getIdentifier());
            } catch (RuntimeException e) {
                Thread.sleep(200);
            }
        }

        return requestReturn("redirect:/admin/analitico/datos", "success");
    }

    @RequestMapping(value = "/admin/analitico/descarga/{ident}", method = RequestMethod.GET)
    public String downloadDocument(
    		WebRequest webRequest,
            HttpServletRequest request, HttpServletResponse response,
            Model model, @PathVariable("ident") String ident) {
    	
    	isLogate(webRequest);

        // Authentication u = getAuthentication(webRequest);
        try {
            File file = cmisConnector.downloadDocument(ident);
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + file.getName() + "\"");
            response.setContentType(cmisConnector.getMimeType(ident));

            FileCopyUtils.copy(new FileInputStream(file),
                    response.getOutputStream());
            
            file.delete();
        } catch (Exception t) {
            Logger.getLogger(this.getClass()).error("Error descargando " + ident, t);
        }

        return null;
    }

    /**
     *
     * @param webRequest
     * @param model
     * @param datumIdentifier
     * @return
     */
    @RequestMapping(value = "/admin/analitico/borrarDato/{ident}", method = RequestMethod.GET)
    public String deleteDatum(WebRequest webRequest, Model model,
            @PathVariable("ident") String datumIdentifier)
            throws InterruptedException {

        isLogate(webRequest);

        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(datumIdentifier);
        } catch (RuntimeException e) {
        }

        if (datum == null) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al recuperar el dato.");
        }

        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                webRequest.getUserPrincipal().getName());

        if (!currentUser.isAdmin() && !currentUser.getAuthorityId().equals(datum.getInstitutionId())) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al recuperar el dato.");
        }

        try {
        	 // We clean the downloads for the document before deleting it.
            AnalyticsDataDownloadsDto downloads = analyticsDownloadsService.getByIdentifier(datumIdentifier);
            if(downloads!=null) {
            	analyticsDownloadsService.delete(downloads);
            }
        
            // We delete the tags when the datum is removed (#86890).
            analyticsTagService.deleteAllForIdentifier(datumIdentifier);
        	
            cmisConnector.deleteDocument(datum);

        } catch (Throwable e) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al eliminar el dato.");
        }

       
        Thread.sleep(4000);
       

        return requestReturn("redirect:/admin/analitico/datos", "success");
    }

    @RequestMapping(value = "/admin/analitico/actualizarDato/{ident}", method = RequestMethod.GET)
    public String editDatum(WebRequest webRequest, Model model,
            @PathVariable("ident") String datumIdentifier) {


        isLogate(webRequest);

        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(datumIdentifier);
        } catch (RuntimeException e) {
        	Logger.getLogger(this.getClass()).error("Exception retrieving datum", e);
        }

        if (datum == null) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al recuperar el dato.");
        }

        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                webRequest.getUserPrincipal().getName());

        if (!currentUser.isAdmin() && !currentUser.getAuthorityId().equals(datum.getInstitutionId())) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al recuperar el dato.");
        }

        analyticsControllerHelper.addDatumFormProperties(
                new AnalyticsDataDto(datum), SUB_MODULE, model, webRequest);

        return "admin/analytics/datumForm";
    }

    @Override
    protected void copyDefaultModel(boolean update, Model model) {
        // TODO Auto-generated method stub		
        model.addAttribute(MODULE_KEY, MODULE);
        model.addAttribute(SUBMODULE_KEY, SUB_MODULE);
        model.addAttribute("IS_ADMIN", true);
    }

    @Override
    protected String getAllSubTabs() {
        return TabsByModule.GENERAL_SUBTABS;
    }

    @Override
    protected int getSelectedSubTab() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected String getDefaultPaginationUrl() {
        return "analitico/datos";
    }

    @Override
    protected String getActiveModule() {
        return MODULE;
    }

    @Override
    protected String getActiveSubModule() {
        return SUB_MODULE;
    }
}
