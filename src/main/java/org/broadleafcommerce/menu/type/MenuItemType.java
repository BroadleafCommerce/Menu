/*-
 * #%L
 * BroadleafCommerce Menu
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
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
package org.broadleafcommerce.menu.type;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Menu Item Types
 *
 * @author bpolster
 *
 */
public class MenuItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<String, MenuItemType> TYPES = new HashMap<String, MenuItemType>();

    public static final MenuItemType LINK = new MenuItemType("LINK", "Link");
    public static final MenuItemType CATEGORY = new MenuItemType("CATEGORY", "Category");
    public static final MenuItemType PAGE = new MenuItemType("PAGE", "Page");
    public static final MenuItemType SUBMENU = new MenuItemType("SUBMENU", "Sub Menu");
    public static final MenuItemType PRODUCT = new MenuItemType("PRODUCT", "Product");
    public static final MenuItemType CUSTOM = new MenuItemType("CUSTOM", "Custom");


    public static MenuItemType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public MenuItemType() {
        //do nothing
    }

    public MenuItemType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MenuItemType other = (MenuItemType) obj;
        if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
