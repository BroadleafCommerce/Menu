/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2023 Broadleaf Commerce
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

import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.service.LinkedDataService;
import org.broadleafcommerce.menu.service.MenuService;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
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
public class MenuProcessor implements MenuExpression {

    @Resource(name = "blMenuService")
    protected MenuService menuService;

    @Resource(name = "blMenuLinkedDataService")
    protected LinkedDataService linkedDataService;

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
    public Map<String, Object> getMenu(String menuId, String menuName) {

        final Menu menu;

        if (menuId != null) {
            menu = menuService.findMenuById(Long.parseLong(menuId));
        } else {
            menu = menuService.findMenuByName(menuName);
        }

        Map<String, Object> newModelVars = new HashMap<>();
        if (menu != null) {
            newModelVars.put("menuName", menuService.constructMenuItemDTOsForMenu(menu));
            extensionManager.getProxy().addAdditionalFieldsToModel(newModelVars, menuId, menuName);

            newModelVars.put("menuLinkedData", linkedDataService.getLinkedData(menu));
        }

        return newModelVars;
    }
}
