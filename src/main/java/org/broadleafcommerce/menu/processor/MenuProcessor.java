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

package org.broadleafcommerce.menu.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.service.MenuService;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.broadleafcommerce.presentation.dialect.AbstractBroadleafVariableModifierProcessor;
import org.broadleafcommerce.presentation.model.BroadleafTemplateContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * A Thymeleaf processor that will add a list of MenuItemDTOs to the model.
 *
 * It accepts a menuName or menuId. The precedence is that a menuId
 * will honored first, followed by a menuName.
 * An extension manager may override the resulting menu if configured to do so.
 *
 * @author bpolster
 */
@Component("blMenuProcessor")
@ConditionalOnTemplating
public class MenuProcessor extends AbstractBroadleafVariableModifierProcessor {

    private final static Log LOG = LogFactory.getLog(MenuProcessor.class);

    @Resource(name = "blMenuService")
    protected MenuService menuService;

    @Resource(name = "blMenuProcessorExtensionManager")
    protected MenuProcessorExtensionManager extensionManager;

    @Override
    public String getName() {
        return "menu";
    }

    @Override
    public int getPrecedence() {
        return 1000;
    }

    @Override
    public Map<String, Object> populateModelVariables(String tagName, Map<String, String> tagAttributes, BroadleafTemplateContext context) {
        String resultVar = tagAttributes.get("resultVar");
        String menuName = tagAttributes.get("menuName");
        String menuId = tagAttributes.get("menuId");

        final Menu menu;

        if (menuId != null) {
            menu = menuService.findMenuById(Long.parseLong(menuId));
        } else {
            menu = menuService.findMenuByName(menuName);
        }

        Map<String, Object> newModelVars = new HashMap<>();
        if (menu != null) {
            newModelVars.put(resultVar, menuService.constructMenuItemDTOsForMenu(menu));
            extensionManager.getProxy().addAdditionalFieldsToModel(tagName, tagAttributes, newModelVars, context);

            //Add linked data as attribute
            JSONArray schemaObjects = new JSONArray();
            try {
                for(MenuItem menuItem : menu.getMenuItems()) {
                    JSONObject navElement = new JSONObject();
                    navElement.put("@context", "http://schema.org/");
                    navElement.put("@type", "SiteNavigationElement");
                    navElement.put("url", menuItem.getActionUrl());
                    navElement.put("name", menuItem.getLabel());
                    schemaObjects.put(navElement);
                }
            } catch (JSONException je) {
                LOG.warn("A JSON error occurred while generating linked data for menu items", je);
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append("<script type=\"application/ld+json\">\n");
            buffer.append(schemaObjects.toString());
            buffer.append("\n</script>");
            newModelVars.put("menuLinkedData", buffer.toString());
        }

        return newModelVars;
    }
}
