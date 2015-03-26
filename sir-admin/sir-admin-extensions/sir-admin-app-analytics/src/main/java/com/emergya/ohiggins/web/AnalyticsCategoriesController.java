/*
 * CartograficoController
 * 
 * Copyright (C) 2012
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

import com.emergya.ohiggins.cmis.AnalyticsDataCMISConnector;
import com.emergya.ohiggins.cmis.bean.AnalyticsData;
import com.emergya.ohiggins.cmis.bean.AnalyticsDataFilter;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.emergya.ohiggins.dto.AnalyticsCategoryDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.services.AnalyticsCategoryService;
import com.emergya.ohiggins.web.util.PaginatedList;

@Controller
public class AnalyticsCategoriesController extends AbstractController {

    private static final long serialVersionUID = 9094715608104607837L;
    public final static String MODULE = "analitico";
    public final static String SUB_MODULE = "categorias";
    @Resource
    private AnalyticsCategoryService analyticsCategoryService;
    @Resource
    private AnalyticsDataController analyticsDataController;
    @Resource
    private AnalyticsDataCMISConnector cmisConnector;
    
    //TODO: i18n
    public static final String EMPTY_CATEGORY = "";
    public static final String NUM_DOCS = "${docs}";
    public static final String DOCS_WITHOUT_CATEGORY = "Se ha borrado correctamente la caregría, dejando " + NUM_DOCS + " documentos sin categoría";


    @RequestMapping(value = "/admin/analitico/categorias")
    public String viewCategories(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "1") int page,
            Model model, WebRequest webRequest) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);
        model.addAttribute("IS_ADMIN", true);

        calculatePagination(model);

        // first = 0;numElements = institucionService.getResults();
        Long numElements = analyticsCategoryService.getResults();

        List<AnalyticsCategoryDto> categoriesInPage =
                (List<AnalyticsCategoryDto>) analyticsCategoryService.getOrdered((page - 1) * size, page * size, "name", true);

        PaginatedList<AnalyticsCategoryDto> result = new PaginatedList<AnalyticsCategoryDto>(
                categoriesInPage, (int) numElements.longValue(), size, page);

        model.addAttribute("categories", result);

        return "admin/analytics/categories";
    }

    @RequestMapping(value = "/admin/analitico/nuevaCategoria")
    public String newCategory(Model model, WebRequest webRequest) {
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        copyDefaultModel(model);

        model.addAttribute("analyticsCategoryDto", new AnalyticsCategoryDto());

        return "admin/analytics/categoryForm";
    }

    @RequestMapping(value = "/admin/analitico/editarCategoria/{id}")
    public String editCategory(@PathVariable Long id, Model model, WebRequest webRequest) {
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        copyDefaultModel(model);

        AnalyticsCategoryDto category = (AnalyticsCategoryDto) analyticsCategoryService.getById(id);

        model.addAttribute("analyticsCategoryDto", category);

        return "admin/analytics/categoryForm";
    }

    @RequestMapping(value = "/admin/analitico/guardarCategoria")
    public String saveCategory(@Valid AnalyticsCategoryDto categoryDto,
            BindingResult bindingResult, Model model, WebRequest webRequest) {
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);
        copyDefaultModel(model);

        if (bindingResult.hasErrors()) {
            model.addAttribute("analyticsCategoryDto", categoryDto);
            return "admin/analytics/categoryForm";
        }

        boolean editing = categoryDto.getId() != null;
        try {
            if (editing) {
                analyticsCategoryService.update(categoryDto);
            } else {
                analyticsCategoryService.create(categoryDto);
            }
        } catch (RuntimeException e) {
            return requestReturn("redirect:/admin/analitico/categorias", "Ocurrió un error al guardar la categoría");
        }

        return requestReturn("redirect:/admin/analitico/categorias", "success");
    }
    
    @RequestMapping(value = "/admin/analitico/borrarCategoria/{id}")
    public String deleteCategory(@PathVariable Long id, Model model, WebRequest webRequest) {
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        copyDefaultModel(model);

        try {

            AnalyticsCategoryDto category = (AnalyticsCategoryDto) analyticsCategoryService.getById(id);
            String msg;
            
            if(category != null){
            	msg = "success";
            	AnalyticsDataFilter filter = new AnalyticsDataFilter();
                filter.setCategoryId(id);
                List<AnalyticsData> documents = analyticsDataController.doSearch(filter, 1, Integer.MAX_VALUE, model, webRequest);
                if(documents !=null
                		&& !documents.isEmpty()){
                	for(AnalyticsData datum: documents){
                		datum.setCategoryId(null);
                		datum.setCategoryName(EMPTY_CATEGORY);
                		cmisConnector.updateDocument(datum);
                	}
                	msg = DOCS_WITHOUT_CATEGORY.replaceAll(DOCS_WITHOUT_CATEGORY, documents.size() + "");
                }
                analyticsCategoryService.delete(category);
            }else{
            	msg = "Categoría desconocida";
            }
            
            return requestReturn("redirect:/admin/analitico/categorias", msg);
            
        } catch (RuntimeException e) {
            return requestReturn("redirect:/admin/analitico/categorias", "Ocurrió un error al borrar la categoría");
        }
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
        return "admin/analitico/categorias";
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
