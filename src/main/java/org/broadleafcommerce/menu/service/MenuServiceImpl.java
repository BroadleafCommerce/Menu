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
package org.broadleafcommerce.menu.service;

import org.apache.commons.collections.CollectionUtils;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.core.catalog.domain.CategoryXref;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.broadleafcommerce.menu.dao.MenuDao;
import org.broadleafcommerce.menu.domain.Menu;
import org.broadleafcommerce.menu.domain.MenuItem;
import org.broadleafcommerce.menu.dto.MenuItemDTO;
import org.broadleafcommerce.menu.type.MenuItemType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service("blMenuService")
public class MenuServiceImpl implements MenuService {
    
    @Resource(name = "blMenuDao")
    protected MenuDao menuDao;
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Override
    public Menu findMenuById(Long id) {
        return menuDao.readMenuById(id);
    }
    
    @Override
    public Menu findMenuByName(String menuName) {
        return menuDao.readMenuByName(menuName);
    }

    @Override
    public MenuItem findMenuItemById(Long menuItemId) {
        return menuDao.readMenuItemById(menuItemId);
    }

    @Override
    public List<MenuItemDTO> constructMenuItemDTOsForMenu(Menu menu) {
        List<MenuItemDTO> dtos = new ArrayList<MenuItemDTO>();
        if (CollectionUtils.isNotEmpty(menu.getMenuItems())) {
            for (MenuItem menuItem : menu.getMenuItems()) {
                dtos.add(convertMenuItemToDTO(menuItem));
            }
        }
        return dtos;
    }

    protected MenuItemDTO convertMenuItemToDTO(MenuItem menuItem) {

        Category category = null;
        if (MenuItemType.CATEGORY.equals(menuItem.getMenuItemType())) {
            category = catalogService.findCategoryByURI(menuItem.getActionUrl());
        }

        if (MenuItemType.SUBMENU.equals(menuItem.getMenuItemType()) &&
                menuItem.getLinkedMenu() != null) {
            MenuItemDTO dto = new MenuItemDTO();
            dto.setUrl(menuItem.getDerivedUrl());
            dto.setLabel(menuItem.getDerivedLabel());

            List<MenuItemDTO> submenu = new ArrayList<MenuItemDTO>();
            List<MenuItem> items = menuItem.getLinkedMenu().getMenuItems();
            if (CollectionUtils.isNotEmpty(items)) {
                for (MenuItem item : items) {
                    submenu.add(convertMenuItemToDTO(item));
                }
            }

            dto.setSubmenu(submenu);
            return dto;
        } else if (category != null) {
            return convertCategoryToMenuItemDTO(category);
        } else {
            MenuItemDTO dto = new MenuItemDTO();
            dto.setUrl(menuItem.getDerivedUrl());
            dto.setLabel(menuItem.getDerivedLabel());
            if (menuItem.getImage() != null) {
                dto.setImageUrl(menuItem.getImage().getUrl());
                dto.setAltText(menuItem.getAltText());
            }
            return dto;
        }

    }

    protected MenuItemDTO convertCategoryToMenuItemDTO(Category category) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setLabel(category.getName());
        dto.setUrl(category.getUrl());

        List<CategoryXref> categoryXrefs = category.getChildCategoryXrefs();

        if (CollectionUtils.isNotEmpty(categoryXrefs)) {
            List<MenuItemDTO> submenu = new ArrayList<MenuItemDTO>();
            for (CategoryXref xref : categoryXrefs) {
                submenu.add(convertCategoryToMenuItemDTO(xref.getSubCategory()));
            }

            dto.setSubmenu(submenu);
        }
        return dto;
    }

}
