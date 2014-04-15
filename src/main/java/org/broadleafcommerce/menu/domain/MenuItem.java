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
package org.broadleafcommerce.menu.domain;

import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.menu.type.MenuItemType;
import java.io.Serializable;


public interface MenuItem extends Serializable {

    /**
     * Id of this menuItem
     * @return
     */
    Long getId();

    /**
     * Sets the id of this menuItem.
     * @param id
     */
    void setId(Long id);

    /**
     * The value to show for this menu item when displayed on a site.
     */
    String getName();

    /**
     * Sets the value for this menu item to display on a site.
     * @param label
     */
    void setName(String name);

    /**
     * The value to show for this menu item when displayed on a site.
     */
    String getLabel();

    /**
     * Sets the value for this menu item to display on a site.
     * @param label
     */
    void setLabel(String label);

    /**
     * Returns the {@link MenuItemType}
     * @return
     */
    MenuItemType getMenuItemType();

    /**
     * Sets the {@link MenuItemType} 
     * @param menuItemType
     */
    void setMenuItemType(MenuItemType menuItemType);

    /**
     * Returns the URL (if applicable) that should be targeted when this menu is clicked.
     * @return
     */
    String getActionUrl();

    /**
     * Sets the URL to go to if this menu is clicked.
     * @param actionUrl
     */
    void setActionUrl(String actionUrl);

    /**
     * Returns the {@link Menu} to which this menuItem belongs
     * @return
     */
    Menu getParentMenu();

    /**
     * Sets the {@link Menu} to which this menuItem belongs
     * @param menu
     */
    void setParentMenu(Menu menu);
    
    /**
     * For items of type {@link MenuItemType#CATEGORY}, stores the associated category.
     * @return
     */
    Category getLinkedCategory();

    /**
     * For items of type {@link MenuItemType#CATEGORY}, returns the associated category.
     * @return
     */
    void setLinkedCategory(Category linkedCategory);

    /**
     * The menu represented by this MenuItem.   (Allows for nested menus).
     * @return
     */
    Menu getLinkedMenu();

    /**
     * Sets the linked menu.
     * @param menu
     */
    void setLinkedMenu(Menu menu);

    /**
     * Sets the order of this MenuItem in the {@link #getParentMenu()}
     * @param sequence
     */
    void setSequence(Long sequence);

    /**
     * Returns the order of this MenuItem in the {@link #getParentMenu()}
     * @return
     */
    Long getSequence();

}