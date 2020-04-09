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
package org.broadleafcommerce.menu.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                MenuItemDTO menuItemDTO = convertMenuItemToDTO(menuItem);

                if (menuItemDTO != null) {
                    dtos.add(menuItemDTO);
                }

            }
        }
        return dtos;
    }

    protected MenuItemDTO convertMenuItemToDTO(MenuItem menuItem) {
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
        } else if (MenuItemType.CATEGORY.equals(menuItem.getMenuItemType())) {
            Category category = catalogService.findCategoryByURI(menuItem.getActionUrl());

            if (category != null) {
                return convertCategoryToMenuItemDTO(category);
            } else {
                return null;
            }
        } else if (MenuItemType.CUSTOM.equals(menuItem.getMenuItemType())) {
            MenuItemDTO dto = createDto(menuItem);
            dto.setCustomHtml(menuItem.getCustomHtml());
            return dto;
        } else {
            return createDto(menuItem);
        }

    }
    
    protected MenuItemDTO convertCategoryToMenuItemDTO(final Category category) {
        Set<Category> convertedCategories = new HashSet<>();
        return convertCategoryToMenuItemDTO(category, convertedCategories);
    }
    
    protected MenuItemDTO convertCategoryToMenuItemDTO(final Category category, Set<Category> convertedCategories) {
        MenuItemDTO dto = createDto(category);
        List<CategoryXref> childXrefs = ListUtils.emptyIfNull(category.getChildCategoryXrefs());
        List<MenuItemDTO> submenu = new ArrayList<>();

        convertedCategories.add(category);
        
        for (CategoryXref childXref : childXrefs) {
            final Category childCategory = childXref.getSubCategory();
            
            if (!convertedCategories.contains(childCategory)) {
                submenu.add(convertCategoryToMenuItemDTO(childCategory, convertedCategories));
            }
        }
        
        dto.setSubmenu(submenu);
        
        return dto;
    }

    protected MenuItemDTO createDto(MenuItem menuItem) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setUrl(menuItem.getDerivedUrl());
        dto.setLabel(menuItem.getDerivedLabel());
        if (menuItem.getImageUrl() != null) {
            dto.setImageUrl(menuItem.getImageUrl());
            dto.setAltText(menuItem.getAltText());
        }
        return dto;
    }
    
    protected MenuItemDTO createDto(Category category) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.setLabel(category.getName());
        dto.setUrl(category.getUrl());
        dto.setCategoryId(category.getId());
        
        return dto;
    }
}
