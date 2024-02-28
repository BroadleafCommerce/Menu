/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2024 Broadleaf Commerce
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

import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.service.LinkedDataService;
import org.broadleafcommerce.menu.service.MenuService;
import org.broadleafcommerce.presentation.condition.ConditionalOnTemplating;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component("blMenuLinkedDataProcessor")
@ConditionalOnTemplating
public class MenuLinkedDataProcessor implements BroadleafVariableExpression {

    @Resource(name = "blMenuService")
    protected MenuService menuService;

    @Resource(name = "blMenuLinkedDataService")
    protected LinkedDataService linkedDataService;

    @Override
    public String getName() {
        return "menuLinkedData";
    }

    public String getMenuLinkedData(String menuName, String menuId) {
        final Menu menu;

        if (menuId != null) {
            menu = menuService.findMenuById(Long.parseLong(menuId));
        } else {
            menu = menuService.findMenuByName(menuName);
        }

        return menu != null ? this.linkedDataService.getLinkedData(menu) : "";
    }
}
