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
package org.broadleafcommerce.menu.domain;

import org.broadleafcommerce.cms.page.domain.Page;
import org.broadleafcommerce.common.copy.MultiTenantCloneable;
import org.broadleafcommerce.menu.type.MenuItemType;

import java.io.Serializable;
import java.math.BigDecimal;

public interface MenuItem extends Serializable, MultiTenantCloneable<MenuItem> {

    /**
     * Id of this menuItem
     *
     * @return
     */
    Long getId();

    /**
     * Sets the id of this menuItem.
     *
     * @param id
     */
    void setId(Long id);

    /**
     * Returns the label for this menu item
     *
     * @return
     * @see MenuItem.getDerivedLabel() to get the label based on MenuItemType
     */
    String getLabel();

    /**
     * Sets the value for this menu item to display on a site.
     *
     * @param label
     */
    void setLabel(String label);

    /**
     * Returns the {@link MenuItemType}
     *
     * @return
     */
    MenuItemType getMenuItemType();

    /**
     * Sets the {@link MenuItemType}
     *
     * @param menuItemType
     */
    void setMenuItemType(MenuItemType menuItemType);

    /**
     * Returns the URL (if applicable) that should be targeted when this menu is clicked.
     *
     * @return
     */
    String getActionUrl();

    /**
     * Sets the URL to go to if this menu is clicked.
     *
     * @param actionUrl
     */
    void setActionUrl(String actionUrl);

    /**
     * For items of type {@link MenuItemType#LINK}, gets an Image associated with this Menu Item
     *
     * @return
     */
    String getImageUrl();

    /**
     * For items of type {@link MenuItemType#LINK}, sets an Image for this Menu Item
     *
     * @param imageUrl
     */
    void setImageUrl(String imageUrl);

    /**
     * Returns the {@link Menu} to which this menuItem belongs
     *
     * @return
     */
    Menu getParentMenu();

    /**
     * Sets the {@link Menu} to which this menuItem belongs
     *
     * @param menu
     */
    void setParentMenu(Menu menu);

    /**
     * The menu represented by this MenuItem.   (Allows for nested menus).
     *
     * @return
     */
    Menu getLinkedMenu();

    /**
     * Sets the linked menu.
     *
     * @param menu
     */
    void setLinkedMenu(Menu menu);

    /**
     * Sets the order of this MenuItem in the {@link #getParentMenu()}
     *
     * @param sequence
     */
    void setSequence(BigDecimal sequence);

    /**
     * Returns the order of this MenuItem in the {@link #getParentMenu()}
     *
     * @return
     */
    BigDecimal getSequence();

    /**
     * For items of type {@link MenuItemType#LINK}, gets the alt text for the associated image.
     *
     * @return
     */
    String getAltText();

    /**
     * For items of type {@link MenuItemType#LINK}, stores the alt text for the associated image.
     *
     * @return
     */
    void setAltText(String altText);

    /**
     * For items of type {@link MenuItemType#PAGE}, returns the associated page.
     *
     * @return
     */
    Page getLinkedPage();

    /**
     * For items of type {@link MenuItemType#PAGE}, sets the associated page.
     *
     * @return
     */
    void setLinkedPage(Page linkedPage);

    /**
     * For items of type {@link MenuItemType#CUSTOM}, returns the associated custom HTML.
     *
     * @return
     */
    String getCustomHtml();

    /**
     * For items of type {@link MenuItemType#CUSTOM}, sets the associated custom HTML.
     *
     * @return
     */
    void setCustomHtml(String customHtml);

    /**
     * Convenience method that returns an anchor URL based on the configured MenuItemType.
     * Will return getActionUrl() unless type is {@link MenuItemType#PAGE},
     * {@link MenuItemType#PRODUCT}, or {@link MenuItemType#CATEGORY} and will use corresponding linked URL.
     *
     * @return
     */
    String getDerivedUrl();

    /**
     * Convenience method that will always return the configured Label if specified.
     * If it is not specified and of type {@link MenuItemType#PRODUCT}, or {@link MenuItemType#CATEGORY},
     * or {@link MenuItemType#SUBMENU}, it will return the corresponding linked name.
     */
    String getDerivedLabel();

}
