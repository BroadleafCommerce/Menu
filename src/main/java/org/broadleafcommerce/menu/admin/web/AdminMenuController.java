/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.menu.admin.web;

import org.broadleafcommerce.openadmin.web.controller.entity.AdminBasicEntityController;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Elbert Bautista (elbertbautista)
 */
@RequestMapping("/" + AdminMenuController.SECTION_KEY)
public class AdminMenuController extends AdminBasicEntityController {

    protected static final String SECTION_KEY = "menu";

    @Override
    protected String getSectionKey(Map<String, String> pathVars) {
        //allow external links to work for ToOne items
        if (super.getSectionKey(pathVars) != null) {
            return super.getSectionKey(pathVars);
        }
        return SECTION_KEY;
    }

    @Override
    @RequestMapping(value = "/{id}/{collectionField:.*}/add", method = RequestMethod.GET)
    public String showAddCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
                                        @PathVariable Map<String, String> pathVars,
                                        @PathVariable(value = "id") String id,
                                        @PathVariable(value = "collectionField") String collectionField,
                                        @RequestParam MultiValueMap<String, String> requestParams) throws Exception {
        String view = super.showAddCollectionItem(request, response, model, pathVars, id, collectionField, requestParams);
        model.addAttribute("additionalControllerClasses", "menu-item-form");

        return view;
    }

    @Override
    @RequestMapping(value = "/{id}/{collectionField:.*}/{collectionItemId}", method = RequestMethod.GET)
    public String showUpdateCollectionItem(HttpServletRequest request, HttpServletResponse response, Model model,
                                           @PathVariable  Map<String, String> pathVars,
                                           @PathVariable(value="id") String id,
                                           @PathVariable(value="collectionField") String collectionField,
                                           @PathVariable(value="collectionItemId") String collectionItemId) throws Exception {
        String view = super.showUpdateCollectionItem(request, response, model, pathVars, id, collectionField, collectionItemId);
        model.addAttribute("additionalControllerClasses", "menu-item-form");

        return view;
    }


}
