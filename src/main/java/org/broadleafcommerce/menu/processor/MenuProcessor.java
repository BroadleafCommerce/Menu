/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2014 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.broadleafcommerce.menu.processor;

import org.broadleafcommerce.common.web.dialect.AbstractModelVariableModifierProcessor;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.service.MenuService;
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;


import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will add the desired menu to the model. 
 * 
 * It accepts a menuName, menuId, and/or menuItem.    The precedence is that a menuItemId
 * will honored first, followed by a menuId, and finally by the menuName.
 * 
 * @author bpolster
 */
@Component("blMenuProcessor")
public class MenuProcessor extends AbstractModelVariableModifierProcessor {

    @Resource(name = "blMenuService")
    protected MenuService menuService;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public MenuProcessor() {
        super("menu");
    }

    @Override
    public int getPrecedence() {
        return 1000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {
        String resultVar = element.getAttributeValue("resultVar");
        String menuName = element.getAttributeValue("menuName");
        String menuId = element.getAttributeValue("menuId");

        final Menu menu;

        if (menuId != null) {
            menu = menuService.findMenuById(Long.parseLong(menuId));
        } else {
            menu = menuService.findMenuByName(menuName);
        }

        addToModel(arguments, resultVar, menu);
    }
}
