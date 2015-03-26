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

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.cmis.ASortOrder;
import com.emergya.ohiggins.cmis.AnalyticsDataCMISConnector;
import com.emergya.ohiggins.cmis.bean.AnalyticsData;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.dto.AnalyticsDataDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.services.AnalyticsDataTagService;

import static com.emergya.ohiggins.web.AbstractController.MODULE_KEY;
import static com.emergya.ohiggins.web.AbstractController.SUBMODULE_KEY;
import com.emergya.ohiggins.web.util.PaginatedList;
import com.emergya.ohiggins.web.validators.AnalyticsDataDtoValidator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AnalyticsRequestsController extends AbstractController {

    private static final long serialVersionUID = -3066162947438190015L;
    public final static String MODULE = "analitico";
    public final static String SUB_MODULE = "solicitudesPublicacion";
    
    private final static String TAG_SEPARATOR = ",";
    
    @Resource
    private AnalyticsDataCMISConnector cmisConnector;
    @Resource
    private UserAdminService userService;
    @Resource
    private AnalyticsControllerHelper analyticsControllerHelper;
    
    @Resource
    private AnalyticsDataTagService analyticsTagService;

    @RequestMapping(value = "/admin/analitico/solicitudesPublicacion")
    public String viewRequests(@RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page,
            Model model, WebRequest webRequest) {

        isLogate(webRequest);

        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                webRequest.getUserPrincipal().getName());

        boolean isAdmin = currentUser.isAdmin();
        model.addAttribute("IS_ADMIN", isAdmin);

        calculatePagination(model);

        Long numElements;

        List<AnalyticsData> requestsInPage;

        if (isAdmin) {
             numElements = cmisConnector.countPendingRequests();
            requestsInPage = (List<AnalyticsData>) cmisConnector.getPagedPendingRequests(
                    (long) (page - 1) * size, page * size, "name", ASortOrder.ASC);
        } else {
            numElements = cmisConnector.countUserRequests(currentUser.getAuthorityId().toString());
            requestsInPage = (List<AnalyticsData>) cmisConnector.getPagedUserRequests(
                    currentUser.getAuthorityId().toString(),
                    (long) (page - 1) * size, page * size, "name", ASortOrder.ASC);
        }




        for (AnalyticsData datumRequest : requestsInPage) {
            analyticsControllerHelper.addDatumNameFields(datumRequest);
        }

        PaginatedList<AnalyticsData> result = new PaginatedList<AnalyticsData>(
                requestsInPage, (int) numElements.longValue(), size, page);

        model.addAttribute("requests", result);

        return "admin/analytics/requests";
    }
    
    @RequestMapping(value = "/admin/analitico/verSolicitud/{identifier}")
    public String viewRequest(
    		@PathVariable("identifier") String identifier,
            Model model, WebRequest webRequest) {

    	decideIfIsUserInRole(ConstantesPermisos.ADMIN);
    	
    	
    	 AnalyticsData existingDatum = null;
         try {
             existingDatum = cmisConnector.getDocument(identifier);
         } catch (RuntimeException e) {
         }

         if (existingDatum == null) {
             // An error happened
             return requestReturn(
                     "redirect:/admin/analitico/solicitudesPublicacion",
                     "Ocurrió un error al rechazar.");
         }
         
         analyticsControllerHelper.addDatumFormProperties(
        		 new AnalyticsDataDto(existingDatum),getActiveSubModule(), model, webRequest);        

        return "admin/analytics/viewRequestForm";
    }

    @RequestMapping(value = "/popup/analitico/verRechazo/{identifier}")
    public String prepareRejection(Model model, WebRequest webRequest,
            @PathVariable("identifier") String identifier) {

        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(identifier);
        } catch (RuntimeException e) {
        }

        if (datum == null) {
            // A problem retrieving it or wasn't found.                
            return requestReturn("redirect:/admin/analitico/solicitudesPublicación", "Ocurrió un error recuperando el dato.");
        }


        model.addAttribute("datumRequest", datum);


        return "admin/analytics/rejectRequestDialog";
    }

    @RequestMapping(value = "/popup/analitico/verRespuesta/{identifier}", method = RequestMethod.GET)
    public String viewRequestAnswer(
            WebRequest webRequest,
            Model model, @PathVariable("identifier") String identifier) {

        isLogate(webRequest);

        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(identifier);
        } catch (RuntimeException e) {
        }

        if (datum == null) {
            // A problem retrieving it or wasn't found.                
            return requestReturn(
                    "redirect:/admin/analitico/solicitudesPublicación",
                    "Ocurrió un error recuperando el dato.");
        }

        String stateLabel = "";
        DateFormat dFmt = new SimpleDateFormat("dd/MM/yy 'a las ' HH:mm");
        switch (datum.getState()) {
            case ACEPTADA:
                stateLabel = String.format("fue aceptada el %s",
                        dFmt.format(datum.getRequestAnswerDate().getTime()));
                break;
            case RECHAZADA:
                stateLabel = String.format("fue rechazada el %s",
                        dFmt.format(datum.getRequestAnswerDate().getTime()));
                break;
            case PENDIENTE:
                stateLabel = "está pendiente de revisión";
                break;
            default:
                throw new IllegalStateException("Not supported publication state!");
        }


        model.addAttribute("stateLabel", stateLabel);
        model.addAttribute("datumRequest", datum);


        return "admin/analytics/stateDialog";
    }

    @RequestMapping(value = "/admin/analitico/rechazarSolicitud")
    public String rejectRequest(@Valid AnalyticsData datumRequest,
            BindingResult bindingResult, Model model, WebRequest webRequest) throws InterruptedException {
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        String answer = datumRequest.getRequestAnswer();

        if (org.apache.commons.lang3.StringUtils.isBlank(answer)) {
            // We redirect because we are in a popup dialog so we can't show errors properly.
            return requestReturn("redirect:/admin/analitico/solicitudesPublicacion", "Ocurrió un error al rechazar.");
        }

        AnalyticsData existingDatum = null;
        try {
            existingDatum = cmisConnector.getDocument(datumRequest.getIdentifier());
        } catch (RuntimeException e) {
        }

        if (existingDatum == null) {
            // An error happened
            return requestReturn(
                    "redirect:/admin/analitico/solicitudesPublicacion",
                    "Ocurrió un error al rechazar.");
        }

        existingDatum.setRequestAnswer(answer);
        existingDatum.setRequestAnswerDate(Calendar.getInstance());

        existingDatum.setState(ADocumentState.RECHAZADA);

        Long countBeforeChange = cmisConnector.countPendingRequests();

        while (countBeforeChange.equals(cmisConnector.countPendingRequests())) {
            cmisConnector.updateDocument(existingDatum);
            Thread.sleep(200);
        }

        return requestReturn("redirect:/admin/analitico/solicitudesPublicacion", "success");
    }
    
    @RequestMapping(value = "/admin/analitico/aceptarPublicacion/{identifier}")
    public String acceptRequest(
            @PathVariable("identifier") String identifier,
            Model model, WebRequest webRequest) {
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        AnalyticsData existingDatum = null;
        try {
            existingDatum = cmisConnector.getDocument(identifier);
        } catch (RuntimeException e) {
        }

        if (existingDatum == null) {
            // An error happened
            return requestReturn(
                    "redirect:/admin/analitico/solicitudesPublicacion",
                    "Ocurrió un error al rechazar.");
        }

        existingDatum.setRequestAnswerDate(Calendar.getInstance());
        
        // We save the datum's tags when the datum is published. 
        analyticsTagService.saveTagsForIdentifier(identifier, existingDatum.getTags().split(TAG_SEPARATOR));

        existingDatum.setState(ADocumentState.ACEPTADA);

        Long countBeforeChange = cmisConnector.countPendingRequests();

        while (countBeforeChange == cmisConnector.countPendingRequests()) {
            cmisConnector.updateDocument(existingDatum);
        }

        return requestReturn("redirect:/admin/analitico/solicitudesPublicacion", "success");
    }

    @RequestMapping(value = "/admin/analitico/publicarSolicitudModificada", method = RequestMethod.POST)
    public String publishModifiedRequest(
            @Valid AnalyticsDataDto datumDto, 
            BindingResult result, Model model, WebRequest request)
            throws InterruptedException {

    	decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        AnalyticsDataDtoValidator validator = new AnalyticsDataDtoValidator(true);
        validator.validate(datumDto, result);
        if (result.hasErrors()) {
            analyticsControllerHelper.addDatumFormProperties(datumDto, SUB_MODULE, model, request);
            return "/admin/analytics/viewRequestForm";
        }

        AnalyticsData datum = cmisConnector.getDocument(datumDto.getIdentifier());
        datumDto.toDatum(datum);
        
        // We set the publicatoin
        datum.setRequestAnswerDate(Calendar.getInstance());
        datum.setState(ADocumentState.ACEPTADA);
        
        // We just update the document.
        cmisConnector.updateDocument(datum);

        // We wait the changes to "solidify" in alfresco.
        Thread.sleep(4000);        
        
        // We save the tags of the document for count purposes.
        analyticsTagService.saveTagsForIdentifier(datumDto.getIdentifier(), datumDto.getTags().split(","));

        // We wait until the document is actually avalaible.
        AnalyticsData document = null;
        while (document == null) {
            try {
                document = cmisConnector.getDocument(datum.getIdentifier());
            } catch (RuntimeException e) {
                Thread.sleep(200);
            }
        }

        return requestReturn("redirect:/admin/analitico/solicitudesPublicacion", "success");
    }

    

    @RequestMapping("/admin/analitico/cambiaEstadoRespuesta")
    protected String handleAnswerRead(
            AnalyticsData datumRequest,
            BindingResult bindingResult, Model model, WebRequest webRequest) throws InterruptedException {
        
        isLogate(webRequest);

        AnalyticsData existingDatum = null;
        try {
            existingDatum = cmisConnector.getDocument(datumRequest.getIdentifier());
        } catch (RuntimeException e) {
        }

        if (existingDatum == null) {
            return requestReturn("redirect:/admin/analitico/solicitudesPublicacion", "Error al cambiar el estado.");
        }
        
        
        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                webRequest.getUserPrincipal().getName());
        Long numPriorElements = cmisConnector.countUserRequests(currentUser.getAuthorityId().toString());

        boolean updateDone = true;
        switch (existingDatum.getState()) {
            case RECHAZADA:
                // In this case we remove the request!
                cmisConnector.deleteDocument(existingDatum);
                break;

            case ACEPTADA:
                existingDatum.setState(ADocumentState.LEIDA);
                cmisConnector.updateDocument(existingDatum);
                break;

            case PENDIENTE:
                // We do nothing;
            	updateDone = false;
                break;
            default:
                throw new IllegalStateException("Unsupported publication state!");
        }
        

        if(updateDone) {
	        int i=0;
	        while (cmisConnector.countUserRequests(currentUser.getAuthorityId().toString())==numPriorElements && i<60){
	        	Thread.sleep(1000);
	        	i++;
	        }
        }

        return "redirect:/admin/analitico/solicitudesPublicacion";
    }
    
    @RequestMapping("/admin/analitico/solicitarPublicacion/{ident}")
    public String createPublicationRequest(
            WebRequest webRequest, Model model,
            @PathVariable("ident") String identifier) {
        
        isLogate(webRequest);
        
        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(identifier);
        } catch(RuntimeException e) {
            
        }
        
        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                webRequest.getUserPrincipal().getName());
        
        if(datum == null 
                || !datum.getInstitutionId().equals(currentUser.getAuthorityId())
                || datum.getState() != ADocumentState.PRIVATE) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al recuperar el dato.");
        }
        
        
        analyticsControllerHelper.addDatumFormProperties(new AnalyticsDataDto(datum), identifier, model, webRequest);
        
        return "admin/analytics/requestForm";
    }
    
    @RequestMapping(value = "/admin/analitico/editarSolicitudPublicacion/{ident}")
    public String editPublicationRequest(
            WebRequest webRequest, Model model,
            @PathVariable("ident") String identifier) {
        
        isLogate(webRequest);
        
        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(identifier);
        } catch(RuntimeException e) {
            
        }
        
        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
                webRequest.getUserPrincipal().getName());
        
        if(datum == null 
                || !datum.getInstitutionId().equals(currentUser.getAuthorityId())
                || datum.getState()!=ADocumentState.RECHAZADA) {
            return requestReturn("redirect:/admin/analitico/datos", "Ocurrió un error al recuperar el dato.");
        }
        
        
        analyticsControllerHelper.addDatumFormProperties(new AnalyticsDataDto(datum), identifier, model, webRequest);
        
        return "admin/analytics/requestForm";
    }
    
    @RequestMapping(value = "/admin/analitico/guardarSolicitudPublicacion", method = RequestMethod.POST)
    public String savePublicationRequest(
    		AnalyticsDataDto requestDto,
            BindingResult result,
            WebRequest webRequest, Model model) throws InterruptedException {
        
        isLogate(webRequest);
        
        UsuarioDto currentUser = userService.obtenerUsuarioByUsername(
        		webRequest.getUserPrincipal().getName());
        
        AnalyticsDataDtoValidator validator = new AnalyticsDataDtoValidator(true);
        requestDto.setInstitutionId(currentUser.getAuthorityId());
        validator.validate(requestDto, result);
        if(result.hasErrors()) {
        	analyticsControllerHelper.addDatumFormProperties(requestDto, requestDto.getIdentifier(), model, webRequest);            
            return "admin/analytics/requestForm";
        }
        
        AnalyticsData datum = null;
        try {
            datum = cmisConnector.getDocument(requestDto.getIdentifier());
        } catch(RuntimeException e) {
            
        }
       
        if(datum==null || !datum.getInstitutionId().equals(currentUser.getAuthorityId())) {
            return requestReturn("redirect:/admin/analitico/solicitudesPublicacion","Ocurrió un error al recuperar el dato.");
        }
        
        // Is new if the document is in private state. This mean we are not editing
        // a rejected publication request.
        boolean isNew = datum.getState()== ADocumentState.PRIVATE;
        
        if(isNew) {
            // We create a new instance because we don't want to work on the
            // private data directly.
            datum = new AnalyticsData();            
        }
        
        datum.setState(ADocumentState.PENDIENTE);
        datum.setRequestDate(Calendar.getInstance());
        datum.setRequestAnswer(null);
        datum.setRequestAnswerDate(null);
        
        requestDto.toDatum(datum);
        
        if(isNew) {
            datum.setInstitutionId(currentUser.getAuthorityId());
            
            // We need to copy the original file and send it again with the new document.
            File documentFile = cmisConnector.downloadDocument(requestDto.getIdentifier());
            
            Long requests= cmisConnector.countPendingRequests();
            
            cmisConnector.createDocument(datum, documentFile);
            
            while(requests.equals(cmisConnector.countPendingRequests())) {
                Thread.sleep(500);
            }
            
            documentFile.delete();
        } else {
            
            // We just update the data.
            cmisConnector.updateDocument(datum);
            Thread.sleep(5000);
        }
        
        
        return requestReturn("redirect:/admin/analitico/solicitudesPublicacion","success");
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
