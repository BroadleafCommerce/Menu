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

package org.broadleafcommerce.menu.web.expression;

import org.broadleafcommerce.common.web.expression.BroadleafVariableExpression;
import org.broadleafcommerce.core.catalog.domain.CategoryXref;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.dto.MenuItemDTO;
import org.broadleafcommerce.menu.type.MenuItemType;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * This Thymeleaf variable expression gives convenience methods that can be performed on Menu Item objects
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blMenuVariableExpression")
public class MenuVariableExpression implements BroadleafVariableExpression {

    @Override
    public String getName() {
        return "menu";
    }

    /**
     * Utility method to determine if the passed in menu item has an image
     * @return
     */    public boolean hasImage(MenuItem menuItem) {
        return MenuItemType.LINK.equals(menuItem.getMenuItemType()) && menuItem.getImageUrl() != null;
    }

    /**
     * Utility method to determine if the passed in menu item has submenu items
     * @return
     */
    public boolean hasSubmenuItems(MenuItem menuItem)  {
        return ((MenuItemType.SUBMENU.equals(menuItem.getMenuItemType()) && menuItem.getLinkedMenu() != null && !menuItem.getLinkedMenu().getMenuItems().isEmpty())
                || (MenuItemType.CATEGORY.equals(menuItem.getMenuItemType()) && menuItem.getLinkedCategory() != null && menuItem.getLinkedCategory().hasChildCategories()));
    }

    /**
     * Utility method to return the passed in menu item's submenu items if it contains it
     * @return
     */
    public List<MenuItemDTO> constructSubmenu(MenuItem menuItem)  {
        if (MenuItemType.SUBMENU.equals(menuItem.getMenuItemType()) &&
                menuItem.getLinkedMenu() != null) {
            List<MenuItem> items = menuItem.getLinkedMenu().getMenuItems();
            if (items != null && !items.isEmpty()) {
                List<MenuItemDTO> dtos = new ArrayList<MenuItemDTO>();
                for (MenuItem item : items) {
                    MenuItemDTO dto = new MenuItemDTO();
                    dto.setLabel(item.getLabel());
                    dto.setUrl(item.getUrl());
                    dtos.add(dto);
                }
                return dtos;
            }

        } else if (MenuItemType.CATEGORY.equals(menuItem.getMenuItemType()) &&
                menuItem.getLinkedCategory() != null) {
            List<CategoryXref> categoryXrefs = menuItem.getLinkedCategory().getChildCategoryXrefs();
            if (categoryXrefs != null && !categoryXrefs.isEmpty()) {
                List<MenuItemDTO> dtos = new ArrayList<MenuItemDTO>();
                for (CategoryXref xref : categoryXrefs) {
                    MenuItemDTO dto = new MenuItemDTO();
                    dto.setLabel(xref.getSubCategory().getName());
                    dto.setUrl(xref.getSubCategory().getUrl());
                    dtos.add(dto);
                }
                return dtos;
            }
        }

        return null;
    }



}
