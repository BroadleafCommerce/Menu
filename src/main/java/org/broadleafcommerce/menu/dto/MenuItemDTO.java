/*
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2016 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License” located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License” located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */

package org.broadleafcommerce.menu.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Generic DTO object that represents the information to display a Menu Item.
 * Can be used on the front end as a way to easily iterate and display menu items of different types.
 *
 * @author Elbert Bautista (elbertbautista)
 */
public class MenuItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String label;
    protected String url;
    protected String imageUrl;
    protected String altText;

    /**
     * not always set, only if the menu item represents a category.
     */
    protected Long categoryId;

    protected List<MenuItemDTO> submenu = new ArrayList<MenuItemDTO>();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public List<MenuItemDTO> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<MenuItemDTO> submenu) {
        this.submenu = submenu;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
